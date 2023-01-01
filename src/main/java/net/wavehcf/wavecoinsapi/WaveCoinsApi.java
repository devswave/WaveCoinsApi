package net.wavehcf.wavecoinsapi;

import org.bukkit.plugin.java.JavaPlugin;

public class WaveCoinsApi extends JavaPlugin {

    WaveCoins economy;
    WaveDatabase database;
    @Override
    public void onEnable() {
        
        economy = new WaveCoins();
        database = new WaveDatabase();

    }
}
