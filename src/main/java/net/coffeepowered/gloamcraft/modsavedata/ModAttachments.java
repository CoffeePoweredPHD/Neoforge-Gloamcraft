package net.coffeepowered.gloamcraft.modsavedata;

import net.coffeepowered.gloamcraft.Gloamcraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Gloamcraft.MOD_ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerRitualProgress> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()).map(Set::copyOf, List::copyOf),
            PlayerRitualProgress::unlockedRituals,
            PlayerRitualProgress::new
    );

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerRitualProgress>> RITUAL_PROGRESS =
            ATTACHMENT.register("ritual_progress", () -> AttachmentType.builder(() -> new PlayerRitualProgress(new HashSet<>()))
                    .serialize(PlayerRitualProgress.CODEC) // Saves to disk
                    .copyOnDeath()
                    .build());

}
