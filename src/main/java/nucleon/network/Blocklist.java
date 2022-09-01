package nucleon.network;

import nucleon.server.Server;

import java.net.InetSocketAddress;
import java.util.HashMap;

public class Blocklist {
    private final HashMap<InetSocketAddress, Long> entries = new HashMap<>();

    public void push(InetSocketAddress address, long ticks) {
        long expiresAt = Server.getInstance().getTick() + ticks;
        entries.put(address, expiresAt);
    }

    public void remove(InetSocketAddress address) {
        entries.remove(address);
    }

    public boolean contains(InetSocketAddress address) {
        if (!entries.containsKey(address)) {
            return false;
        }
        long expiresAt = entries.get(address);
        if (Server.getInstance().getTick() >= expiresAt) {
            remove(address);
            return false;
        }
        return true;
    }
}
