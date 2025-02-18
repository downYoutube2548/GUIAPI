package me.downn_falls;

import me.downn_falls.component.GuiComponent;
import me.downn_falls.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class GUI {

    public static HashMap<UUID, GUI> guis = new HashMap<>();
    private final String title;
    private final int size;
    private final LinkedHashMap<String, GuiComponent> components = new LinkedHashMap<>();
    private Inventory inventory;
    private final UUID inventoryUUID = UUID.randomUUID();

    public GUI(String title, int rows) {
        this.title = Utils.colorize(title);
        this.size = 9 * rows;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void revalidate() {
        guis.put(inventoryUUID, this);
    }

    public void repaint() {
        if (inventory != null) {
            for (GuiComponent component : getComponents().values()) {
                component.render(new GuiRenderer(inventory, null, component.getSlot(), component.getColumn()));
            }
        }
    }

    public UUID getUUID() {
        return inventoryUUID;
    }

    public void addComponent(GuiComponent component) {
        components.put(component.getId(), component);
    }

    public Map<String, GuiComponent> getComponents() { return this.components; }

    public void open(Player player) {

        Inventory inventory = Bukkit.createInventory(new GuiInventoryHolder(inventoryUUID), size, title);

        for (GuiComponent component : getComponents().values()) {
            component.render(new GuiRenderer(inventory, null, component.getSlot(), component.getColumn()));
        }

        this.inventory = inventory;

        player.openInventory(inventory);
        guis.put(inventoryUUID, this);
    }

}
