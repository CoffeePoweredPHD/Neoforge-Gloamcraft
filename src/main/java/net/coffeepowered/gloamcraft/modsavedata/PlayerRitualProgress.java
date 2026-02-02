package net.coffeepowered.gloamcraft.modsavedata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record PlayerRitualProgress(Set<String> unlockedRituals) {

    public PlayerRitualProgress(Set<String> unlockedRituals) {
        this.unlockedRituals = new HashSet<>(unlockedRituals);
    }

    public static final Codec<PlayerRitualProgress> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.listOf().xmap(Set::copyOf, List::copyOf)
                            .fieldOf("unlocked").forGetter(PlayerRitualProgress::unlockedRituals)
            ).apply(instance, PlayerRitualProgress::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerRitualProgress> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()).map(Set::copyOf, List::copyOf),
            PlayerRitualProgress::unlockedRituals,
            PlayerRitualProgress::new
    );

    public boolean hasUnlocked(String ritualId) {
        return unlockedRituals.contains(ritualId);
    }

    public void unlock(String ritualId) {
        unlockedRituals.add(ritualId);
    }

    public Set<String> getUnlockedRituals() {
        return Collections.unmodifiableSet(unlockedRituals);
    }

}
