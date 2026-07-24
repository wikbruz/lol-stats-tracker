package com.wbruzgo.lolstatstrack;

import java.util.List;

public class RuneTree {
    private int id;
    private String key;
    private String name;
    private List<Slot> slots;

    public static class Slot {
        private List<Rune> runes;

        public List<Rune> getRunes() {
            return runes;
        }
        public void setRunes(List<Rune> runes) {
            this.runes = runes;
        }
    }

    public static class Rune {
        /*These values sit at diff levels of the tree structure
        * and need to be looked up separately. The variables here
        * are for each individual rune in a rune tree*/
        private int id;
        private String key;
        private String name;

        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<Slot> getSlots() {
        return slots;
    }
    public void setSlots(List<Slot> slots) {
        this.slots = slots;
    }
}
