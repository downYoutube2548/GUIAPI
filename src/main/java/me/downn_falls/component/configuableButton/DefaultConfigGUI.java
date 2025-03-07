package me.downn_falls.component.configuableButton;

import me.downn_falls.GUI;
import me.downn_falls.ItemStackBuilder;
import me.downn_falls.api.InputResult;
import me.downn_falls.component.GuiButton;
import me.downn_falls.component.GuiListPage;
import me.downn_falls.component.GuiTextInput;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class DefaultConfigGUI extends GUI {
    public DefaultConfigGUI(GuiConfigurableButton parent) {
        super("Configure", 5);

        GuiListPage configPanel = new GuiListPage(this, "config_panel", 11, 2, 5, 18, 26);
        configPanel.setNotAvailableButton(new ItemStack(Material.AIR));
        configPanel.setNotAvailableComponent(new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE, 1).setDisplayName("&7---").build());

        Set<GuiTextInput> configs = new HashSet<>();
        for (String key : parent.getKeyTemplates().keySet()) {
            KeyValueTemplate template = parent.getKeyTemplates().get(key);
            GuiTextInput b = new GuiTextInput(this, key, 0);
            b.setDisplayItem(new ItemStackBuilder(template.getDisplay()).addLore("", "&7Current: ", "&e{text}", "", "&eClick to set!").build());
            b.setDefaultInput(template.getDefaultValue());
            b.setText(parent.getValue(key));
            b.setWhenInput((event) -> {
                if (template.test(event.getMessage()))
                    return InputResult.SUCCESS;
                else
                    return InputResult.ERROR;
            });
            configs.add(b);
            configPanel.addComponent(b);
        }

        GuiButton back = new GuiButton(this, "back", 39);
        back.setDisplayItem(new ItemStackBuilder(Material.ARROW, 1).setDisplayName("&eBack").addLore("", "&7Click to go back.").build());
        back.addListener((id, nbt, event) -> parent.getGUI().open((Player) event.getWhoClicked()));

        GuiButton confirm = new GuiButton(this, "confirm", 40);
        confirm.setDisplayItem(new ItemStackBuilder(Material.LIME_STAINED_GLASS, 1).setDisplayName("&aConfirm").addLore("", "&7Click to save current config.").build());
        confirm.addListener((id, nbt, event) -> {
            for (GuiTextInput config : configs) {
                parent.setValue(config.getId(), config.getText());
            }

            parent.getGUI().repaint();
            parent.getGUI().open((Player) event.getWhoClicked());
        });

        addComponent(back);
        addComponent(confirm);
        addComponent(configPanel);
    }
}
