package org.metamechanists.metaapi.implementation.tasks;

import lombok.Getter;

@Getter
public record Task(String id, String name, Completer completer, String[] precursors, Requirement[] requirements,
                   Reward[] rewards) {

}
