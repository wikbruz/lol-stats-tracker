package com.wbruzgo.lolstatstrack;

public class PlayerStatsSummary {
    private int gamesPlayed;
    private int wins;
    private int losses;
    private double averageKills;
    private double averageDeaths;
    private double averageAssists;
    private double averageCsPerMin;
    private double averageVisionScore;
    private double averageControlWardsBought;
    private double averageWardsPlaced;
    private double averageWardsDestroyed;
    private double averageKillParticipation;

    public int getGamesPlayed() {
        return gamesPlayed;
    }
    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getWins() {
        return wins;
    }
    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }
    public void setLosses(int losses) {
        this.losses = losses;
    }

    public double getAverageKills() {
        return averageKills;
    }
    public void setAverageKills(double averageKills) {
        this.averageKills = averageKills;
    }

    public double getAverageDeaths() {
        return averageDeaths;
    }
    public void setAverageDeaths(double averageDeaths) {
        this.averageDeaths = averageDeaths;
    }

    public double getAverageAssists() {
        return averageAssists;
    }
    public void setAverageAssists(double averageAssists) {
        this.averageAssists = averageAssists;
    }

    public double getAverageCsPerMin() {
        return averageCsPerMin;
    }
    public void setAverageCsPerMin(double averageCsPerMin) {
        this.averageCsPerMin = averageCsPerMin;
    }

    public double getAverageVisionScore() {
        return averageVisionScore;
    }
    public void setAverageVisionScore(double averageVisionScore) {
        this.averageVisionScore = averageVisionScore;
    }

    public double getAverageControlWardsBought() {
        return averageControlWardsBought;
    }
    public void setAverageControlWardsBought(double averageControlWardsBought) {
        this.averageControlWardsBought = averageControlWardsBought;
    }

    public double getAverageWardsPlaced() {
        return averageWardsPlaced;
    }
    public void setAverageWardsPlaced(double averageWardsPlaced) {
        this.averageWardsPlaced = averageWardsPlaced;
    }

    public double getAverageWardsDestroyed() {
        return averageWardsDestroyed;
    }
    public void setAverageWardsDestroyed(double averageWardsDestroyed) {
        this.averageWardsDestroyed = averageWardsDestroyed;
    }

    public double getAverageKillParticipation() {
        return averageKillParticipation;
    }
    public void setAverageKillParticipation(double averageKillParticipation) {
        this.averageKillParticipation = averageKillParticipation;
    }
}
