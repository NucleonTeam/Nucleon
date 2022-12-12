package nucleon.event.player;

import nucleon.player.Player;

public class PlayerLoginEvent extends PlayerEvent {

    public PlayerLoginEvent(Player player) {
        super(player);
    }
}
