package nucleon.network.handler;

import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.data.GameType;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.*;
import nucleon.event.player.PlayerLoginEvent;
import nucleon.player.Player;
import nucleon.registry.Items;
import nucleon.registry.Registry;

import java.util.ArrayList;

public class InGamePacketHandler implements BedrockPacketHandler {

    private final BedrockServerSession session;
    private final Player player;

    public InGamePacketHandler(BedrockServerSession session, Player player) {
        this.session = session;
        this.player = player;
    }

    void sendStartGamePacket() {
        var loginEvent = new PlayerLoginEvent(player);
        loginEvent.call();

        if (player.isDisconnected() || player.isRemoved()) return;

        var defaultWorld = loginEvent.getSpawningWorld();
        if (defaultWorld != null) {
            player.setWorld(defaultWorld);
            player.setPosition(loginEvent.getSpawnPosition());
        }

        if (player.getWorld() == null) {
            session.disconnect("Default world not found");
            return;
        }

        var startGamePacket = new StartGamePacket();
        startGamePacket.setUniqueEntityId(player.getEntityId());
        startGamePacket.setRuntimeEntityId(player.getEntityId());
        startGamePacket.setPlayerGameType(player.getGameMode().getType());
        startGamePacket.setPlayerPosition(player.getPosition().toFloat());
        startGamePacket.setRotation(player.getRotation().toVector2());
        startGamePacket.setSeed(-1L);
        startGamePacket.setDimensionId(player.getWorld().getDimension().getId());
        startGamePacket.setLevelGameType(GameType.SURVIVAL);
        startGamePacket.setDifficulty(1);
        startGamePacket.setDefaultSpawn(player.getPosition().toInt());
        startGamePacket.setAchievementsDisabled(true);
        startGamePacket.setDayCycleStopTime(-1);
        startGamePacket.setRainLevel(0);
        startGamePacket.setLightningLevel(0);
        startGamePacket.setCommandsEnabled(true);
        startGamePacket.getGamerules(); //TODO: create game rules
        startGamePacket.setLevelId("");
        startGamePacket.setLevelName("world");
        startGamePacket.setGeneratorId(1);
        startGamePacket.getPlayerMovementSettings(); //...
        startGamePacket.setItemEntries(Registry.items().getItemEntries());

        session.sendPacket(startGamePacket);

        session.sendPacket(Registry.biomes().prepareBiomeDefinitionPacket());
        session.sendPacket(Registry.entities().prepareAvailableEntityIdentifiersPacket());

        var creativeContent = new CreativeContentPacket();
        //TODO: creative content
        session.sendPacket(creativeContent);

        var craftingData = new CraftingDataPacket();
        //TODO: crafting data
        session.sendPacket(craftingData);

        //TODO: update adventure packet
        player.sendAttributes();
        //TODO: send packets with potions effects
        //TODO: set entity data packet
        //TODO: send world time

        player.addViewer(player);

        var playStatusPacket = new PlayStatusPacket();
        playStatusPacket.setStatus(PlayStatusPacket.Status.PLAYER_SPAWN);
        player.sendPacket(playStatusPacket);
    }

    @Override
    public boolean handle(SetLocalPlayerAsInitializedPacket packet) {
        //TODO
        return true;
    }
}
