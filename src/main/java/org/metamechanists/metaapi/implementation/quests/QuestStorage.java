package org.metamechanists.metaapi.implementation.quests;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.metamechanists.metaapi.implementation.Quests;
import org.metamechanists.metaapi.util.Log;
import org.metamechanists.metaapi.util.TextUtil;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class QuestStorage {

    private static final String PATH_QUESTS = "data-storage/MetaAPI/quests.yml";
    private static final File file = new File(PATH_QUESTS);
    private static final FileConfiguration config = YamlConfiguration.loadConfiguration(file);

    // Warning suppressed since the file is created only if it does not exist, hence the file cannot already exist and createNewFile() cannot return false
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void initialise() {
        // Create the quest directories and file if they do not exist
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ConfigurationSection getPlayerConfig(Player player) {
        // Get the Provided Players Configuration Section
        ConfigurationSection playerConfig = config.getConfigurationSection(player.getName());

        //If it does not already exist, create it
        if (playerConfig == null) {
            return config.createSection(player.getName());
        }
        return playerConfig;
    }

    @Nullable
    private static ConfigurationSection getNestedConfiguration(ConfigurationSection nestFrom, String sectionName) {
        // Get the Configuration Section from the Provided One
        ConfigurationSection toReturn = nestFrom.getConfigurationSection(sectionName);

        // If it is null put it in the Logs
        if (toReturn == null) {
            Log.missingKeyError(sectionName, nestFrom, sectionName);
        }
        return toReturn;
    }

    @Nullable
    private static ConfigurationSection getQuestConfig(Player player, Quest quest) {
        //Return the Quest Configuration Section for the Player
        return getNestedConfiguration(getPlayerConfig(player), quest.getId());
    }

    @Nullable
    private static ConfigurationSection getRequirementConfig(Player player, Quest quest) {
        //Return the Quest Requirements Configuration Section for the Player
        return getNestedConfiguration(Objects.requireNonNull(getQuestConfig(player, quest)), "requirements");
    }

    public static List<Quest> getActiveQuests(Player player) {
        //Get Important Variables
        List<Quest> activeQuests = new ArrayList<>();
        ConfigurationSection playerConfig = getPlayerConfig(player);

        // Loop through every quest
        for (String questId : playerConfig.getKeys(false)) {

            // Check A - that the master quest list contains the questID B - that the quest is not complete
            if (Quests.getQuests().containsKey(questId) && !isQuestComplete(player, Quests.getQuests().get(questId))) {

                // If so, add it to the list of active quests
                activeQuests.add(Quests.getQuests().get(questId));
            }
        }

        return activeQuests;
    }

    public static int getProgress(Player player, Quest quest, Requirement requirement) {
        // Find the index at which this requirement is stored within the list of quests, then use that as the key to get the progress of that requirement
        List<Requirement> list = Arrays.asList(quest.getRequirements());
        String key = String.valueOf(list.indexOf(requirement));
        return Objects.requireNonNull(getRequirementConfig(player, quest)).getInt(key);
    }

    public static boolean isRequirementComplete(Player player, Quest quest, Requirement requirement) {
        //Return whether the Players Config has the Given Quest's Requirement Completed
        return getProgress(player, quest, requirement) >= requirement.getThreshold();
    }

    public static boolean isQuestComplete(Player player, Quest quest) {
        //Return whether the Players Config has the Given Quest Completed
        return Objects.requireNonNull(getQuestConfig(player, quest)).getBoolean("complete");
    }

    public static boolean areQuestRequirementsComplete(Player player, Quest quest) {
        // Loop through every quest
        for (Requirement r : quest.getRequirements()) {

            // If any given requirement is incomplete, return false, since all requirements must be complete to mark the quest as complete
            if (!isRequirementComplete(player, quest, r)) {
                return false;
            }
        }

        // If false has not been returned by now, all the requirements must be complete
        return true;
    }

    public static void addQuest(Player player, Quest quest) {
        // Get/create config sections
        ConfigurationSection playerSection = getPlayerConfig(player);

        // Make sure that we haven't already added the quest
        if (playerSection.getConfigurationSection(quest.getId()) != null) {return;}
        ConfigurationSection questSection = playerSection.createSection(quest.getId());
        ConfigurationSection requirementSection = questSection.createSection("requirements");

        // Initialise 'complete' to false
        questSection.set("complete", false);

        // Initialise every requirement to have 0 progress
        for (Requirement requirement : quest.getRequirements()) {
            List<Requirement> list = Arrays.asList(quest.getRequirements());
            String key = String.valueOf(list.indexOf(requirement));
            requirementSection.set(key, 0);
        }

        //Notify the Player they Unlocked a New Quest
        player.sendMessage(TextUtil.getLanguageEntry("quest.unlock_quest", "player", player, "quest", quest.getName()));
    }

    public static void completeQuest(Player player, Quest quest) {
        // Set Complete to be True
        Objects.requireNonNull(getQuestConfig(player, quest)).set("complete", true);

        // Notify the Player they Completed the Quest
        player.sendMessage(TextUtil.getLanguageEntry("quest.complete_quest", "player", player, "quest", quest.getName()));

        // Grant all Rewards the Player should Recieve
        grantQuestRewards(player, quest);

        // Check if any new quests should be Unlocked
        unlockNewQuests(player, quest);
    }

    public static void updateProgress(Player player, Quest quest, Requirement requirement, int progress) {
        // Get a list of requirements, check that the list contains the given requirement, calculate the new progress, then set the new progress
        List<Requirement> list = Arrays.asList(quest.getRequirements());
        String key = String.valueOf(list.indexOf(requirement));
        int newProgress = getProgress(player, quest, requirement) + progress;
        Objects.requireNonNull(getRequirementConfig(player, quest)).set(key, newProgress);

        // Now that a requirement has been updated, check if all requirements are complete
        if (areQuestRequirementsComplete(player, quest)) {
            completeQuest(player, quest);
        }
    }

    public static void unlockNewQuests(Player player, @Nullable Quest limiter) {
        // Check if any new quests should now be active
        for (Quest newQuest : Quests.getQuests().values()) {
            List<String> precursors = Arrays.asList(newQuest.getPrecursors());

            // Check if quest is a precursor to newQuest
            if (limiter != null && !precursors.contains(limiter.getId())) { continue; }
            boolean complete = true;
            for (String precursor : precursors) {
                complete = isQuestComplete(player, Quests.getQuests().get(precursor)) && complete;
            }

            // If all the precursors are complete, add the quest to the list of active quests in the config file
            if (complete) {
                addQuest(player, newQuest);
            }
        }
    }

    public static void grantQuestRewards(Player player, Quest quest) {
        //Get Reward Array
        Reward[] rewards = quest.getRewards();
        //Go through Each Possible Reward
        for (Reward reward : rewards) {
            //Activate Reward Method
            reward.rewardPlayer(player);
        }
    }

    public static void save() {
        try {
            config.save(file);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
