package com.wbruzgo.lolstatstrack;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RiotController {

    private final RiotService riotService;
    private final RunesService runesService;

    private static final String GAME_NAME = "meowvie";
    private static final String TAG_LINE = "MINGI";

    public RiotController(RiotService riotService, RunesService runesService) {
        this.riotService = riotService;
        this.runesService = runesService;
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
    public MatchStatsView getMatchStats() {
        String puuid = riotService.getAccount(GAME_NAME, TAG_LINE).getPuuid();
        List<String> matchIds = riotService.getMatchIds(puuid, 10);
        String mostRecentMatchId = matchIds.get(0);

        MatchDetails match = riotService.getMatchDetails(mostRecentMatchId);

        MatchDetails.Participant participant = match.getInfo().getParticipants().stream()
                .filter(p -> p.getPuuid().equals(puuid))
                .findFirst()
                .orElseThrow();

        MatchStatsView view = new MatchStatsView();
        view.setParticipant(participant);
        view.setRunes(runesService.resolveRunePage(participant.getPerks()));

        return view;
    }

    @GetMapping("/summary")
    public PlayerStatsSummary getSummary() {
        String puuid = riotService.getAccount(GAME_NAME, TAG_LINE).getPuuid();
        return riotService.getPlayerStatsSummary(puuid, 10);
    }
}
