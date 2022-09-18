package org.metamechanists.metaapi.implementation.tasks;

import lombok.Getter;
import org.metamechanists.metaapi.implementation.Tasks;

import java.util.HashSet;
import java.util.Set;

@Getter
public class Task {

    private final String id;
    private final String name;
    private final Completer completer;
    private final String[] precursors;
    private final Set<String> children = new HashSet<>();
    private final Requirement[] requirements;
    private final Reward[] rewards;

    public Task(String id, String name, Completer completer, String[] precursors, Requirement[] requirements, Reward[] rewards) {
        this.id = id;
        this.name = name;
        this.completer = completer;
        this.precursors = precursors;
        this.requirements = requirements;
        this.rewards = rewards;
    }

    public void add() {
        //Put this after **All** logic
        Tasks.addTask(this);
    }

    public void addChild(String id) {
        children.add(id);
    }
}