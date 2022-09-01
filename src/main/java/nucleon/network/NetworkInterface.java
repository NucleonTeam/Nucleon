package nucleon.network;

import lombok.Getter;

import java.net.InetSocketAddress;

public abstract class NetworkInterface {

    @Getter private final InetSocketAddress address;

    public NetworkInterface(InetSocketAddress address) {
        this.address = address;
    }
}
