package net.coffeepowered.gloamcraft.block.entity;

import net.coffeepowered.gloamcraft.Gloamcraft;
import net.coffeepowered.gloamcraft.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Gloamcraft.MOD_ID);

    public static final Supplier<BlockEntityType<DarkEffigyBlockEntity>> DARKEFFIGY_BE =
            BLOCK_ENTITIES.register("darkeffigy_be", () -> new BlockEntityType<>(
                    DarkEffigyBlockEntity::new, ModBlocks.DARK_EFFIGY.get()));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}