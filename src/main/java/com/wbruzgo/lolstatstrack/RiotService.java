package com.wbruzgo.lolstatstrack;

import  org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.List;

@Service
public class RiotService {

    @Value("${riot.api.key}")
    private String apiKey;

    private final RestClient restClient = RestClient.create();

    public RiotAccount getAccount(String gameName, String tagLine) {
        String url = "https://europe.api.riotgames.com/riot/account/v1/accounts/by-riot-id"
                + "/" + gameName + "/" + tagLine;

        return restClient.get()
                .uri(url)
                .header("X-Riot-Token", apiKey)
                .retrieve()
                .body(RiotAccount.class);
    }

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

    //creating a record to store all game stats instead of updating each one individually
    private record  GameStats(
            boolean win, int kills, int deaths, int assists,
            double csPerMin, int visionScore, int controlWards,
            int wardsPlaced, int wardsKilled, double killParticipation
    ) {}

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
