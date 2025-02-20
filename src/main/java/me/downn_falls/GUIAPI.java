package me.downn_falls;

import org.bukkit.plugin.java.JavaPlugin;

public final class GUIAPI extends JavaPlugin {

    private static GUIAPI plugin;

    @Override
    public void onEnable() {
        plugin = this;
        getServer().getPluginManager().registerEvents(new GuiListener(), this);
    }

    public static GUIAPI getInstance() {
        return plugin;
    }
}
