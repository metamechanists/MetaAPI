package org.metamechanists.implementation;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;

import java.util.HashMap;
import java.util.Map;

public class RegisteredItems {

    public static final Map<String, SlimefunItem> items = new HashMap<>();

    public static SlimefunItem getById(String id) {
        return items.get(id);
    }
}
