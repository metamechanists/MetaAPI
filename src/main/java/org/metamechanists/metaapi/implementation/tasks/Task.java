package org.metamechanists.metaapi.implementation.tasks;

import lombok.Getter;

@Getter
public class Task {

    private final String id;
    private final String name;
    private final String[] precursors;
    private final Requirement[] requirements;
    private final Reward[] rewards;

    public Task(String id, String name, String[] precursors, Requirement[] requirements, Reward[] rewards) {
        this.id = id;
        this.name = name;
        this.precursors = precursors;
        this.requirements = requirements;
        this.rewards = rewards;
    }
}
