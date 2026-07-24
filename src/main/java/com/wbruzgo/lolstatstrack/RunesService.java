package com.wbruzgo.lolstatstrack;

import jakarta.annotation.PostConstruct;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolves numeric Riot rune/tree/shard IDs (as seen on
 * {@link MatchDetails.Participant.Perks}) into their display names.
 * <p>
 *     Rune and tree names are loaded once at startup from Riot's Data Dragon
 *     static assets - a separate, unauthenticated host from the live match-v5
 *     API, versioned per patch. Stat shard names are <b>not</b> included in
 *     Data Dragon's rune data, so they're maintained as a small hardcoded map
 *     instead.
 * </p>
 */
@Service
public class RunesService {

    private final RestClient restClient = RestClient.create();
    private final Map<Integer, String> runeNamesById = new HashMap<>();
    private final Map<Integer, String> treeNamesById = new HashMap<>();

    /**
     * Maps stat shard IDs to display names. Not sourced from Data Dragon -
     * shards aren't part of {@code runesReforged.json} - so this is
     * maintained by hand. Shard IDs are stable and rarely change.
     */
    private static final Map<Integer, String> SHARD_NAMES = Map.of(
            5008, "Adaptive Force",
            5005, "Attack Speed",
            5007, "Ability Haste",
            5002, "Armor",
            5003, "Magic Resist",
            5001, "Health Scaling"
    );

    /**
     * Fetches the current patch's rune tree data from Data Dragon and
     * populates {@link #runeNamesById} and {@link #treeNamesById}.
     * <p>
     *     Runs once at application startup. If Data Dragon is unreachable at
     *     that point, startup fails - tradeoff for a personal project to not need
     *     null-checks/lazy-loading logic on every lookup.
     * </p>
     */
    @PostConstruct
    public void loadRuneData() {
        String latestVersion = restClient.get()
                .uri("https://ddragon.leagueoflegends.com/api/versions.json")
                .retrieve()
                .body(new ParameterizedTypeReference<List<String>>() {})
                .get(0);

        List<RuneTree> trees = restClient.get()
                .uri("https://ddragon.leagueoflegends.com/cdn/{version}/data/en_US/runesReforged.json", latestVersion)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        for (RuneTree tree : trees) {
            treeNamesById.put(tree.getId(), tree.getName());
            for (RuneTree.Slot slot : tree.getSlots()) {
                for (RuneTree.Rune rune : slot.getRunes()) {
                    runeNamesById.put(rune.getId(), rune.getName());
                }
            }
        }
    }

    /**
     * Resolves a specific rune's ID (e.g. the keystone or a minor rune) to
     * its display name.
     *
     * @param perkId the rune ID, as returned in a {@code PerkSelection}
     * @return the rune's name, or {@code "Unknown Rune"} if not found.
     */
    public String getRuneName(int perkId) {
        return runeNamesById.getOrDefault(perkId, "Unknown Rune");
    }

    /**
     *  Resolves a rune tree's ID (e.g. Domination, Resolve) to its display name.
     *
     * @param styleId the tree ID, as returned in a {@code PerkStyle}
     * @return the tree's name, or {@code "Unknown Tree"} if not found
     */
    public String getTreeName(int styleId) {
        return treeNamesById.getOrDefault(styleId, "Unknown Tree");
    }

    /**
     * Resolves a stat shard's ID to its display name.
     *
     * @param shardId the shard ID, as returned in {@code StatPerks}
     * @return the shard's name, or {@code "Unknown Shard"} if not found
     */
    public String getShardName(int shardId) {
        return SHARD_NAMES.getOrDefault(shardId, "Unknown Shard");
    }

    /**
     * Builds a fully-resolved {@link RunePageView} — primary and secondary
     * tree names, their selected rune names, and all three stat shard
     * names — from a match participant's raw {@link MatchDetails.Participant.Perks}.
     *
     * @param perks the participant's raw perks data from a match
     * @return a {@link RunePageView} with every ID resolved to a display name
     */
    public RunePageView resolveRunePage(MatchDetails.Participant.Perks perks){
        RunePageView view = new RunePageView();

        for (MatchDetails.Participant.PerkStyle style : perks.getStyles()){
            List<String> names = style.getSelections().stream()
                    .map(selection -> getRuneName(selection.getPerk()))
                    .toList();

            if ("primaryStyle".equals((style.getDescription()))) {
                view.setPrimaryTreeName(getTreeName(style.getStyle()));
                view.setPrimaryRuneNames(names);
            } else if ("subStyle".equals(style.getDescription())) {
                view.setSecondaryTreeName(getTreeName(style.getStyle()));
                view.setSecondaryRuneNames(names);
            }
        }

        MatchDetails.Participant.StatPerks statPerks = perks.getStatPerks();
        view.setOffenseShardName(getShardName(statPerks.getOffense()));
        view.setFlexShardName(getShardName(statPerks.getFlex()));
        view.setDefenseShardName(getShardName(statPerks.getDefense()));

        return view;
    }
}

