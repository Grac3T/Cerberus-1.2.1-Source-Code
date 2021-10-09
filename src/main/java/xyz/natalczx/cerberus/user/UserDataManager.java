// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.user;

import org.bukkit.entity.Player;
import java.util.Collection;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.HashMap;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import java.util.UUID;
import java.util.Map;

public class UserDataManager
{
    private final Map<UUID, UserData> players;
    
    public UserDataManager() {
        Bukkit.getOnlinePlayers().forEach(this::add);
        this.players = new HashMap<UUID, UserData>();
    }
    
    public List<UserData> getUsers() {
        return (List<UserData>)ImmutableList.copyOf((Collection)this.players.values());
    }
    
    public UserData getDataPlayer(final Player player) {
        return this.players.get(player.getUniqueId());
    }
    
    public void add(final Player player) {
        this.players.put(player.getUniqueId(), new UserData(player));
    }
    
    public void remove(final Player player) {
        this.players.remove(player.getUniqueId());
    }
    
    public void addPlayerData(final Player player) {
        this.players.put(player.getUniqueId(), new UserData(player));
    }
    
    public UserData getData(final Player player) {
        return this.players.get(player.getUniqueId());
    }
    
    public void removePlayerData(final Player player) {
        this.players.remove(player.getUniqueId());
    }
}
