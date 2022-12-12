/*
 * Copyright (C) 2022 Idra - All Rights Reserved
 */

package org.metamechanists.metaapi.implementation;

import lombok.Getter;
import org.metamechanists.metaapi.implementation.tasks.TaskLine;
import org.metamechanists.metaapi.util.Log;

import java.util.HashMap;
import java.util.Map;

public class TaskLines {

    @Getter
    private static final Map<String, TaskLine> taskLines = new HashMap<>();

    public static void addTaskLine(TaskLine taskLine) {
        if (!taskLines.containsKey(taskLine.getId())) {
            taskLines.put(taskLine.getId(), taskLine);
        }
    }

    public static TaskLine getTaskLine(String taskLineId) {
        //Attempt to get the Task from the Id, if null, warn console
        TaskLine taskLine = taskLines.get(taskLineId);
        if (taskLine == null) {
            Log.warning(taskLineId + " is not found in TaskLine Map!");
        }
        return taskLine;
    }
}
