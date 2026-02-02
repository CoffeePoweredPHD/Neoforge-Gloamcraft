package net.coffeepowered.gloamcraft.network;

import net.coffeepowered.gloamcraft.Gloamcraft;
import net.coffeepowered.gloamcraft.modsavedata.UnlockRitualPayload;
import net.coffeepowered.gloamcraft.rituals.TeleportAbilityHandler;
import net.coffeepowered.gloamcraft.rituals.TeleportAbilityPayload;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModNetworking {


    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(Gloamcraft.MOD_ID);

        registrar.playToServer(
                UnlockRitualPayload.TYPE,
                UnlockRitualPayload.STREAM_CODEC,
                UnlockRitualPayload::handle
        );

        event.registrar("gloamcraft")
                .playToServer(
                        TeleportAbilityPayload.TYPE,
                        TeleportAbilityPayload.CODEC,
                        TeleportAbilityHandler::handle
                );
    }

    public static void sendTeleportRequest(Vec3 target) {
        PacketDistributor.sendToServer(
                new TeleportAbilityPayload(target)
        );
    }
}