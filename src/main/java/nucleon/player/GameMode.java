package nucleon.player;

import com.nukkitx.protocol.bedrock.data.GameType;
import lombok.Getter;

@Getter
public enum GameMode {
    SURVIVAL(0, GameType.SURVIVAL),
    CREATIVE(1, GameType.CREATIVE),
    ADVENTURE(2, GameType.ADVENTURE),
    SPECTATOR(3, GameType.SPECTATOR);

    private final int id;
    private final GameType type;

    GameMode(int id, GameType type) {
        this.id = id;
        this.type = type;
    }
}
