package net.coffeepowered.gloamcraft.rituals;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public record TeleportAbilityPayload(Vec3 target)
        implements CustomPacketPayload {

    public static final Type<TeleportAbilityPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(
                    "gloamcraft",
                    "teleport_ability"));


    public static final StreamCodec<FriendlyByteBuf, TeleportAbilityPayload> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VECTOR3F.map(Vec3::new, vec -> new Vector3f((float)vec.x, (float)vec.y, (float)vec.z)),
                    TeleportAbilityPayload::target,
                    TeleportAbilityPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
