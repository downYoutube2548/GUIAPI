package me.downn_falls;

import de.tr7zw.nbtapi.NBTItem;
import me.downn_falls.api.Clickable;
import me.downn_falls.api.Editable;
import me.downn_falls.component.GuiComponent;
import me.downn_falls.component.GuiItemChooser;
import me.downn_falls.component.GuiListTextInput;
import me.downn_falls.component.GuiTextInput;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;

public class GuiListener implements Listener {

    public static final HashMap<Player, GuiTextInput> GUI_TEXT_INPUT = new HashMap<>();
    public static final HashMap<Player, GuiListTextInput> GUI_LIST_TEXT_INPUT = new HashMap<>();
    public static final HashMap<Player, GuiItemChooser> GUI_CHOOSE_ITEM = new HashMap<>();


    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof GuiInventoryHolder inventoryHolder) {

            if (GUI.guis.containsKey(inventoryHolder.getInventoryUUID())) {

                if (event.getClickedInventory() != null && event.getClickedInventory().getType().equals(InventoryType.CHEST)) {
                    GUI gui = GUI.guis.get(inventoryHolder.getInventoryUUID());

                    if (event.getCurrentItem() != null) {
                        NBTItem nbt = new NBTItem(event.getCurrentItem());

                        String rawComponentId = nbt.getString("component-id");

                        String[] componentIDs = rawComponentId.split("\\.");

                        if (gui.getComponents().containsKey(componentIDs[0])) {
                            GuiComponent component = gui.getComponents().get(componentIDs[0]);
                            if (!(component instanceof Editable)) event.setCancelled(true);
                            if (component instanceof Clickable clickable) {
                                //String newComponentId = componentIDs.length > 1 ? rawComponentId.substring(componentIDs[0].length()+1) : rawComponentId;

                                clickable.onClick(rawComponentId, nbt, event);
                            }
                        } else {
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onItemChoose(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            if (GUI_CHOOSE_ITEM.containsKey(player)) {
                if (event.getCurrentItem() != null && !event.getCurrentItem().getType().equals(Material.AIR)) {
                    event.setCancelled(true);

                    GuiItemChooser itemChooser = GUI_CHOOSE_ITEM.get(player);

                    itemChooser.onChoose(event);
                }
            }
        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof GuiInventoryHolder) {
            GUI.guis.entrySet().removeIf(entry -> entry.getValue().getInventory() != null && entry.getValue().getInventory().getViewers().isEmpty());
        }
    }

    @EventHandler
    public void onInputText(AsyncPlayerChatEvent event) {
        if (GUI_TEXT_INPUT.containsKey(event.getPlayer())) {
            event.setCancelled(true);

            GuiTextInput textInput = GUI_TEXT_INPUT.get(event.getPlayer());

            textInput.onInput(event);
        }

        if (GUI_LIST_TEXT_INPUT.containsKey(event.getPlayer())) {
            event.setCancelled(true);

            GuiListTextInput listTextInput = GUI_LIST_TEXT_INPUT.get(event.getPlayer());

            listTextInput.onInput(event);
        }
    }
}
