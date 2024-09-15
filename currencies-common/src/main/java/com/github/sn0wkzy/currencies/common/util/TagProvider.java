package com.github.sn0wkzy.currencies.common.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

@UtilityClass
public final class TagProvider {

    private static final LuckPerms PROVIDER = LuckPermsProvider.get();

    private final Cache<UUID, String> PLAYER_PREFIXES_BY_UUID_MAP = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .build();

    private final Cache<String , String> PLAYER_PREFIXES_BY_NAME_MAP = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .build();

    public String getTagById(String id) {
        final Group group = PROVIDER.getGroupManager().getGroup(id);
        if (group == null) {
            return "";
        }

        final String prefix = getCachedMetadata(group).getPrefix();
        return prefix == null ? "" : translateAlternateColorCodes('&', prefix);
    }

    public String getTagWithName(Player player) {
        final CachedMetaData metaData = getCachedMetadata(player.getName());
        return metaData != null && metaData.getPrefix() != null
                ? translateAlternateColorCodes('&', metaData.getPrefix()) + player.getName()
                : "§7";
    }

    public String getTagWithName(OfflinePlayer offlinePlayer) {
        return getOfflineTagWithName(offlinePlayer);
    }

    public String getTag(Player player) {
        final CachedMetaData cachedMetadata = getCachedMetadata(player.getName());
        return cachedMetadata != null && cachedMetadata.getPrefix() != null
                ? translateAlternateColorCodes(
                '&', cachedMetadata.getPrefix().replace("[", "").replace("]", ""))
                : "§7Membro";
    }

    public String getPlayerNameById(UUID playerId) {
        try {
            final UserManager userManager = PROVIDER.getUserManager();
            return userManager.lookupUsername(playerId).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SneakyThrows
    public String getOfflineTagWithName(OfflinePlayer offlinePlayer) {
        final UUID uniqueId = offlinePlayer.getUniqueId();
        final UserManager userManager = PROVIDER.getUserManager();

        if (userManager.isLoaded(uniqueId)) {
            final User user = userManager.getUser(uniqueId);
            if (user == null) {
                return "§7";
            }

            final CachedMetaData cachedMetadata = user.getCachedData().getMetaData();

            return (cachedMetadata.getPrefix() != null
                    ? translateAlternateColorCodes(
                    '&', cachedMetadata.getPrefix())
                    : "§7") + user.getUsername();
        }

        final String cachedPrefix = PLAYER_PREFIXES_BY_UUID_MAP.getIfPresent(uniqueId);
        if (cachedPrefix != null) {
            return cachedPrefix;
        }

        final User user = userManager.loadUser(uniqueId).get();
        if (user == null) {
            return "§7" + offlinePlayer.getName();
        }

        final CachedDataManager cachedData = user.getCachedData();
        final CachedMetaData metaData = cachedData.getMetaData();
        final String prefix = metaData.getPrefix() != null
                ? translateAlternateColorCodes('&', metaData.getPrefix()) + user.getUsername()
                : "§7" + user.getUsername();

        PLAYER_PREFIXES_BY_UUID_MAP.put(uniqueId, prefix);
        return prefix;
    }

    @SneakyThrows
    public String getOfflineTagWithName(UUID uuid) {
        final UserManager userManager = PROVIDER.getUserManager();
        final User user = userManager.getUser(uuid);

        if (user != null) {
            final CachedMetaData cachedMetadata = user.getCachedData().getMetaData();

            return (cachedMetadata.getPrefix() != null
                    ? translateAlternateColorCodes(
                    '&', cachedMetadata.getPrefix())
                    : "§7") + user.getUsername();
        }

        final String cachedPrefix = PLAYER_PREFIXES_BY_UUID_MAP.getIfPresent(uuid);
        if (cachedPrefix != null) {
            return cachedPrefix;
        }

        final User fetchedUser = userManager.loadUser(uuid).get();
        final String playerName = userManager.lookupUsername(uuid).get();

        if (playerName == null) {
            return "§7Desconhecido";
        }

        final CachedDataManager cachedData = fetchedUser.getCachedData();
        final CachedMetaData metaData = cachedData.getMetaData();
        final String prefix = metaData.getPrefix() != null
                ? translateAlternateColorCodes('&', metaData.getPrefix()) + playerName
                : "§7" + playerName;

        PLAYER_PREFIXES_BY_UUID_MAP.put(uuid, prefix);
        return prefix;
    }

    @SneakyThrows
    public String getOfflineTagWithName(String name) {
        final UserManager userManager = PROVIDER.getUserManager();
        final User user = userManager.getUser(name);

        if (user != null) {
            final CachedMetaData cachedMetadata = user.getCachedData().getMetaData();

            return (cachedMetadata.getPrefix() != null
                    ? translateAlternateColorCodes(
                    '&', cachedMetadata.getPrefix())
                    : "§7") + name;
        }

        final String cachedPrefix = PLAYER_PREFIXES_BY_NAME_MAP.getIfPresent(name);
        if (cachedPrefix != null) {
            return cachedPrefix;
        }

        final UUID uuid = userManager.lookupUniqueId(name).get();
        final User fetchedUser = userManager.loadUser(uuid).get();

        if (fetchedUser == null) {
            return "§7" + name;
        }

        final CachedDataManager cachedData = fetchedUser.getCachedData();
        final CachedMetaData metaData = cachedData.getMetaData();
        final String prefix = metaData.getPrefix() != null
                ? translateAlternateColorCodes('&', metaData.getPrefix()) + name
                : "§7" + name;

        PLAYER_PREFIXES_BY_NAME_MAP.put(name, prefix);
        return prefix;
    }

    public String getTagColor(String tag) {
        String tagById = getTagById(tag);
        return tagById.isEmpty() ? "§7" : tagById.substring(0, 2);
    }

    public String getTagColor(OfflinePlayer player) {
        return getOfflineTagWithName(player).substring(0, 2);
    }

    public String getTagColorWithName(OfflinePlayer player) {
        return getTagColor(player) + player.getName();
    }

    private CachedMetaData getCachedMetadata(Group group) {
        return group.getCachedData().getMetaData();
    }

    private CachedMetaData getCachedMetadata(@NotNull String userName) {
        final User user = PROVIDER.getUserManager().getUser(userName);
        if (user == null) {
            return null;
        }

        final Group group = PROVIDER.getGroupManager().getGroup(user.getPrimaryGroup());
        return group == null ? null : group.getCachedData().getMetaData(QueryOptions.nonContextual());
    }

    private CachedMetaData getCachedMetadata(@NotNull UUID uuid) {
        final User user = PROVIDER.getUserManager().getUser(uuid);
        if (user == null) {
            return null;
        }

        final Group group = PROVIDER.getGroupManager().getGroup(user.getPrimaryGroup());
        return group == null ? null : group.getCachedData().getMetaData(QueryOptions.nonContextual());
    }
}
