package me.downn_falls.component;

import me.downn_falls.GUI;
import me.downn_falls.GuiRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class GuiComponent {
    private final String id;
    private GuiComponent parent;
    private int slot;
    private final int row;
    private final int column;
    private final GUI gui;

    public GuiComponent(GUI gui, String id, int slot, int row, int column) {
        this.gui = gui;
        this.id = id;
        this.slot = slot;
        this.row = row;
        this.column = column;
    }

    public String getId() {
        return id;
    }

    public List<GuiComponent> getParents() {
        List<GuiComponent> output = new ArrayList<>();
        GuiComponent component = this;
        while (component != null) {
            output.add(component);
            component = component.getParent();
        }
        Collections.reverse(output);
        return output;
    }

    public String getFullId() {
        return getParents().stream().map(GuiComponent::getId).collect(Collectors.joining("."));
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int value) {
        slot = value;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setParent(GuiComponent parent) {
        this.parent = parent;
    }

    public GuiComponent getParent() {
        return parent;
    }

    public GUI getGUI() {
        return gui;
    }

    public abstract void render(GuiRenderer renderer);
}
