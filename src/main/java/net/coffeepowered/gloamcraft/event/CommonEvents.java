package net.coffeepowered.gloamcraft.event;

import net.coffeepowered.gloamcraft.Gloamcraft;
import net.coffeepowered.gloamcraft.modsavedata.ModAttachments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = Gloamcraft.MOD_ID)
public class CommonEvents {

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        var player = event.getEntity();
        var progress = player.getData(ModAttachments.RITUAL_PROGRESS);

        System.out.println("Loaded rituals: " + progress.getUnlockedRituals());
    }
}
