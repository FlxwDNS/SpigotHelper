package de.flxwdns.spigothelper;

import de.flxwdns.spigothelper.injection.InjectionLayer;
import de.flxwdns.spigothelper.inventory.service.InventoryImpl;
import de.flxwdns.spigothelper.inventory.service.InventoryService;
import de.flxwdns.spigothelper.sql.SQLImpl;
import de.flxwdns.spigothelper.sql.SQLService;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class SpigotHelper extends JavaPlugin {
    @Getter
    private static SpigotHelper instance;

    @Override
    public void onEnable() {
        instance = this;

        InjectionLayer.register(SQLService.class, new SQLImpl());
        InjectionLayer.register(InventoryService.class, new InventoryImpl());
    }
}
