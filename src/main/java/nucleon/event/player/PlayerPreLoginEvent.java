package nucleon.event.player;

import lombok.Getter;
import lombok.NonNull;
import nucleon.event.Event;
import nucleon.player.PlayerChainData;

@Getter
public class PlayerPreLoginEvent extends Event {

    private final PlayerChainData chainData;
    private Result result = Result.ACCEPTED;
    private String rejectReason = null;

    public PlayerPreLoginEvent(PlayerChainData chainData) {
        this.chainData = chainData;
    }

    public void reject(@NonNull String reason) {
        result = Result.REJECTED;
        rejectReason = reason;
    }

    public void accept() {
        result = Result.ACCEPTED;
        rejectReason = null;
    }

    public enum Result {
        ACCEPTED,
        REJECTED
    }
}
