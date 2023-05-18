package de.flxwdns.spigothelper.inventory;

import com.google.common.collect.Multimap;
import de.flxwdns.spigothelper.inventory.items.ClickableItem;
import de.flxwdns.spigothelper.inventory.items.Item;
import dev.dbassett.skullcreator.SkullCreator;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class FilterInventory<T, D extends Enum<D>> extends SingleInventory {

    private final Multimap<D, List<T>> values;
    private final List<D> categories;

    @Getter
    private int currentPage = 1;
    private int possibleAmount = 0;
    private int currentCategory = 0;

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

    private ClickableItem filterItem;

    public FilterInventory(String name, int rows, boolean clickable, Multimap<D, List<T>> values, List<D> categories) {
        super(name, rows, clickable);
        this.categories = categories;

        clear();
        this.possibleAmount = (int) Arrays.stream(getInventory().getContents()).filter(Objects::isNull).count();
        this.values = values;
        defineFilterItem();

        createPage(1);
    }

    private void defineFilterItem() {
        filterItem = new ClickableItem(new Item(Material.SPYGLASS).name("§8» §fFilter").lore(getLoreList()), player -> {
            currentCategory++;
            if(categories.size() < currentCategory) currentCategory = 0;

            defineFilterItem();
            createPage(currentPage);
            player.playSound(player.getLocation(), Sound.BLOCK_PACKED_MUD_PLACE, 1f, 1f);
        });
    }

    public abstract ClickableItem constructItem(T value);

    public void createPage(int id) {
        this.currentPage = id;
        clear();

        setClickableItem(getInventory().getSize() / 9, 2, localLastPage);
        setClickableItem(getInventory().getSize() / 9, 6, localNextPage);
        setClickableItem(getInventory().getSize() / 9, 4, filterItem);

        List<T> tempList = new ArrayList<>();
        values.forEach((category, item) -> {
            if(category == getCurrentCategory() || currentCategory == 0) tempList.addAll(item);
        });

        if(tempList.size() == 0) {
            setItem(3, 4, new Item(SkullCreator.itemFromUrl("https://textures.minecraft.net/texture/3cc470ae2631efdfaf967b369413bc2451cd7a39465da7836a6c7a14e877")).name("§8» §cDie Liste ist leer§8!"));
            return;
        }

        for (T element : tempList.subList(possibleAmount * (currentPage - 1), Math.min(tempList.size(), possibleAmount * (currentPage - 1) + possibleAmount))) {
            addClickableItem(constructItem(element));
        }
    }

    private List<String> getLoreList() {
        List<String> loreList = new ArrayList<>();
        loreList.add("§8- " + ((currentCategory == 0) ? "§x§0§0§b§c§f§d" : "§7") + "Keinen");
        categories.forEach(it -> loreList.add("§8- " + ((it == getCurrentCategory() ? "§x§0§0§b§c§f§d" : "§7") + it.name().toUpperCase().charAt(0) + it.name().toLowerCase().substring(1))));
        loreList.add("");
        loreList.add("§8● §7Linksklick§8, §7um zu Wechseln");
        return loreList;
    }

    private D getCurrentCategory() {
        if(currentCategory == 0) return null;
        return categories.get(currentCategory - 1);
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
        List<T> tempList = new ArrayList<>();
        values.forEach((category, item) -> {
            if(category == getCurrentCategory() || currentCategory == 0) tempList.addAll(item);
        });

        return (int) Math.ceil((double) tempList.size() / possibleAmount);
    }
}
