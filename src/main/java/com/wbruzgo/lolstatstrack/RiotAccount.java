package com.wbruzgo.lolstatstrack;

/**
 * Mirrors riot's account-v1 {@code /accounts/by-riot-id/{gameName}/{tagLine}} response.
 */
public class RiotAccount {
    private String puuid;
    private String gameName;
    private String tagLine;

    public String getPuuid() { return puuid; }
    public void setPuuid(String puuid) { this.puuid = puuid; }

    public String getGameName() { return gameName; }
    public void  setGameName(String gameName) {this.gameName = gameName; }

    public String getTagLine() { return tagLine; }
    public void setTagLine(String tagLine) { this.tagLine = tagLine; }

}
