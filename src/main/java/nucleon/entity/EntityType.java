package nucleon.entity;

import lombok.Getter;
import nucleon.entity.metadata.EntityMetadata;
import nucleon.nbt.CompoundTag;
import nucleon.nbt.Tag;
import reactor.util.annotation.NonNull;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@Getter
public final class EntityType {

    private final static AtomicInteger freeId = new AtomicInteger(1);

    private final String identifier;
    private final int runtimeId = freeId.getAndIncrement();
    private final String bid;
    private final Supplier<EntityMetadata> metadataSupplier;

    private EntityType(String identifier, String bid, Supplier<EntityMetadata> metadataSupplier) {
        this.identifier = identifier;
        this.bid = bid;
        this.metadataSupplier = metadataSupplier;
    }

    @Override
    public String toString() {
        return "EntityType{ identifier='" + identifier + "', runtimeId=" + runtimeId + " }";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof EntityType target) return identifier.equals(target.identifier) && runtimeId == target.runtimeId;
        return false;
    }

    public @NonNull CompoundTag getCompoundTag() {
        return new CompoundTag()
                .set(Tag.String("bid"), bid)
                .set(Tag.Boolean("hasspawnegg"), false)
                .set(Tag.String("id"), identifier)
                .set(Tag.Integer("rid"), runtimeId)
                .set(Tag.Boolean("summonable"), false);
    }

    public static EntityType create(@NonNull String identifier, @NonNull Supplier<EntityMetadata> metadataSupplier) {
        return create(identifier, "", metadataSupplier);
    }

    public static EntityType create(@NonNull String identifier, @NonNull String bid, @NonNull Supplier<EntityMetadata> metadataSupplier) {
        return new EntityType(
                identifier,
                bid,
                metadataSupplier
        );
    }
}
