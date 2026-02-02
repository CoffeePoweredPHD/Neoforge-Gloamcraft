package net.coffeepowered.gloamcraft.block;

import net.coffeepowered.gloamcraft.Gloamcraft;
import net.coffeepowered.gloamcraft.block.custom.DarkEffigyBlock;
import net.coffeepowered.gloamcraft.item.ModItems;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Gloamcraft.MOD_ID);


    public static final DeferredBlock<Block> DARK_EFFIGY = registerBlock("dark_effigy",
            (properties) -> new DarkEffigyBlock(properties
                    .strength(4f).sound(SoundType.WART_BLOCK).noOcclusion()));



    //_______________________________________________________________________________________________

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> function) {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, function);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.registerItem(name, (properties) -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
