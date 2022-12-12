package nucleon.world;

import lombok.Getter;

@Getter
public enum Dimension {

    OVERWORLD(0),
    NETHER(1),
    END(2);

    private final int id;

    Dimension(int id) {
        this.id = id;
    }
}
