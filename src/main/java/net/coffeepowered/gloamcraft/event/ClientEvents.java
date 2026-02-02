package net.coffeepowered.gloamcraft.event;

import net.coffeepowered.gloamcraft.Gloamcraft;
import net.coffeepowered.gloamcraft.modsavedata.ModAttachments;
import net.coffeepowered.gloamcraft.network.ModNetworking;
import net.coffeepowered.gloamcraft.modsavedata.RitualProgressHelper;
import net.coffeepowered.gloamcraft.util.ModKeybinds;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = Gloamcraft.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {
    private static boolean wasKeyDown = false;
    private static long keyDownTime = 0;
    private static boolean holdTriggered = false;
    private static final long HOLD_TIME_MS = 450; // tweak 400–600

    private static long lastTeleportTime = 0;
    private static final long TELEPORT_COOLDOWN_MS = 1500; // 1.5s, tune freely


    @SubscribeEvent
    public static void onAbilityKey(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        boolean isKeyDown = ModKeybinds.ABILITY.isDown();
        long now = System.currentTimeMillis();

        // Key just pressed
        if (isKeyDown && !wasKeyDown) {
            keyDownTime = now;
            holdTriggered = false;
        }

        // Key held long enough → transform
        if (isKeyDown && !holdTriggered && now - keyDownTime >= HOLD_TIME_MS) {
            holdTriggered = true;
            onAbilityTransform(mc.player);
        }

        // Key just released
        if (!isKeyDown && wasKeyDown) {
            if (!holdTriggered) {
                onAbilityTeleport(mc.player);
            }
        }

        wasKeyDown = isKeyDown;
    }

    private static void onAbilityTeleport(Player player) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        double maxRange = 50.0;

        Vec3 eyePos = player.getEyePosition();
        Vec3 lookVec = player.getLookAngle();
        Vec3 reachVec = eyePos.add(lookVec.scale(maxRange));

        ClipContext context = new ClipContext(
                eyePos,
                reachVec,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        );

        BlockHitResult hit = mc.level.clip(context);

        Vec3 target = hit.getType() == HitResult.Type.BLOCK
                ? hit.getLocation()
                : reachVec;

        // Ask the server to try
        ModNetworking.sendTeleportRequest(target);
    }


    /*private static void onAbilityTeleport(Player player) {


        //Spam Protection
        long now = System.currentTimeMillis();
        if (now - lastTeleportTime < TELEPORT_COOLDOWN_MS) {
            player.playSound(SoundEvents.EYEBLOSSOM_CLOSE, 1f, 0.6f);
            return;
        }
        lastTeleportTime = now;


        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;



        double maxRange = 50.0;

        Vec3 eyePos = player.getEyePosition();
        Vec3 lookVec = player.getLookAngle();
        Vec3 reachVec = eyePos.add(lookVec.scale(maxRange));

        ClipContext context = new ClipContext(
                eyePos,
                reachVec,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        );

        BlockHitResult hit = mc.level.clip(context);

        Vec3 target = hit.getType() == HitResult.Type.BLOCK
                ? hit.getLocation()
                : reachVec;

        ModNetworking.sendTeleportRequest(target);
    }*/

    private static void onAbilityTransform(Player player) {
        //ModNetworking.sendTransformRequest();
    }
}

