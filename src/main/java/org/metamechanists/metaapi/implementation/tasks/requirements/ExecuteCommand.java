package org.metamechanists.metaapi.implementation.tasks.requirements;

import lombok.Getter;
import org.metamechanists.metaapi.implementation.tasks.Requirement;

@Getter
public class ExecuteCommand extends Requirement {

    private final String command;

    public ExecuteCommand(int threshold, String command) {
        super(threshold);
        this.command = command;
    }
}
