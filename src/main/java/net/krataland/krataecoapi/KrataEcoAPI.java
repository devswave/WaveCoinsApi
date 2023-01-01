package net.krataland.krataecoapi;

import org.bukkit.plugin.java.JavaPlugin;

public class KrataEcoAPI extends JavaPlugin {

    KrataEconomy economy;
    KrataDatabase database;
    @Override
    public void onEnable() {
        
        economy = new KrataEconomy();
        database = new KrataDatabase();

    }
}
