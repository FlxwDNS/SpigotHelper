package de.flxwdns.spigothelper.inventory;

import de.flxwdns.spigothelper.SpigotHelper;
import de.flxwdns.spigothelper.inventory.items.ClickableItem;
import de.flxwdns.spigothelper.inventory.items.Item;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class SingleInventory implements Listener {

    @Getter
    private final Inventory inventory;
    @Getter
    private final List<ClickableItem> items;
    private final Boolean clickable;

    public SingleInventory(String name, int rows, boolean clickable) {
        this.inventory = Bukkit.createInventory(null, rows * 9, Component.text("§7#" + new Random().nextInt(9999) + " §8| §7" + name));
        this.items = new ArrayList<>();
        this.clickable = clickable;

        setPlaceHolder(1);
        setPlaceHolder(rows);

        for (int i = 1; i < rows; i++) {
            setPlaceHolder(i, 0);
            setPlaceHolder(i, 8);
        }

        SpigotHelper.getInstance().getServer().getPluginManager().registerEvents(this, SpigotHelper.getInstance());
    }

    public void setItem(int slot, Item item) {
        inventory.setItem(slot, item.getItemStack());
    }

    public void setItem(int row, int slot, Item item) {
        inventory.setItem(((row - 1) * 9) + slot, item.getItemStack());
    }

    public void addItem(Item item) {
        inventory.addItem(item.getItemStack());
    }

    public void open(Player player) {
        if(!(player.getOpenInventory().getType().equals(InventoryType.CHEST))) player.playSound(player.getLocation(), Sound.BLOCK_BARREL_OPEN, 1f, 1f);
        player.openInventory(inventory);
    }

    public void setClickableItem(int slot, ClickableItem clickableItem) {
        items.add(clickableItem);
        inventory.setItem(slot, clickableItem.getItem().getItemStack());
    }
    public void setClickableItem(int row, int slot, ClickableItem clickableItem) {
        items.add(clickableItem);
        inventory.setItem(((row - 1) * 9) + slot, clickableItem.getItem().getItemStack());
    }

    public void addClickableItem(ClickableItem clickableItem) {
        items.add(clickableItem);
        inventory.addItem(clickableItem.getItem().getItemStack());
    }

    public void setPlaceHolder(int row) {
        for (int i = 0; i < 9; i++) setPlaceHolder(row, i);
    }

    public void setPlaceHolder(int row, int slot) {
        setItem(row, slot, new Item(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)).name("§7 "));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!event.getInventory().equals(inventory)) return;
        if(event.getCurrentItem() == null || !(event.getWhoClicked() instanceof Player)) return;
        event.setCancelled(!clickable);

        items.stream().filter(item -> item.getItem().getItemStack().equals(event.getCurrentItem())).findFirst().ifPresent(item -> {
            if(event.isLeftClick()) {
                item.click((Player) event.getWhoClicked());
            } else {
                item.clickRight((Player) event.getWhoClicked());
            }
        });
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(event.getInventory().equals(getInventory())) getItems().clear();
    }
}
