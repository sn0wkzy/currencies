package com.github.sn0wkzy.currencies.spigot.inventory;

import com.github.sn0wkzy.currencies.common.controller.LeadboardController;
import com.github.sn0wkzy.currencies.common.model.currency.Currency;
import com.github.sn0wkzy.currencies.common.model.leaderboard.LeadboardUser;
import com.github.sn0wkzy.currencies.common.util.NumberFormatter;
import com.github.sn0wkzy.currencies.common.util.item.ItemBuilder;
import me.saiintbrisson.minecraft.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class CurrencyTopInventory extends PaginatedView<LeadboardUser> {

    private final LeadboardController leadboardController;

    public CurrencyTopInventory(LeadboardController leadboardController) {
        super(5, "");

        this.leadboardController = leadboardController;

        setSource(context -> {
            final Currency currency = context.get("currency");
            return leadboardController.getLeadboard(currency).getLeadboardUsers();
        });
        setLayout("XXXXXXXXX",
                "XOOOOOOOX",
                "XXXOOOXXX",
                "XXXXXXXXX",
                "XXXXXXXXX");

        setCancelOnClick(true);
        setCancelOnDrop(true);
        setCancelOnPickup(true);
        setCancelOnDrag(true);
    }

    @Override
    protected void onOpen(@NotNull OpenViewContext context) {
        final Currency currency = context.get("currency");
        context.setContainerTitle("Ranking de " + currency.getPlural());
    }

    @Override
    protected void onRender(@NotNull ViewContext context) {
        context.slot(40).onRender(render -> render.setItem(getCloseIcon()))
                .onClick(onCloseClick());
    }

    @Override
    protected void onItemRender(@NotNull PaginatedViewSlotContext<LeadboardUser> paginatedViewSlotContext, @NotNull ViewItem viewItem, @NotNull LeadboardUser leadboardUser) {
        viewItem.onRender(render -> render.setItem(getLeadboardUserIcon(leadboardUser)));
    }

    private ItemStack getCloseIcon() {
        return new ItemBuilder(Material.ARROW)
                .displayName("§cFechar")
                .build();
    }

    private ItemStack getLeadboardUserIcon(LeadboardUser leadboardUser) {
        return new ItemBuilder(new ItemStack(Material.SKULL_ITEM, 1, (short) 0, (byte) 3))
                .displayName(leadboardUser.getDisplayName())
                .addLore("§fQuantia: §7" + NumberFormatter.formatWithSuffix(leadboardUser.getAmount()))
                .build();
    }

    private Consumer<ViewSlotClickContext> onCloseClick() {
        return context -> {
            context.getData().clear();
            context.close();
        };
    }
}
