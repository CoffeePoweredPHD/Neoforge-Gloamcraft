package net.coffeepowered.gloamcraft.util;

import net.coffeepowered.gloamcraft.Gloamcraft;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = Gloamcraft.MOD_ID, value = Dist.CLIENT)
public class ModKeybinds {
    public static KeyMapping ABILITY;

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        ABILITY = new KeyMapping(
                "key.gloamcraft.ability",
                GLFW.GLFW_KEY_V,
                "key.categories.gloamcraft"
        );

        event.register(ABILITY);
    }
}