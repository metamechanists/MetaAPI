package org.metamechanists.metaapi.listeners.tasks;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.metaapi.implementation.tasks.Requirement;
import org.metamechanists.metaapi.implementation.tasks.Task;
import org.metamechanists.metaapi.implementation.tasks.TaskStorage;
import org.metamechanists.metaapi.implementation.tasks.requirements.Craft;

import java.util.HashMap;
import java.util.Map;

public class OnMachineCraft implements Listener {

    private static Map<String, Integer> machineInventories = new HashMap<>();
    private static Map<Integer, Integer> machineOutputs = new HashMap<>();

    public static void fillMap() {
        machineInventories.put("Example", 0);
        machineOutputs.put(0, 19);
    }

    public static void checkRequirement(String completer, Task task, Requirement requirement, ItemStack result) {

        // Check if the requirement is relevant to this listener
        if (requirement instanceof Craft craft) {

            //Get the Requirement Stack to Check With
            ItemStack requirementItem = craft.getItem().clone();
            requirementItem.setAmount(1);

            //Get the Result Stack to Check With
            ItemStack resultItem = result.clone();
            resultItem.setAmount(1);

            //Is the Required Item a Slimefun Item
            boolean isRequirementSlimefunItem = (SlimefunItem.getByItem(resultItem) != null);
            //If Both are Vanilla Items and Equal
            boolean vanillaPassed = (!isRequirementSlimefunItem && requirementItem == resultItem);
            //If Both are Slimefun Items and Equal
            boolean slimefunPassed = isRequirementSlimefunItem && SlimefunItem.getByItem(resultItem) == SlimefunItem.getByItem(requirementItem);

            if (vanillaPassed || slimefunPassed) {

                // Increment the objective
                TaskStorage.updateProgress(completer, task, requirement, result.getAmount());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e) throws NoSuchMethodException {
        HumanEntity humanEntity = e.getWhoClicked();
        Inventory inventory = e.getClickedInventory();
        InventoryView inventoryView = e.getView();

        if (!(humanEntity instanceof Player player && inventory != null)) {
            return;
        }

        int slot = e.getSlot();
        String uuid = player.getUniqueId().toString();
        String title = inventoryView.getTitle();

        if (!(machineInventories.containsKey(title) && machineInventories.get(title) == slot)) {
            return;
        }

        // Check player task
        TaskStorage.checkTask(uuid, this.getClass().getMethod(
                "checkRequirement", String.class, Task.class, Requirement.class, ItemStack.class), this, inventory.getItem(machineOutputs.get(machineInventories.get(title))));
    }
}
