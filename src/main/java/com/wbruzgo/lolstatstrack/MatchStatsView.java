package com.wbruzgo.lolstatstrack;

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
