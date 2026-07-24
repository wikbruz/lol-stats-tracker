package com.wbruzgo.lolstatstrack;

import java.util.List;

public class RunePageView {
    private String primaryTreeName;
    private List<String> primaryRuneNames;
    private String secondaryTreeName;
    private List<String> secondaryRuneNames;
    private String offenseShardName;
    private String flexShardName;
    private String defenseShardName;

    public String getPrimaryTreeName() {
        return primaryTreeName;
    }
    public void setPrimaryTreeName(String primaryTreeName) {
        this.primaryTreeName = primaryTreeName;
    }

    public List<String> getPrimaryRuneNames() {
        return primaryRuneNames;
    }
    public void setPrimaryRuneNames(List<String> primaryRuneNames) {
        this.primaryRuneNames = primaryRuneNames;
    }

    public String getSecondaryTreeName() {
        return secondaryTreeName;
    }
    public void setSecondaryTreeName(String secondaryTreeName) {
        this.secondaryTreeName = secondaryTreeName;
    }

    public List<String> getSecondaryRuneNames() {
        return secondaryRuneNames;
    }
    public void setSecondaryRuneNames(List<String> secondaryTreeRunes) {
        this.secondaryRuneNames = secondaryTreeRunes;
    }

    public String getOffenseShardName() {
        return offenseShardName;
    }
    public void setOffenseShardName(String offenseShardName) {
        this.offenseShardName = offenseShardName;
    }

    public String getFlexShardName() {
        return flexShardName;
    }
    public void setFlexShardName(String flexShardName) {
        this.flexShardName = flexShardName;
    }

    public String getDefenseShardName() {
        return defenseShardName;
    }
    public void setDefenseShardName(String defenseShardName) {
        this.defenseShardName = defenseShardName;
    }
}
