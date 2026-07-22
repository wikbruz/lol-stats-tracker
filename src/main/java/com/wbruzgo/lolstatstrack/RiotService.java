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
                .body(new ParameterizedTypeReference<List<String>>() {});
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

    public PlayerStatsSummary getPlayerStatsSummary(String puuid, int numberOfGames) {
        List<String> matchIds = getMatchIds(puuid, numberOfGames);

        int wins = 0;
        int totalKills = 0;
        int totalDeaths = 0;
        int totalAssists = 0;

        for (String matchId : matchIds) {
            MatchDetails match = getMatchDetails(matchId);

            MatchDetails.Participant me = match.getInfo().getParticipants().stream()
                    .filter(p -> p.getPuuid().equals(puuid))
                    .findFirst()
                    .orElseThrow();

            if (me.isWin()) {
                wins++;
            }
            totalKills += me.getKills();
            totalDeaths += me.getDeaths();
            totalAssists += me.getAssists();
        }

        PlayerStatsSummary summary = new PlayerStatsSummary();
        summary.setGamesPlayed(matchIds.size());
        summary.setWins(wins);
        summary.setLosses(matchIds.size() - wins);
        summary.setAverageKills((double) totalKills / matchIds.size());
        summary.setAverageDeaths((double) totalDeaths / matchIds.size());
        summary.setAverageAssists((double) totalAssists / matchIds.size());

        return summary;
    }

}
