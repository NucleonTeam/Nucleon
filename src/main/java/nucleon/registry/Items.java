package nucleon.registry;

import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import lombok.extern.log4j.Log4j2;
import nucleon.item.Material;
import nucleon.item.VanillaItems;
import reactor.util.annotation.NonNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Log4j2
public class Items {

    private final HashMap<String, Material> items = new HashMap<>();
    private final HashMap<Short, Material> runtimeIds = new HashMap<>();
    private final List<StartGamePacket.ItemEntry> startGamePacketEntries = new LinkedList<>();

    Items() {
        registerDefaultItems();
    }

    public void registerItem(@NonNull Material material) {
        Registry.checkForInitialization();

        if (items.containsKey(material.getIdentifier())) {
            log.error("Item '{}' already registered", material.getIdentifier());
            return;
        }

        items.put(material.getIdentifier(), material);
        runtimeIds.put(material.getRuntimeId(), material);
    }

    public @NonNull Material getItemByIdentifier(@NonNull String identifier) {
        if (items.containsKey(identifier)) return items.get(identifier);

        throw new IllegalArgumentException("Item with identifier '" + identifier + "' not found");
    }

    public @NonNull Material getItemByRuntimeId(short runtimeId) {
        if (runtimeIds.containsKey(runtimeId)) return runtimeIds.get(runtimeId);

        throw new IllegalArgumentException("Item with runtime id " + runtimeId + " not found");
    }

    void completeInitialization() {
        items.values().forEach(material -> {
            startGamePacketEntries.add(new StartGamePacket.ItemEntry(material.getIdentifier(), material.getRuntimeId()));
        });

        log.info("{} biomes were registered", items.size());
    }

    public @NonNull List<StartGamePacket.ItemEntry> getItemEntries() {
        return startGamePacketEntries;
    }

    private void registerDefaultItems() {
        registerItem(VanillaItems.AIR);
    }
}
