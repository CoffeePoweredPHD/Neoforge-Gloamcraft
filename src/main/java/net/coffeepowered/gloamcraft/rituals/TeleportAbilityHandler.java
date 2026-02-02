package net.coffeepowered.gloamcraft.rituals;

import net.coffeepowered.gloamcraft.modsavedata.ModAttachments;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.core.particles.ParticleTypes;

public class TeleportAbilityHandler {

    public static void handle(TeleportAbilityPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) return;

            ServerLevel level = player.serverLevel();

            // 🔐 Unlock check (SERVER ONLY)
            var progress = player.getData(ModAttachments.RITUAL_PROGRESS);
            if (!progress.hasUnlocked("teleport")) {
                player.displayClientMessage(
                        Component.literal("You do not yet understand the teleport ritual."),
                        true
                );
                return;
            }

            // ⏱ Cooldown (SERVER ONLY)
            long now = System.currentTimeMillis();
            long last = player.getPersistentData().getLong("gloamcraft_last_teleport");

            if (now - last < 1500) {
                player.playSound(SoundEvents.EYEBLOSSOM_CLOSE, 1f, 0.6f);
                return;
            }

            player.getPersistentData().putLong("gloamcraft_last_teleport", now);

            // ✨ Teleport
            Vec3 fromPos = player.position();
            Vec3 toPos = payload.target();

            player.connection.teleport(
                    toPos.x,
                    toPos.y,
                    toPos.z,
                    player.getYRot(),
                    player.getXRot()
            );

            level.playSound(null, fromPos.x, fromPos.y, fromPos.z,
                    SoundEvents.CREAKING_DEATH, SoundSource.PLAYERS, 5.0f, 0.8f);
            spawnCreakingDropParticles(level, fromPos);

            level.playSound(null, toPos.x, toPos.y, toPos.z,
                    SoundEvents.CREAKING_ACTIVATE, SoundSource.PLAYERS, 5.0f, 1.2f);
            spawnCreakingDropParticles(level, toPos);
        });
    }

/*
    public static void handle(TeleportAbilityPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) return;
            //ServerPlayer player = (ServerPlayer) context.player();
            ServerLevel level = player.serverLevel();


            var progress = player.getData(ModAttachments.RITUAL_PROGRESS);
            if (!progress.hasUnlocked("teleport")) {
                player.displayClientMessage(
                        Component.literal("You do not yet understand the teleport ritual."),
                        true
                );
                return;
            }

            Vec3 fromPos = player.position(); //Players current location, needed for sound
            if (player == null) return;

            Vec3 toPos = payload.target();

            player.connection.teleport(
                    toPos.x,
                    toPos.y,
                    toPos.z,
                    player.getYRot(),
                    player.getXRot()
            );

            level.playSound(null,fromPos.x, fromPos.y, fromPos.z, SoundEvents.CREAKING_DEATH, SoundSource.PLAYERS, 5.0f, 0.8f);
            spawnCreakingDropParticles(level, fromPos);
            level.playSound(null,toPos.x, toPos.y, toPos.z,SoundEvents.CREAKING_ACTIVATE,SoundSource.PLAYERS,5.0f,1.2f);
            spawnCreakingDropParticles(level, toPos);

        });*/
    
    private static void spawnCreakingDropParticles(ServerLevel level, Vec3 pos) {
        level.sendParticles(
                /* particle */ ParticleTypes.PALE_OAK_LEAVES,
                /* x */ pos.x,
                /* y */ pos.y + 0.75,
                /* z */ pos.z,
                /* count */ 16,
                /* offsetX */ 0.25,
                /* offsetY */ 0.1,
                /* offsetZ */ 0.25,
                /* speed */ 0.02
        );
    }

}
