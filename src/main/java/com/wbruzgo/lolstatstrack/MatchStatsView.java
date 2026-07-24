package com.wbruzgo.lolstatstrack;

/**
 * Combined response for a single match with raw participant stats
 * from Riot paired with the resolved rune page for that same participant.
 */
public class MatchStatsView {
    private MatchDetails.Participant participant;
    private RunePageView runes;

    public MatchDetails.Participant getParticipant(){ return participant; }
    public void setParticipant(MatchDetails.Participant participant) {
        this.participant = participant;
    }

    public RunePageView getRunes() {
        return runes;
    }
    public void setRunes(RunePageView runes) {
        this.runes = runes;
    }
}
