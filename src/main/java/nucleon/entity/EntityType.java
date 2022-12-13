package nucleon.entity;

import lombok.Getter;
import nucleon.entity.metadata.EntityMetadata;

import java.util.function.Supplier;

@Getter
public class EntityType {

    private final String identifier;
    private final Supplier<EntityMetadata> metadataSupplier;

    private EntityType(String identifier, Supplier<EntityMetadata> metadataSupplier) {
        this.identifier = identifier;
        this.metadataSupplier = metadataSupplier;
    }

    @Override
    public String toString() {
        return "EntityType{ identifier='" + identifier + "' }";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof EntityType) return identifier.equals(((EntityType) obj).identifier);
        return false;
    }

    public static EntityType create(String identifier, Supplier<EntityMetadata> metadataSupplier) {
        return new EntityType(
                identifier,
                metadataSupplier
        );
    }
}
