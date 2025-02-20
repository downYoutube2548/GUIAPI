package me.downn_falls.component;

import de.tr7zw.nbtapi.NBTItem;
import me.downn_falls.*;
import me.downn_falls.api.Clickable;
import me.downn_falls.api.InputResult;
import me.downn_falls.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Function;

public class GuiTextInput extends GuiButton implements Clickable {

    private String text;
    private String editTitle = Utils.colorize("&eInput Text");
    private String editSubTitle = Utils.colorize("&fInput the text in the chat message");
    private String defaultInput;
    private String invalidInputMessage = Utils.colorize("&cInvalid input format!");
    private Function<AsyncPlayerChatEvent, InputResult> whenInput = (e) -> InputResult.SUCCESS;

    public GuiTextInput(GUI gui, String id, int slot) {
        super(gui, id, slot);
    }

    public GuiTextInput setDisplayItem(ItemStack item) {
        this.displayItem = item;
        return this;
    }
    public GuiTextInput setDefaultInput(String s) {
        this.defaultInput = s;
        return this;
    }

    public String getDefaultInput() {
        return this.defaultInput;
    }

    public GuiTextInput setEditTitle(String s) {
        this.editTitle = s;
        return this;
    }
    public GuiTextInput setEditSubTitle(String s) {
        this.editSubTitle = s;
        return this;
    }
    public void setInvalidInputMessage(String s) { this.invalidInputMessage = s; }

    public String getText() {
        return text == null ? defaultInput : text;
    }

    public GuiTextInput setWhenInput(Function<AsyncPlayerChatEvent, InputResult> value) {
        this.whenInput = value;
        return this;
    }

    public GuiTextInput setText(String s) {
        this.text = s;
        return this;
    }

    public GuiTextInput setText(String s, boolean update) {
        this.text = s;
        if (update) getGUI().repaint();
        return this;
    }

    @Override
    public void render(GuiRenderer renderer) {

        ItemStack item = ItemStackBuilder.replaceLore(enable ? displayItem : notEnableButton, "{text}", text == null ? defaultInput == null ? Utils.colorize("&cNone") : defaultInput : text);

        NBTItem nbt = new NBTItem(item);
        nbt.setString("component-id", getFullId());

        renderer.setSlot(0, nbt.getItem());
    }

    @Override
    public void onClick(String componentId, NBTItem nbt, InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            if (enable) {
                if (event.getClick().equals(ClickType.RIGHT)) {
                    setText(defaultInput, true);
                } else {
                    player.closeInventory();
                    GuiListener.GUI_TEXT_INPUT.put(player, this);

                    player.sendTitle(Utils.colorize(editTitle), Utils.colorize(editSubTitle), 10, 5 * 60 * 20, 20);
                }
            }
        }
        super.onClick(componentId, nbt, event);
    }

    public void onInput(AsyncPlayerChatEvent event) {

        InputResult result = whenInput.apply(event);

        Bukkit.getScheduler().runTask(GUIAPI.getInstance(), ()-> {
            if (result.isError()) {
                event.getPlayer().sendMessage(result.getMessage() != null ? result.getMessage() : invalidInputMessage);
            } else {

                setText(result.getMessage() != null ? result.getMessage() : event.getMessage());

                event.getPlayer().openInventory(getGUI().getInventory());
                getGUI().repaint();
                getGUI().revalidate();
                event.getPlayer().resetTitle();

                GuiListener.GUI_TEXT_INPUT.remove(event.getPlayer());
            }
        });
    }
}
