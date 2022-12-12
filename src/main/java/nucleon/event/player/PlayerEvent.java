package nucleon.event.player;

import lombok.Getter;
import nucleon.event.Event;
import nucleon.player.Player;

public abstract class PlayerEvent extends Event {

    @Getter private final Player player;

    public PlayerEvent(Player player) {
        this.player = player;
    }
}
