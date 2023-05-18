package de.flxwdns.spigothelper.inventory.items;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public final class ClickableItem {

    @Getter
    private final Item item;
    private final Consumer<Player> onClick;
    private final Consumer<Player> onRightClick;

    public ClickableItem(Item item, Consumer<Player> onClick) {
        this.item = item;
        this.onClick = onClick;
        this.onRightClick = player -> {};
    }

    public ClickableItem(Item item, Consumer<Player> onLeftClick, Consumer<Player> onRightClick) {
        this.item = item;
        this.onClick = onLeftClick;
        this.onRightClick = onRightClick;
    }

    public void click(Player player) {
        onClick.accept(player);
    }

    public void clickRight(Player player) {
        onRightClick.accept(player);
    }
}
