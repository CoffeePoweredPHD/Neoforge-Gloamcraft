package net.coffeepowered.gloamcraft.block.ritualnetwork;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import com.mojang.serialization.Codec;

import java.util.HashMap;
import java.util.Map;

public class DarkEffigyData extends SavedData{
    private Map<BlockPos, String> effigies = new HashMap<>();

    private static final Codec<Map<BlockPos, String>> MAP_CODEC =
            Codec.unboundedMap(BlockPos.CODEC, Codec.STRING);

    public static final SavedData.Factory<DarkEffigyData> FACTORY = new SavedData.Factory<>(
            DarkEffigyData::new,
            DarkEffigyData::load,
            null
    );

    public DarkEffigyData() {}


    public static DarkEffigyData load(CompoundTag tag, HolderLookup.Provider registries) {
        DarkEffigyData data = new DarkEffigyData();
        if (tag.contains("effigies")) {
            MAP_CODEC.parse(NbtOps.INSTANCE, tag.get("effigies"))
                    .resultOrPartial(System.err::println)
                    .ifPresent(loadedMap -> data.effigies = new HashMap<>(loadedMap));
        }
        return data;
    }

    public Map<BlockPos, String> getEffigies() {
        return this.effigies;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        MAP_CODEC.encodeStart(NbtOps.INSTANCE, this.effigies)
                .resultOrPartial(System.err::println)
                .ifPresent(nbt -> tag.put("effigies", nbt));
        return tag;
    }

    public void addEffigy(BlockPos pos, String name) {
        this.effigies.put(pos, name);
        this.setDirty();
    }

    public void removeEffigy(BlockPos pos) {
        if (this.effigies.remove(pos) != null) {
            this.setDirty();
        }
    }
    private static final String DATA_NAME = "dark_effigies";
    public static DarkEffigyData get(ServerLevel level) {
        return level.getServer()
                .getLevel(Level.OVERWORLD)
                .getDataStorage()
                .computeIfAbsent(FACTORY, DATA_NAME);
    }

}
