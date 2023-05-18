package de.flxwdns.spigothelper.inventory.items;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.List;

@Getter
public final class Item {

    private final ItemStack itemStack;

    public Item(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public Item(Material material) {
        this.itemStack = new ItemStack(material);
    }

    public Item name(String name) {
        var meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);

        return this;
    }

    public Item lore(List<String> lore) {
        itemStack.setLore(lore);
        return this;
    }

    public Item amount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public Item glow() {
        itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        //itemStack.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1);

        //itemStack.addEnchantment((itemStack.getType() == Material.BOW) ? Enchantment.ARROW_INFINITE : Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        var meta = itemStack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(meta);

        return this;
    }

    public Item enchant(Enchantment enchantment, int level) {
        itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public Item setItemFlag(ItemFlag itemFlag) {
        var meta = itemStack.getItemMeta();
        meta.addItemFlags(itemFlag);
        itemStack.setItemMeta(meta);
        return this;
    }

    public Item setItemFlags(List<ItemFlag> itemFlags) {
        var meta = itemStack.getItemMeta();
        itemFlags.forEach(meta::addItemFlags);
        itemStack.setItemMeta(meta);
        return this;
    }

    public Item setBannerPatterns(List<Pattern> patterns) {
        var meta = (BannerMeta) itemStack.getItemMeta();
        meta.setPatterns(patterns);
        itemStack.setItemMeta(meta);
        return this;
    }
}
