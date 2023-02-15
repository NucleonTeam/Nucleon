package nucleon.item;

import reactor.util.annotation.NonNull;

public final class ItemStack {

    public final static ItemStack AIR = of(VanillaItems.AIR);

    private final Material material;

    private ItemStack(Material material) {
        this.material = material;
    }

    public @NonNull Material getMaterial() {
        return material;
    }

    public static @NonNull ItemStack of(@NonNull Material material) {
        return new ItemStack(material);
    }
}
