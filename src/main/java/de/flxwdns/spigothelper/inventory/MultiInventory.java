package de.flxwdns.spigothelper.inventory;

import de.flxwdns.spigothelper.inventory.items.ClickableItem;
import de.flxwdns.spigothelper.inventory.items.Item;
import dev.dbassett.skullcreator.SkullCreator;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class MultiInventory<T> extends SingleInventory {

    private final List<T> values;

    @Getter
    private int currentPage = 1;
    private int possibleAmount = 0;
    @Getter
    private final int currentFilter = 0;

    private final ClickableItem localLastPage = new ClickableItem(new Item(SkullCreator.itemFromUrl("https://textures.minecraft.net/texture/bd69e06e5dadfd84e5f3d1c21063f2553b2fa945ee1d4d7152fdc5425bc12a9")).name("§8» §fLetzte Seite"), player -> {
        if(currentPage > 1) {
            createPage(--currentPage);
            player.playSound(player.getLocation(), Sound.BLOCK_PACKED_MUD_PLACE, 1f, 1f);
        } else {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
        }
    });
    private final ClickableItem localNextPage = new ClickableItem(new Item(SkullCreator.itemFromUrl("https://textures.minecraft.net/texture/19bf3292e126a105b54eba713aa1b152d541a1d8938829c56364d178ed22bf")).name("§8» §fNächste Seite"), player -> {
        if(currentPage != getMaxPage() && getMaxPage() > 1) {
            createPage(++currentPage);
            player.playSound(player.getLocation(), Sound.BLOCK_PACKED_MUD_PLACE, 1f, 1f);
        } else {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
        }
    });

    public MultiInventory(String name, int rows, boolean clickable, List<T> values) {
        super(name, rows, clickable);

        clear();
        this.possibleAmount = (int) Arrays.stream(getInventory().getContents()).filter(Objects::isNull).count();
        this.values = values;

        createPage(1);
    }

    public abstract ClickableItem constructItem(T value);

    public void createPage(int id) {
        this.currentPage = id;
        clear();

        setClickableItem(getInventory().getSize() / 9, 2, localLastPage);
        setClickableItem(getInventory().getSize() / 9, 6, localNextPage);

        if(values.size() == 0) {
            setItem(3, 4, new Item(SkullCreator.itemFromUrl("https://textures.minecraft.net/texture/3cc470ae2631efdfaf967b369413bc2451cd7a39465da7836a6c7a14e877")).name("§8» §cDie Liste ist leer§8!"));
        }

        for (T element : values.subList(possibleAmount * (currentPage - 1), Math.min(values.size(), possibleAmount * (currentPage - 1) + possibleAmount))) {
            addClickableItem(constructItem(element));
        }
    }

    public void clear() {
        getItems().clear();

        for (int i = 0; i < 7; i++) setItem(2, i + 1, new Item(Material.AIR));
        for (int i = 0; i < 7; i++) setItem(3, i + 1, new Item(Material.AIR));
        for (int i = 0; i < 7; i++) setItem(4, i + 1, new Item(Material.AIR));
        for (int i = 0; i < 7; i++) setItem(5, i + 1, new Item(Material.AIR));
    }

    public int getSlot() {
        return getInventory().getSize() - 18;
    }

    public int getMaxPage() {
        return (int) Math.ceil((double) values.size() / possibleAmount);
    }
}
