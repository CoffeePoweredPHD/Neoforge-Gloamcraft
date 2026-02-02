package net.coffeepowered.gloamcraft.modsavedata;

import net.minecraft.world.entity.player.Player;

public class RitualProgressHelper {

    public static PlayerRitualProgress get(Player player) {
        return player.getData(ModAttachments.RITUAL_PROGRESS.get());
    }

    public static boolean hasUnlocked(Player player, String ritual) {
        return get(player).hasUnlocked(ritual);
    }

    public static void unlock(Player player, String ritual) {
        get(player).unlock(ritual);
    }
}
