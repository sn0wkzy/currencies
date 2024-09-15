package com.github.sn0wkzy.currencies.common.util.item;

import com.github.sn0wkzy.currencies.common.util.ColorUtil;
import com.google.common.util.concurrent.Atomics;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ItemBuilder {

    private final ItemStack itemStack;

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack.clone();
    }

    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public ItemBuilder(Material material, int data) {
        this(new ItemStack(material, (short) data));
    }

    public ItemBuilder displayName(String displayName) {
        return modifyItemMeta(itemMeta -> itemMeta.setDisplayName(ColorUtil.colored(displayName)));
    }

    public ItemBuilder lore(String... lore) {
        return modifyItemMeta(itemMeta -> itemMeta.setLore(Arrays.asList(ColorUtil.colored(lore))));
    }

    public ItemBuilder lore(List<String> lore) {
        return modifyItemMeta(itemMeta -> itemMeta.setLore(ColorUtil.colored(lore)));
    }

    public ItemBuilder loreWithPlaceholder(List<String> lore, Map<String, String> placeholders) {
        final AtomicReference<List<String>> atomicReference = Atomics.newReference(lore);
        placeholders.forEach((key, placeholder) -> atomicReference.getAndUpdate(get -> get.stream()
                .map(line -> line.replace(key, placeholder).replace("&", "§"))
                .collect(Collectors.toList())));

        return modifyItemMeta(itemMeta -> itemMeta.setLore(atomicReference.get()));
    }

    public ItemBuilder addLore(String... lore) {
        List<String> currentLore = itemStack.getItemMeta().getLore();
        if (currentLore == null) {
            currentLore = new ArrayList<>();
        }

        currentLore.addAll(Arrays.asList(ColorUtil.colored(lore)));

        final List<String> finalLore = currentLore;
        return modifyItemMeta(itemMeta -> itemMeta.setLore(finalLore));
    }

    public ItemBuilder clearLore() {
        return modifyItemMeta(itemMeta -> itemMeta.setLore(new ArrayList<>()));
    }

    public ItemBuilder amount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchant, int level) {
        return modifyItemMeta(itemMeta -> itemMeta.addEnchant(enchant, level, true));
    }

    public ItemBuilder removeEnchantment(Enchantment enchant) {
        return modifyItemMeta(itemMeta -> itemMeta.removeEnchant(enchant));
    }

    public ItemBuilder itemFlags(ItemFlag... flags) {
        return modifyItemMeta(itemMeta -> itemMeta.addItemFlags(flags));
    }

    public ItemBuilder hideAll() {
        return modifyItemMeta(itemMeta -> itemMeta.addItemFlags(ItemFlag.values()));
    }

    public ItemStack build() {
        return itemStack;
    }

    private ItemBuilder modifyItemMeta(Consumer<ItemMeta> consumer) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        consumer.accept(itemMeta);

        itemStack.setItemMeta(itemMeta);
        return this;
    }
}