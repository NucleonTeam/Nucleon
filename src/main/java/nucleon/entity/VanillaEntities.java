package nucleon.entity;

import nucleon.entity.metadata.PlayerMetadata;

public interface VanillaEntities {

    EntityType HUMAN = EntityType.create("minecraft:human", PlayerMetadata::new);
}
