package org.metamechanists.implementation.quests.requirements;

import lombok.Getter;
import org.metamechanists.implementation.quests.Requirement;

@Getter
public class ExecuteCommand extends Requirement {

    private final String command;

    public ExecuteCommand(int threshold, String command) {
        super(threshold);
        this.command = command;
    }
}
