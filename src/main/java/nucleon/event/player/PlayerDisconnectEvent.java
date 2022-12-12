package nucleon.event.player;

import com.nukkitx.network.util.DisconnectReason;
import lombok.Getter;
import nucleon.player.Player;

public class PlayerDisconnectEvent extends PlayerEvent {

    @Getter private final DisconnectReason reason;

    public PlayerDisconnectEvent(Player player, DisconnectReason reason) {
        super(player);

        this.reason = reason;
    }
}
