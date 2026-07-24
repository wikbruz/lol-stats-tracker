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

    /**
     * Looks up the current Riot account. Riot Id is hardcoded for now -
     * parameterizing this into a request param is a planned next step. The aim
     * is for the user to be able to look up any player, rather than this account.
     *
     * @return the account details, including its {@code puuid}
     */
    @GetMapping("/account")
    public RiotAccount getAccount() {
        return  riotService.getAccount(GAME_NAME, TAG_LINE);
    }

    /**
     * Fetches the 10 most recent match IDs for the account.
     *
     * @return match Ids, most recent first
     */
    @GetMapping("/matches")
    public List<String> getMatches() {
        String puuid = riotService.getAccount(GAME_NAME, TAG_LINE).getPuuid();
        return riotService.getMatchIds(puuid,10);
    }

    /**
     * Fetches raw stats and resolved rune page for the account's
     * most recent fetch.
     *
     * @return combined raw participant stats and resolved rune page
     */
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

    /**
     * Fetches aggregated stats across the accounts 10 most recent matches.
     *
     * @return averageK/D/A, CS/min, vision, wards bought and kill participation.
     */
    @GetMapping("/summary")
    public PlayerStatsSummary getSummary() {
        String puuid = riotService.getAccount(GAME_NAME, TAG_LINE).getPuuid();
        return riotService.getPlayerStatsSummary(puuid, 10);
    }
}
