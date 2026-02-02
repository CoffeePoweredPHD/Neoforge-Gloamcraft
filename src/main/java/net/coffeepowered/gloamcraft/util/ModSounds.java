package net.coffeepowered.gloamcraft.util;

import net.coffeepowered.gloamcraft.Gloamcraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, Gloamcraft.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> FINAL_DAY =
            SOUND_EVENTS.register("final_day",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(Gloamcraft.MOD_ID, "final_day")
                    ));
}
