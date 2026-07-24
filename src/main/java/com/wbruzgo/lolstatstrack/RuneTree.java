package com.wbruzgo.lolstatstrack;

import java.util.List;

/**
 * Mirrors a single rune tree from data Dragon's {@code runesReforged.json}.
 * it's {@code id} corresponds to the {@code style} field on {@link MatchDetails.Participant.PerkStyle}.
 */
public class RuneTree {
    private int id;
    private String key;
    private String name;
    private List<Slot> slots;

    /**
     * One row within a rune tree, containing the choices of rune available for that row.
     */
    public static class Slot {
        private List<Rune> runes;

        public List<Rune> getRunes() {
            return runes;
        }
        public void setRunes(List<Rune> runes) {
            this.runes = runes;
        }
    }

    /**
     * A single, individual rune. This sits at a different level of the
     * tree structure than {@link RuneTree} itself, thus needing to be looked up
     * separately. Its {@code id} corresponds to the {@code perk} field on
     * {@link MatchDetails.Participant.PerkSelection}, not the {@code style} field.
     */
    public static class Rune {
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
