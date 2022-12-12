/*
 * Copyright (C) 2022 Idra - All Rights Reserved
 */

package org.metamechanists.metaapi.implementation.tasks;

import lombok.Getter;
import org.metamechanists.metaapi.implementation.Tasks;

import java.util.List;

@Getter
public class TaskLine {

    private final String id;
    private final String name;
    private final List<String> tasks;
    private final Task root;

    //I thought the problem about using the final ones was if a taskline has no FINAL task, like if there are multiple

    public TaskLine(String id, String name, List<String> tasks, Task root) {
        this.id = id;
        this.name = name;
        this.tasks = tasks;
        this.root = root;

        // Up to this point, only the precursors have been set
        // Now, do the reverse, and set the children
        for (String taskId : tasks) {
            Task task = Tasks.getTask(taskId);
            for (String precursorId : task.getPrecursors()) {
                Tasks.getTask(precursorId).addChild(id);
            }
        }
    }
        // indie smells - 7

    private void taskLineIteration(Task parentTask, int recursion) {
        if (parentTask.getChildren().size() == 0) {
            //Do something as the final task
        } else {
            for (String childId : parentTask.getChildren()) {
                //Get the task from TaskStorage
                Task childTask = Tasks.getTask(childId);

                //If the task doesn't exist then continue through the loop
                if (childTask == null) {
                    continue;
                }

                //Do something with the task

                if (childTask.getChildren().size() > 0) {
                    //Do something with all its children
                    taskLineIteration(childTask, recursion+1);
                }
            }
        }
    }
}
