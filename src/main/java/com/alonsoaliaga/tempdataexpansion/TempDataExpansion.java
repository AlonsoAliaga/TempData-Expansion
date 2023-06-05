package com.alonsoaliaga.tempdataexpansion;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.Cacheable;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TempDataExpansion  extends PlaceholderExpansion implements Configurable, Cacheable, Listener {
    public Cache<UUID, Cache<String,String>> cachePlayerData = null;
    public Cache<String, String> cacheGlobalData = null;
    private String hexFormat;
    private int playerDataCacheDuration;
    private TimeUnit playerDataCacheTimeUnit;
    public TempDataExpansion() {
        boolean debug = false;
        try {
            debug = getPlaceholderAPI().getPlaceholderAPIConfig().isDebugMode();
        } catch (Throwable ignored) {
        }
        hexFormat = getString("default-hex-format", "&#<hex-color>");

        playerDataCacheDuration = getInt("player-cache.data.duration",1);
        try {
            playerDataCacheTimeUnit = TimeUnit.valueOf(getString("player-cache.data.time-unit", "MINUTES"));
        }catch (Throwable e) {
            playerDataCacheTimeUnit = TimeUnit.MINUTES;
        }

        int playerCacheDuration = getInt("player-cache.player.duration",5);
        TimeUnit playerCacheTimeUnit;
        try {
            playerCacheTimeUnit = TimeUnit.valueOf(getString("player-cache.player.time-unit", "MINUTES"));
        }catch (Throwable e) {
            playerCacheTimeUnit = TimeUnit.MINUTES;
        }
        cachePlayerData = CacheBuilder.newBuilder()
                .expireAfterAccess(playerCacheDuration, playerCacheTimeUnit)
                .build();
        int globalCacheDuration = getInt("global-cache.duration",5);
        int globalCacheMaxSize = Math.max(0,getInt("global-cache.max-size", 100));
        TimeUnit globalCacheTimeUnit;
        try {
            globalCacheTimeUnit = TimeUnit.valueOf(getString("global-cache.time-unit", "MINUTES"));
        }catch (Throwable e) {
            globalCacheTimeUnit = TimeUnit.MINUTES;
        }
        CacheBuilder<Object, Object> b = CacheBuilder.newBuilder()
                .expireAfterAccess(globalCacheDuration, globalCacheTimeUnit);
        if(globalCacheMaxSize > 0) b.maximumSize(globalCacheMaxSize);
        cacheGlobalData = b.build();
    }
    @Override
    public Map<String, Object> getDefaults() {
        final Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("default-hex-format","&#<hex-color>");
        defaults.put("player-cache.player.duration",5);
        defaults.put("player-cache.player.time-unit", "MINUTES");
        defaults.put("player-cache.data.duration",1);
        defaults.put("player-cache.data.time-unit", "MINUTES");
        defaults.put("global-cache.duration",5);
        defaults.put("global-cache.time-unit", "MINUTES");
        defaults.put("global-cache.max-size", 100);
        return defaults;
    }
    @Override
    public void clear() {
        if(cachePlayerData != null) cachePlayerData.invalidateAll();
        if(cacheGlobalData != null) cacheGlobalData.invalidateAll();
    }
    @Override
    public String onPlaceholderRequest(Player p, String params) {
        if (params.equalsIgnoreCase("version")) {
            return getVersion();
        }
        if (params.equalsIgnoreCase("author")) {
            return getAuthor();
        }
        // %tempdata_playerset_test_<percent>a_placeholder<percent>%
        if (params.startsWith("playerset_")) {
            String[] parts = params.substring(10).split("_",2);
            if(parts.length >= 2) {
                Cache<String, String> map = cachePlayerData.getIfPresent(p.getUniqueId());
                if(map == null) {
                    map = CacheBuilder.newBuilder()
                            .expireAfterAccess(playerDataCacheDuration, playerDataCacheTimeUnit)
                            .build();
                    cachePlayerData.put(p.getUniqueId(),map);
                }
                String value = PlaceholderAPI.setPlaceholders(p,PlaceholderAPI.setBracketPlaceholders(p,parts[1].replace("<percent>","%")));
                map.put(parts[0],value);
                return value;
            }
            return null;
        }
        // %tempdata_playerget_test_<percent>a_placeholder<percent>%
        if (params.startsWith("playerget_")) {
            String[] parts = params.substring(10).split("_",2);
            if(parts.length >= 2) {
                Cache<String, String> map = cachePlayerData.getIfPresent(p.getUniqueId());
                if(map == null) {
                    map = CacheBuilder.newBuilder()
                            .expireAfterAccess(playerDataCacheDuration, playerDataCacheTimeUnit)
                            .build();
                    cachePlayerData.put(p.getUniqueId(),map);
                }
                String storedValue = map.getIfPresent(parts[0]);
                if(storedValue == null) {
                    return PlaceholderAPI.setPlaceholders(p,PlaceholderAPI.setBracketPlaceholders(p,parts[1].replace("<percent>","%")));
                }else{
                    return storedValue;
                }
            }
            return null;
        }
        // %tempdata_playergetorset_test_<percent>a_placeholder<percent>%
        if (params.startsWith("playergetorset_")) {
            String[] parts = params.substring(15).split("_",2);
            if(parts.length >= 2) {
                Cache<String, String> map = cachePlayerData.getIfPresent(p.getUniqueId());
                if(map == null) {
                    map = CacheBuilder.newBuilder()
                            .expireAfterAccess(playerDataCacheDuration, playerDataCacheTimeUnit)
                            .build();
                    cachePlayerData.put(p.getUniqueId(),map);
                }
                String storedValue = map.getIfPresent(parts[0]);
                if(storedValue == null) {
                    String newValue = PlaceholderAPI.setPlaceholders(p,PlaceholderAPI.setBracketPlaceholders(p,parts[1].replace("<percent>","%")));;
                    map.put(parts[1],newValue);
                    return newValue;
                }else{
                    return storedValue;
                }
            }
            return null;
        }
        // %tempdata_globalset_test_<percent>a_placeholder<percent>%
        if (params.startsWith("globalset_")) {
            String[] parts = params.substring(10).split("_",2);
            if(parts.length >= 2) {
                String value = PlaceholderAPI.setPlaceholders(p,PlaceholderAPI.setBracketPlaceholders(p,parts[1].replace("<percent>","%")));
                cacheGlobalData.put(parts[0],value);
                return value;
            }
            return null;
        }
        // %tempdata_globalget_test_<percent>a_placeholder<percent>%
        if (params.startsWith("globalget_")) {
            String[] parts = params.substring(10).split("_",2);
            if(parts.length >= 2) {
                String value = cacheGlobalData.getIfPresent(parts[0]);
                if(value == null) {
                    value = PlaceholderAPI.setPlaceholders(p,PlaceholderAPI.setBracketPlaceholders(p,parts[1].replace("<percent>","%")));
                    return value;
                }else{
                    return value;
                }
            }
            return null;
        }
        // %tempdata_globalgetorset_test_<percent>a_placeholder<percent>%
        if (params.startsWith("globalgetorset_")) {
            String[] parts = params.substring(15).split("_",2);
            if(parts.length >= 2) {
                String value = cacheGlobalData.getIfPresent(parts[0]);
                if(value == null) {
                    value = PlaceholderAPI.setPlaceholders(p,PlaceholderAPI.setBracketPlaceholders(p,parts[1].replace("<percent>","%")));
                    cacheGlobalData.put(parts[0],value);
                    return value;
                }else{
                    return value;
                }
            }
            return null;
        }
        return null;
    }
    @Override
    public @NotNull String getIdentifier() {
        return "tempdata";
    }
    @Override
    public @NotNull String getAuthor() {
        return "AlonsoAliaga";
    }
    @Override
    public @NotNull String getVersion() {
        return "0.1-BETA";
    }
    @Override
    public boolean persist() {
        return true;
    }
    @Override
    public boolean canRegister() {
        return true;
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e) {
        cachePlayerData.put(e.getPlayer().getUniqueId(), CacheBuilder.newBuilder()
                .expireAfterAccess(playerDataCacheDuration, playerDataCacheTimeUnit)
                .build());
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent e) {
        Cache<String, String> data = cachePlayerData.getIfPresent(e.getPlayer().getUniqueId());
        if(data != null) {
            data.invalidateAll();
            cachePlayerData.invalidate(e.getPlayer().getUniqueId());
        }
    }
}