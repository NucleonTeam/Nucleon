package nucleon.entity;

import lombok.Getter;

@Getter
public class EntityType {

    private final String identifier;

    private EntityType(String identifier) {
        this.identifier = identifier;
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

    public static EntityType create(String identifier) {
        return new EntityType(
                identifier
        );
    }
}
