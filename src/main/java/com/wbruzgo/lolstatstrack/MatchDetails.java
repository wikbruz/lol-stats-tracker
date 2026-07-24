package com.wbruzgo.lolstatstrack;

import java.util.List;

public class MatchDetails {
    private Info info;

    public Info getInfo() { return info; }
    public void setInfo(Info info) {this.info = info; }

    public static class Info {
        private long gameDuration;
        private List<Participant> participants;

        public long getGameDuration() {
            return gameDuration;
        }
        public void setGameDuration(long gameDuration) {this.gameDuration = gameDuration;}

        public List<Participant> getParticipants() {return participants;}
        public void setParticipants(List<Participant> participants) {this.participants = participants;}
    }

    public static class Participant {

        public static class Perks {
            private StatPerks statPerks;
            private List<PerkStyle> styles;

            public StatPerks getStatPerks() {
                return statPerks;
            }
            public void setStatPerks(StatPerks statPerks) {
                this.statPerks = statPerks;
            }

            public List<PerkStyle> getStyles() {
                return styles;
            }
            public void setStyles(List<PerkStyle> styles) {
                this.styles = styles;
            }
        }

        public static class StatPerks {
            private int offense, flex, defense;

            public int getOffense() {
                return offense;
            }
            public void setOffense(int offence) {
                this.offense = offence;
            }

            public int getFlex() {
                return flex;
            }
            public void setFlex(int flex) {
                this.flex = flex;
            }

            public int getDefense() {
                return defense;
            }
            public void setDefense(int defense) {
                this.defense = defense;
            }
        }

        public static class PerkStyle {
            private String description;
            private int style;
            private List<PerkSelection> selections;

            public String getDescription() {
                return description;
            }
            public void setDescription(String description) {
                this.description = description;
            }

            public int getStyle() {
                return style;
            }
            public void setStyle(int style) {
                this.style = style;
            }

            public List<PerkSelection> getSelections() {
                return selections;
            }
            public void setSelections(List<PerkSelection> selections) {
                this.selections = selections;
            }
        }

        public static class PerkSelection {
            private  int perk;

            public int getPerk() {
                return perk;
            }
            public void setPerk(int perk) {
                this.perk = perk;
            }
        }

        public static class Challenges {
            private double killParticipation;

            public double getKillParticipation() {
                return killParticipation;
            }
            public void setKillParticipation(double killParticipation) {
                this.killParticipation = killParticipation;
            }
        }

        private String puuid;
        private String championName;
        private int kills;
        private int deaths;
        private int assists;
        private boolean win;
        private int visionScore;
        private int totalMinionsKilled;
        private int neutralMinionsKilled;
        private int visionWardsBoughtInGame;
        private int wardsPlaced;
        private int wardsKilled;
        private Challenges challenges;
        private Perks perks;

        public String getPuuid() { return puuid; }
        public void setPuuid(String puuid){ this.puuid = puuid;}

        public String getChampionName() {return championName; }
        public void setChampionName(String championName) {
            this.championName = championName;
        }

        public int getKills() {
            return kills;
        }
        public void setKills(int kills) {
            this.kills = kills;
        }

        public int getDeaths() {
            return deaths;
        }
        public void setDeaths(int deaths) {
            this.deaths = deaths;
        }

        public int getAssists() {
            return assists;
        }
        public void setAssists(int assists) {
            this.assists = assists;
        }

        public boolean isWin() {
            return win;
        }
        public void setWin(boolean win) {
            this.win = win;
        }

        public int getVisionScore() {
            return visionScore;
        }
        public void setVisionScore(int visionScore) {
            this.visionScore = visionScore;
        }

        public int getTotalMinionsKilled() {
            return totalMinionsKilled;
        }
        public void setTotalMinionsKilled(int totalMinionsKilled) {
            this.totalMinionsKilled = totalMinionsKilled;
        }

        public int getNeutralMinionsKilled() {
            return neutralMinionsKilled;
        }
        public void setNeutralMinionsKilled(int neutralMinionsKilled) {
            this.neutralMinionsKilled = neutralMinionsKilled;
        }

        public int getVisionWardsBoughtInGame() {
            return visionWardsBoughtInGame;
        }
        public void setVisionWardsBoughtInGame(int visionWardsBoughtInGame) {
            this.visionWardsBoughtInGame = visionWardsBoughtInGame;
        }

        public int getWardsPlaced() {
            return wardsPlaced;
        }
        public void setWardsPlaced(int wardsPlaced) {
            this.wardsPlaced = wardsPlaced;
        }

        public int getWardsKilled() {
            return wardsKilled;
        }
        public void setWardsKilled(int wardsKilled) {
            this.wardsKilled = wardsKilled;
        }

        public Challenges getChallenges() {
            return challenges;
        }
        public void setChallenges(Challenges challenges) {
            this.challenges = challenges;
        }

        public Perks getPerks() {
            return perks;
        }
        public void setPerks(Perks perks) {
            this.perks = perks;
        }
    }
}

