package nucleon.server;

import lombok.Getter;
import lombok.Setter;

import java.net.InetSocketAddress;

@Getter
@Setter
public final class ServerSettings {

    private String ipAddress = "0.0.0.0";
    private int port = 19132;

    public InetSocketAddress getInetAddress() {
        return new InetSocketAddress(ipAddress, 19132);
    }
}
