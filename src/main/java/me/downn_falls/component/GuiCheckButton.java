package me.downn_falls.component;

import de.tr7zw.nbtapi.NBTItem;
import me.downn_falls.GUI;
import me.downn_falls.GuiRenderer;
import me.downn_falls.ItemStackBuilder;
import me.downn_falls.utils.Utils;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GuiCheckButton extends GuiButton {

    private boolean check = false;

    private String enableFormat = Utils.colorize("&aTrue");
    private String disableFormat = Utils.colorize("&cFalse");

    public GuiCheckButton(GUI gui, String id, int slot) {
        super(gui, id, slot);
    }

    public boolean isCheck() { return check; }
    public void setCheck(boolean b) { this.check = b; }

    public void setEnableFormat(String s) {
        this.enableFormat = Utils.colorize(s);
    }

    public void setDisableFormat(String s) {
        this.disableFormat = Utils.colorize(s);
    }

    @Override
    public void render(GuiRenderer renderer) {

        ItemStack item = ItemStackBuilder.replaceLore(displayItem, "{value}", check ? enableFormat : disableFormat);

        NBTItem nbt = new NBTItem(enable ? item : notEnableButton);
        nbt.setString("component-id", getFullId());

        renderer.setSlot(0, nbt.getItem());
    }

    @Override
    public void onClick(String componentId, NBTItem nbt, InventoryClickEvent event) {
        if (enable) {
            setCheck(!check);
            getGUI().repaint();
        }
        super.onClick(componentId, nbt, event);
    }
}
