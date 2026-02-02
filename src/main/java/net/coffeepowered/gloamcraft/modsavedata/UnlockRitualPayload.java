package net.coffeepowered.gloamcraft.modsavedata;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashSet;
import java.util.Set;

import static net.coffeepowered.gloamcraft.Gloamcraft.MOD_ID;

public record UnlockRitualPayload(String ritualId) implements CustomPacketPayload {
    public static final Type<UnlockRitualPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MOD_ID, "unlock_ritual"));

    public static final StreamCodec<ByteBuf, UnlockRitualPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, UnlockRitualPayload::ritualId,
            UnlockRitualPayload::new
    );


    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            PlayerRitualProgress progress = player.getData(ModAttachments.RITUAL_PROGRESS.get());
            Set<String> newSet = new HashSet<>(progress.getUnlockedRituals());
            if (newSet.add(ritualId)) {
                player.setData(ModAttachments.RITUAL_PROGRESS.get(), new PlayerRitualProgress(newSet));
            }
        });
    }
}