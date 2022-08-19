package org.metamechanists.metaapi.implementation.tasks;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.metamechanists.metaapi.MetaAPI;
import org.metamechanists.metaapi.implementation.Tasks;
import org.metamechanists.metaapi.util.Log;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TaskStorage {

    private static final String PATH_QUESTS = "data-storage/MetaAPI/tasks.yml";
    private static final File file = new File(PATH_QUESTS);
    private static final FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    public static final String SERVER_TASK_KEY = "server";

    // Warning suppressed since the file is created only if it does not exist, hence the file cannot already exist and createNewFile() cannot return false
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void initialize() {
        // Create the task directories and file if they do not exist
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void alertCompleter(String completer, String path) {
        if (MetaAPI.getInstance().getServer().getPlayer(completer) != null) {
            // TODO
        }
    }

    private static ConfigurationSection getCompleterConfig(String completer) {
        // Get the Provided Players Configuration Section
        ConfigurationSection playerConfig = config.getConfigurationSection(completer);

        //If it does not already exist, create it
        if (playerConfig == null) {
            return config.createSection(completer);
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
    private static ConfigurationSection getTaskConfig(String completer, Task task) {
        //Return the Task Configuration Section for the Player
        return getNestedConfiguration(getCompleterConfig(completer), task.getId());
    }

    @Nullable
    private static ConfigurationSection getRequirementConfig(String completer, Task task) {
        //Return the Task Requirements Configuration Section for the Player
        return getNestedConfiguration(Objects.requireNonNull(getTaskConfig(completer, task)), "requirements");
    }

    public static List<Task> getActiveTasks(String completer) {
        //Get Important Variables
        List<Task> activeTasks = new ArrayList<>();
        ConfigurationSection playerConfig = getCompleterConfig(completer);

        // Loop through every task
        for (String taskId : playerConfig.getKeys(false)) {

            // Check A - that the master task list contains the taskID B - that the task is not complete
            if (Tasks.getTasks().containsKey(taskId) && !isTaskComplete(completer, Tasks.getTasks().get(taskId))) {

                // If so, add it to the list of active tasks
                activeTasks.add(Tasks.getTasks().get(taskId));
            }
        }

        return activeTasks;
    }

    public static void checkTask(String completer, Method toCall, Object callFrom, Object...args) {
        for (Task task : TaskStorage.getActiveTasks("server")) {
            for (Requirement requirement : task.getRequirements()) {
                try {
                    toCall.invoke(callFrom, completer, task, requirement, args);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static int getProgress(String completer, Task task, Requirement requirement) {
        // Find the index at which this requirement is stored within the list of tasks, then use that as the key to get the progress of that requirement
        List<Requirement> list = Arrays.asList(task.getRequirements());
        String key = String.valueOf(list.indexOf(requirement));
        return Objects.requireNonNull(getRequirementConfig(completer, task)).getInt(key);
    }

    public static boolean isRequirementComplete(String completer, Task task, Requirement requirement) {
        //Return whether the Players Config has the Given Task's Requirement Completed
        return getProgress(completer, task, requirement) >= requirement.getThreshold();
    }

    public static boolean isTaskComplete(String completer, Task task) {
        //Return whether the Players Config has the Given Task Completed
        return Objects.requireNonNull(getTaskConfig(completer, task)).getBoolean("complete");
    }

    public static boolean areTaskRequirementsComplete(String completer, Task task) {
        // Loop through every task
        for (Requirement r : task.getRequirements()) {

            // If any given requirement is incomplete, return false, since all requirements must be complete to mark the task as complete
            if (!isRequirementComplete(completer, task, r)) {
                return false;
            }
        }

        // If false has not been returned by now, all the requirements must be complete
        return true;
    }

    public static void addTask(String completer, Task task) {
        // Get/create config sections
        ConfigurationSection playerSection = getCompleterConfig(completer);

        // Make sure that we haven't already added the task
        if (playerSection.getConfigurationSection(task.getId()) != null) {return;}
        ConfigurationSection taskSection = playerSection.createSection(task.getId());
        ConfigurationSection requirementSection = taskSection.createSection("requirements");

        // Initialise 'complete' to false
        taskSection.set("complete", false);

        // Initialise every requirement to have 0 progress
        for (Requirement requirement : task.getRequirements()) {
            List<Requirement> list = Arrays.asList(task.getRequirements());
            String key = String.valueOf(list.indexOf(requirement));
            requirementSection.set(key, 0);
        }

        //Notify the Completer they Unlocked a New Task
        alertCompleter(completer, "task.");
    }

    public static void completeTask(String completer, Task task) {
        // Set Complete to be True
        Objects.requireNonNull(getTaskConfig(completer, task)).set("complete", true);

        // Notify the Player they Completed the Task
        alertCompleter(completer, "task.complete_task");

        // Attempt to get the Player from the UUID
        Object object = MetaAPI.getInstance().getServer().getPlayer(UUID.fromString(completer));

        // Check if the object is in fact a Player
        if (object instanceof Player player) {

            // If so, reward the player
            task.getCompleter().grantTaskRewards(player, task);

        } else {
            // Otherwise, reward the server
            task.getCompleter().grantTaskRewards(task);
        }

        // Check if any new tasks should be Unlocked
        unlockNewTasks(completer, task);
    }

    public static void updateProgress(String completer, Task task, Requirement requirement, int progress) {
        // Get a list of requirements, check that the list contains the given requirement, calculate the new progress, then set the new progress
        List<Requirement> list = Arrays.asList(task.getRequirements());
        String key = String.valueOf(list.indexOf(requirement));
        int newProgress = getProgress(completer, task, requirement) + progress;
        Objects.requireNonNull(getRequirementConfig(completer, task)).set(key, newProgress);

        // Now that a requirement has been updated, check if all requirements are complete
        if (areTaskRequirementsComplete(completer, task)) {
            completeTask(completer, task);
        }
    }

    public static void unlockNewTasks(String completer, @Nullable Task limiter) {
        // Check if any new tasks should now be active
        for (Task newTask : Tasks.getTasks().values()) {
            List<String> precursors = Arrays.asList(newTask.getPrecursors());

            // Check if task is a precursor to newTask
            if (limiter != null && !precursors.contains(limiter.getId())) { continue; }
            boolean complete = true;
            for (String precursor : precursors) {
                complete = isTaskComplete(completer, Tasks.getTasks().get(precursor)) && complete;
            }

            // If all the precursors are complete, add the task to the list of active tasks in the config file
            if (complete) {
                addTask(completer, newTask);
            }
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