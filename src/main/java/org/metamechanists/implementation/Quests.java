package org.metamechanists.implementation;

import lombok.Getter;

import org.metamechanists.implementation.quests.Quest;

import java.util.HashMap;
import java.util.Map;

public class Quests {

    @Getter
    private static final Map<String, Quest> quests = new HashMap<>();

    public static void addQuest(Quest quest) {
        quests.put(quest.getId(), quest);
    }

    @Getter
    private static Quest rootQuest;
}
