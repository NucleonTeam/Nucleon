package nucleon.network.handler;

import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.*;
import nucleon.player.Player;

public class InGamePacketHandler implements BedrockPacketHandler {

    private final BedrockServerSession session;
    private final Player player;

    public InGamePacketHandler(BedrockServerSession session, Player player) {
        this.session = session;
        this.player = player;
    }

    void sendStartGamePacket() {
        var startGamePacket = new StartGamePacket();
        //TODO
        session.sendPacket(startGamePacket);

        var biomeDefinitionList = new BiomeDefinitionListPacket();
        //TODO: biome definition list
        session.sendPacket(biomeDefinitionList);

        var entityIdentifiers = new AvailableEntityIdentifiersPacket();
        //TODO: entity identifiers
        session.sendPacket(entityIdentifiers);

        var creativeContent = new CreativeContentPacket();
        //TODO: creative content
        session.sendPacket(creativeContent);

        //TODO: update adventure packet
        //TODO: update attributes packet
        //TODO: send packets with potions effects
        //TODO: set entity data packet
        //TODO: send world time
    }

    @Override
    public boolean handle(SetLocalPlayerAsInitializedPacket packet) {
        //TODO
        return true;
    }
}
