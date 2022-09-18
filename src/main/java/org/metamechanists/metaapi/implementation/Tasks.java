package org.metamechanists.metaapi.implementation;

import lombok.Getter;
import org.metamechanists.metaapi.implementation.tasks.Task;
import org.metamechanists.metaapi.util.Log;

import java.util.HashMap;
import java.util.Map;

public class Tasks {

    @Getter
    private static final Map<String, Task> tasks = new HashMap<>();

    public static void addTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public static Task getTask(String taskId) {
        //Attempt to get the Task from the Id, if null, warn console
        Task task = tasks.get(taskId);
        if (task == null) {
            Log.warning(taskId + " is not found in Task Map!");
        }
        return task;
    }
}
