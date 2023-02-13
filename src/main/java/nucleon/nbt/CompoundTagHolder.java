package nucleon.nbt;

import reactor.util.annotation.NonNull;

public interface CompoundTagHolder {

    @NonNull CompoundTag getCompoundTag();

    void setCompoundTag(@NonNull CompoundTag newTag);
}
