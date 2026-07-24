package com.wbruzgo.lolstatstrack;

import  org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.List;

/**
 * Handles all outbound calls to the Riot Games API (match-v5 and account v-1),
 * and aggregates raw match data into player-facing summaries.
 * <p>
 *  ALL requests use Riot's regional routing (e.g. {@code eurpoe.api.riotgames.com}),
 *  not platform routing (e.g. {@code euw1}) - account and match endpoints require
 *  the regional host, unlike summoner-level endpoints.
 * </p>
 */
@Service
public class RiotService {

    @Value("${riot.api.key}")
    private String apiKey;

    private final RestClient restClient = RestClient.create();

    /**
     * Looks up a Riot account by Riot ID.
     *
     * @param gameName the player's in-game name
     * @param tagLine the player's tag
     * @return the matching {@link RiotAccount}, including its {@code puuid}
     */
    public RiotAccount getAccount(String gameName, String tagLine) {
        String url = "https://europe.api.riotgames.com/riot/account/v1/accounts/by-riot-id"
                + "/" + gameName + "/" + tagLine;

        return restClient.get()
                .uri(url)
                .header("X-Riot-Token", apiKey)
                .retrieve()
                .body(RiotAccount.class);
    }

    /**
     * Fetches a player's most recent match IDs.
     *
     * @param puuid player's unique Riot identifier
     * @param count how many recent match IDs to retrieve
     * @return match IDs, most recent first
     */
    public List<String> getMatchIds(String puuid, int count) {
        String url = String.format(
                "https://europe.api.riotgames.com/lol/match/v5/matches/by-puuid/%s/ids?start=0&count=%d",
                puuid, count
        );

        return restClient.get()
                .uri(url)
                .header("X-Riot-Token", apiKey)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    /**
     * Fetches full details for a single match
     *
     * @param matchId the Riot match ID (as returned by {@link #getMatchIds})
     * @return the full {@link MatchDetails} for that match
     */
    public MatchDetails getMatchDetails(String matchId) {
        String url = String.format(
                "https://europe.api.riotgames.com/lol/match/v5/matches/%s",
                matchId
        );

        return restClient.get()
                .uri(url)
                .header("X-Riot-Token", apiKey)
                .retrieve()
                .body(MatchDetails.class);
    }

    /**
     * Holds one game's derived stats for a single player, used as an
     * intermediate step when building a {@link PlayerStatsSummary} - avoids
     * tracking a separate running total for each stat individually
     * @param win wether the game is a win or a loss
     * @param kills number of kills the player accumulated
     * @param deaths number of deaths the player accumulated
     * @param assists number of assists on kills the player accumulated
     * @param csPerMin creep score (number of enemy minions + neutral jungle monsters) the player killed, calculated as
     *                 a per-minute average, as is standard in most League of Legends stats breakdowns
     * @param visionScore vision score achieved by the player, calculated by Riot during the game
     * @param controlWards number of Control Wards items the player bought in the shop over the course of the game
     * @param wardsPlaced number of wards (control + stealth) placed by the player over the duration of the game
     * @param wardsKilled number of enemy wards destroyed by the player over the course of the game
     * @param killParticipation amount of kills + assists accumulated by the player in relation to the total team kills (generally a percentage)
     */
    private record  GameStats(
            boolean win, int kills, int deaths, int assists,
            double csPerMin, int visionScore, int controlWards,
            int wardsPlaced, int wardsKilled, double killParticipation
    ) {}

    /**
     * Fetches and aggregates a player's stats across their most recent matches.
     * <p>
     *     Combines kills/deaths/assists, CS/min, vision, ward and KP
     *     averages into a single summary. KP is taken directly from Riot, not computed manually.
     * </p>
     * @param puuid player's unique Riot identifier
     * @param numberOfGames how many recent matches to include in the aggregate
     * @return a {@link PlayerStatsSummary} of the computed averages
     * @throws java.util.NoSuchElementException is a fetched match doesn't
     * contain a participant matching {@code puuid}
     */
    public PlayerStatsSummary getPlayerStatsSummary(String puuid, int numberOfGames) {
        List<String> matchIds = getMatchIds(puuid, numberOfGames);

        List<GameStats> games = matchIds.stream()
                .map(matchId -> {
                    MatchDetails match = getMatchDetails(matchId);
                    long gameDurationSeconds = match.getInfo().getGameDuration();

                    MatchDetails.Participant p = match.getInfo().getParticipants().stream()
                            .filter(participant -> participant.getPuuid().equals(puuid))
                            .findFirst()
                            .orElseThrow();

                    int cs = p.getTotalMinionsKilled() + p.getNeutralMinionsKilled();
                    double csPerMin = cs / (gameDurationSeconds / 60.0);

                    return new GameStats(
                            p.isWin(), p.getKills(), p.getDeaths(), p.getAssists(),
                            csPerMin, p.getVisionScore(), p.getVisionWardsBoughtInGame(),
                            p.getWardsPlaced(), p.getWardsKilled(), p.getChallenges().getKillParticipation()
                    );
                })
                .toList();

        PlayerStatsSummary summary = new PlayerStatsSummary();
        int gamesPlayed = games.size();
        long wins = games.stream().filter(GameStats::win).count();

        summary.setGamesPlayed(gamesPlayed);
        summary.setWins((int) wins);
        summary.setLosses(gamesPlayed - (int) wins);
        summary.setAverageKills(games.stream().mapToInt(GameStats::kills).average().orElse(0));
        summary.setAverageDeaths(games.stream().mapToInt(GameStats::deaths).average().orElse(0));
        summary.setAverageAssists(games.stream().mapToInt(GameStats::assists).average().orElse(0));
        summary.setAverageCsPerMin(games.stream().mapToDouble(GameStats::csPerMin).average().orElse(0));
        summary.setAverageVisionScore(games.stream().mapToInt(GameStats::visionScore).average().orElse(0));
        summary.setAverageControlWardsBought(games.stream().mapToInt(GameStats::controlWards).average().orElse(0));
        summary.setAverageWardsPlaced(games.stream().mapToInt(GameStats::wardsPlaced).average().orElse(0));
        summary.setAverageWardsDestroyed(games.stream().mapToInt(GameStats::wardsKilled).average().orElse(0));
        summary.setAverageKillParticipation(games.stream().mapToDouble(GameStats::killParticipation).average().orElse(0));

        return summary;
    }

}
