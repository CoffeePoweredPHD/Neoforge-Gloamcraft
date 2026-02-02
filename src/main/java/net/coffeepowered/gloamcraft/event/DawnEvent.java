package net.coffeepowered.gloamcraft.event;

import net.coffeepowered.gloamcraft.Gloamcraft;
import net.coffeepowered.gloamcraft.util.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.level.SleepFinishedTimeEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.Random;

@EventBusSubscriber(modid = Gloamcraft.MOD_ID)
public class DawnEvent {

    private static long lastDawnDay = -1;
    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public static void onWakeUp(SleepFinishedTimeEvent event) {

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        ServerLevel level = server.overworld();
        if (level == null) return;

        // 1 in 100 chance?
        if (RANDOM.nextInt(500) != 0) return;

        broadcastFinalDay(server);
    }

    private static void broadcastFinalDay(MinecraftServer server) {


        Component title = Component.literal("Dawn of the Final Day")
                .withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD);

        Component subtitle = Component.literal("24 hours remain.")
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    ModSounds.FINAL_DAY.get(), SoundSource.AMBIENT, 1.0f, 1.0f);
            player.connection.send(
                    new ClientboundSetTitleTextPacket(title)
            );
            player.connection.send(
                    new ClientboundSetSubtitleTextPacket(subtitle)
            );
            player.connection.send(
                    new ClientboundSetTitlesAnimationPacket(
                            10,  // fade in
                            80,  // stay
                            20   // fade out
                    )
            );
        }
    }

}
