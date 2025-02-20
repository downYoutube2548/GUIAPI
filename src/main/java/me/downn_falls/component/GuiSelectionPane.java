package me.downn_falls.component;

import de.tr7zw.nbtapi.NBTItem;
import me.downn_falls.GUI;
import me.downn_falls.GuiRenderer;
import me.downn_falls.api.Clickable;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GuiSelectionPane extends GuiComponent implements Clickable {

    private GuiPanel selectionPanel;
    private GuiPanel selectedPanel;
    private int maximumSelect = 0;

    public GuiSelectionPane(GUI gui, String id) {
        super(gui, id, 0, 1, 1);
    }

    public void setMaximumSelect(int value) {
        maximumSelect = value;
    }

    public void setSelectionPanel(GuiPanel component) {
        component.setParent(this);

        component.addListener((id, nbt, event) -> {

            if (nbt.getString("pane-mode").equals("selection")) {

                if (selectedPanel.getComponents().size() < maximumSelect) {

                    component.removeComponent(id);

                    ItemStack item = nbt.getItemStack("real-item");

                    GuiButton button = new GuiButton(getGUI(), UUID.randomUUID().toString(), 0);
                    button.setDisplayItem(item);
                    addSelectedItem(button);

                    getGUI().repaint();

                }
            }

        });

        selectionPanel = component;
    }

    public void setSelectedPanel(GuiPanel component) {

        component.setParent(this);

        component.addListener((id, nbt, event) -> {

            if (nbt.getString("pane-mode").equals("selected")) {
                component.removeComponent(id);

                ItemStack item = nbt.getItemStack("real-item");

                GuiButton button = new GuiButton(getGUI(), UUID.randomUUID().toString(), 0);
                button.setDisplayItem(item);
                selectionPanel.addComponent(button);
            }
        });

        selectedPanel = component;
    }

    @Override
    public void render(GuiRenderer renderer) {

        if (selectionPanel != null) {

            GuiRenderer selectionRenderer = new GuiRenderer(renderer.getInventory(), null, selectionPanel.getSlot(), selectionPanel.getColumn());
            selectionRenderer.addMetadata("pane-mode", "selection");

            selectionPanel.render(selectionRenderer);
        }

        if (selectedPanel != null) {

            GuiRenderer selectedRenderer = new GuiRenderer(renderer.getInventory(), null, selectedPanel.getSlot(), selectedPanel.getColumn());
            selectedRenderer.addMetadata("pane-mode", "selected");

            selectedPanel.render(selectedRenderer);
        }
    }

    @Override
    public void onClick(String componentId, NBTItem nbt, InventoryClickEvent event) {

        selectionPanel.onClick(componentId, nbt, event);
        selectedPanel.onClick(componentId, nbt, event);

    }

    public void addSelectedItem(GuiButton button) {
        selectedPanel.addComponent(button);
        getGUI().repaint();
    }

    public List<ItemStack> getSelectedItems() {

        List<ItemStack> items = new ArrayList<>();
        for (GuiComponent component : selectedPanel.getComponents().values()) {
            if (component instanceof GuiButton button) {

                ItemStack item = button.getDisplayItem();
                NBTItem nbtItem = new NBTItem(item);

                items.add(nbtItem.getItemStack("real-item"));
            }
        }
        return items;

    }
}
