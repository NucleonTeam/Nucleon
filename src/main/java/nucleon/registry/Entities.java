package nucleon.registry;

import com.nukkitx.nbt.NbtMap;
import com.nukkitx.protocol.bedrock.packet.AvailableEntityIdentifiersPacket;
import lombok.extern.log4j.Log4j2;
import nucleon.entity.EntityType;
import nucleon.entity.VanillaEntities;
import nucleon.nbt.CompoundTag;
import nucleon.nbt.Tag;
import reactor.util.annotation.NonNull;

import java.util.HashMap;
import java.util.LinkedList;

@Log4j2
public class Entities {

    private final HashMap<String, EntityType> entities = new HashMap<>();
    private NbtMap finallyEntityIdentifiersList = null;

    Entities() {
        registerDefaultEntities();
    }

    public void registerEntityType(@NonNull EntityType entity) {
        Registry.checkForInitialization();

        if (isRegistered(entity)) {
            var registered = entities.get(entity.getIdentifier());
            log.error("EntityType '{}'({}) already registered. Entity with class {} was ignored",
                    entity.getIdentifier(), registered.getClass().getName(), entity.getClass().getName());
            return;
        }

        entities.put(entity.getIdentifier(), entity);
    }

    public boolean isRegistered(@NonNull EntityType entity) {
        return entities.containsKey(entity.getIdentifier());
    }

    public @NonNull EntityType getEntityTypeByIdentifier(@NonNull String identifier) {
        if (entities.containsKey(identifier)) return entities.get(identifier);

        throw new IllegalArgumentException("EntityType with id '" + identifier + "' not found");
    }

    void completeInitialization() {
        var list = new LinkedList<NbtMap>();
        entities.values().forEach(entityType -> list.add(entityType.getCompoundTag().getNbtMap()));

        var nbt = new CompoundTag();
        nbt.setList(Tag.Compound("idlist"), list);

        log.info("{} entity types were registered", entities.size());

        finallyEntityIdentifiersList = nbt.getNbtMap();
    }

    public @NonNull AvailableEntityIdentifiersPacket prepareAvailableEntityIdentifiersPacket() {
        var pk = new AvailableEntityIdentifiersPacket();
        pk.setIdentifiers(finallyEntityIdentifiersList);
        return pk;
    }

    private void registerDefaultEntities() {
        registerEntityType(VanillaEntities.HUMAN);
    }
}
