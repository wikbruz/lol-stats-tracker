package com.wbruzgo.lolstatstrack;

import jakarta.annotation.PostConstruct;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RunesService {

    private final RestClient restClient = RestClient.create();
    private final Map<Integer, String> runeNamesById = new HashMap<>();
    private final Map<Integer, String> treeNamesById = new HashMap<>();

    private static final Map<Integer, String> SHARD_NAMES = Map.of(
            5008, "Adaptive Force",
            5005, "Attack Speed",
            5007, "Ability Haste",
            5002, "Armor",
            5003, "Magic Resist",
            5001, "Health Scaling"
    );

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

    public String getRuneName(int perkId) {
        return runeNamesById.getOrDefault(perkId, "Unknown Rune");
    }

    public String getTreeName(int styleId) {
        return treeNamesById.getOrDefault(styleId, "Unknown Tree");
    }

    public String getShardName(int shardId) {
        return SHARD_NAMES.getOrDefault(shardId, "Unknown Shard");
    }

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

