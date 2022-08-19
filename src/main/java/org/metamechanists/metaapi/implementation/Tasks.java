package org.metamechanists.metaapi.implementation;

import lombok.Getter;

import lombok.Setter;
import org.metamechanists.metaapi.implementation.tasks.Task;

import java.util.HashMap;
import java.util.Map;

public class Tasks {

    @Getter
    private static final Map<String, Task> tasks = new HashMap<>();

    public static void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Getter
    @Setter
    private static Task rootTask;
}
