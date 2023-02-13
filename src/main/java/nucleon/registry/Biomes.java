package nucleon.registry;

import com.nukkitx.nbt.NbtMap;
import com.nukkitx.protocol.bedrock.packet.BiomeDefinitionListPacket;
import lombok.extern.log4j.Log4j2;
import nucleon.nbt.CompoundTag;
import nucleon.nbt.Tag;
import nucleon.world.biome.Biome;
import nucleon.world.biome.DefaultBiome;
import reactor.util.annotation.NonNull;

import java.util.HashMap;

@Log4j2
public class Biomes {

    private final HashMap<String, Biome> biomes = new HashMap<>();
    private NbtMap finallyBiomeDefinitionNbt = null;

    Biomes() {
        registerDefaultBiomes();
    }

    public void registerBiome(Biome biome) {
        Registry.checkForInitialization();

        if (biomes.containsKey(biome.getId())) {
            var registered = biomes.get(biome.getId());
            log.error("Biome '{}'({}) already registered. Biome with class {} was ignored",
                    biome.getId(), registered.getClass().getName(), biome.getClass().getName());
            return;
        }
        biomes.put(biome.getId(), biome);
    }

    public @NonNull Biome getBiomeById(@NonNull String id) throws IllegalArgumentException {
        if (biomes.containsKey(id)) return biomes.get(id);

        throw new IllegalArgumentException("Biome with id '" + id + "' not found");
    }

    void completeInitialization() {
        var nbt = new CompoundTag();
        biomes.forEach((biomeId, biome) -> nbt.set(Tag.Compound(biomeId), new CompoundTag(biome.getNbt())));

        log.info("{} biomes were registered", biomes.size());

        finallyBiomeDefinitionNbt = nbt.getNbtMap();
    }

    public @NonNull BiomeDefinitionListPacket prepareBiomeDefinitionPacket() {
        var pk = new BiomeDefinitionListPacket();
        pk.setDefinitions(finallyBiomeDefinitionNbt);
        return pk;
    }

    private void registerDefaultBiomes() {
        registerBiome(new DefaultBiome());
    }
}
