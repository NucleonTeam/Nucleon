package nucleon.player;

import lombok.NonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class PlayerManager {

    private static PlayerManager instance = null;

    private final ConcurrentHashMap<UUID, Player> players = new ConcurrentHashMap<>();

    private PlayerManager() {

    }

    public static void init() {
        if (instance != null) throw new IllegalStateException("Module already initialized");
        instance = new PlayerManager();
    }

    public static @NonNull PlayerManager getInstance() {
        if (instance == null) throw new IllegalStateException("Module isn't initialized");
        return instance;
    }

    void addPlayer(Player player) {
        players.put(player.getId(), player);
    }

    void removePlayer(Player player) {
        players.remove(player.getId());
    }

    public @NonNull Collection<Player> getPlayers() {
        return new HashSet<>(players.values());
    }

    public @NonNull Player getPlayerById(@NonNull UUID id) {
        return players.get(id);
    }

    public @NonNull Player getPlayerByName(@NonNull String name) {
        for (Player player: players.values()) {
            if (name.equals(player.getName())) return player;
        }
        throw new IllegalArgumentException("Player '" + name + "' not found");
    }
}
