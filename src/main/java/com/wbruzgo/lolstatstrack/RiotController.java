package com.wbruzgo.lolstatstrack;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RiotController {

    private final RiotService riotService;

    private static final String GAME_NAME = "meowvie";
    private static final String TAG_LINE = "MINGI";

    public RiotController(RiotService riotService) {
        this.riotService = riotService;
    }

    @GetMapping("/account")
    public RiotAccount getAccount() {
        return  riotService.getAccount(GAME_NAME, TAG_LINE);
    }

    @GetMapping("/matches")
    public List<String> getMatches() {
        String puuid = riotService.getAccount(GAME_NAME, TAG_LINE).getPuuid();
        return riotService.getMatchIds(puuid,10);
    }

    @GetMapping("/match-stats")
    public MatchDetails.Participant getMatchStats() {
        String puuid = riotService.getAccount(GAME_NAME, TAG_LINE).getPuuid();
        List<String> matchIds = riotService.getMatchIds(puuid, 10);
        String mostRecentMatchId = matchIds.get(0);

        MatchDetails match = riotService.getMatchDetails(mostRecentMatchId);

        return match.getInfo().getParticipants().stream()
                .filter(p -> p.getPuuid().equals(puuid))
                .findFirst()
                .orElseThrow();
    }

    @GetMapping("/summary")
    public PlayerStatsSummary getSummary() {
        String puuid = riotService.getAccount(GAME_NAME, TAG_LINE).getPuuid();
        return riotService.getPlayerStatsSummary(puuid, 10);
    }
}
