package me.downn_falls.component;

import de.tr7zw.nbtapi.NBTItem;
import me.downn_falls.GUI;
import me.downn_falls.GuiRenderer;
import me.downn_falls.TriConsumer;
import me.downn_falls.api.Clickable;
import me.downn_falls.api.Editable;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GuiPanel extends GuiComponent implements Clickable {

    private final LinkedHashMap<String, GuiComponent> components = new LinkedHashMap<>();

    private final List<TriConsumer<String, NBTItem, InventoryClickEvent>> listeners = new ArrayList<>();

    public GuiPanel(GUI gui, String id, int slot, int row, int column) {
        super(gui, id, slot, row, column);
    }

    public void addComponent(GuiComponent component) {
        component.setParent(this);
        components.put(getFullId()+"."+component.getId(), component);
    }

    public void removeComponent(String componentId) {
        components.keySet().removeIf(componentId::endsWith);
    }

    public Map<String, GuiComponent> getComponents() { return this.components; }

    public void addListener(TriConsumer<String, NBTItem, InventoryClickEvent> listener) {
        listeners.add(listener);
    }

    @Override
    public void render(GuiRenderer renderer) {
        for (GuiComponent component : components.values()) {
            component.render(new GuiRenderer(renderer.getInventory(), renderer, component.getSlot(), component.getColumn()));
        }
    }

    @Override
    public void onClick(String componentId, NBTItem nbt, InventoryClickEvent event) {

        for (TriConsumer<String, NBTItem, InventoryClickEvent> listener : listeners) {
            listener.accept(componentId, nbt, event);
        }

        if (getComponents().containsKey(componentId)) {
            GuiComponent component = getComponents().get(componentId);
            if (!(component instanceof Editable)) event.setCancelled(true);
            if (component instanceof Clickable clickable) {
                clickable.onClick(componentId, nbt, event);
            }
        }
    }
}
