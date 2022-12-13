package nucleon.entity.metadata;

import com.nukkitx.protocol.bedrock.data.entity.EntityDataMap;
import lombok.Getter;

public abstract class EntityMetadata {

    @Getter protected final EntityDataMap dataMap = new EntityDataMap();

    public EntityMetadata() {
        init();
    }

    protected void init() {

    }
}
