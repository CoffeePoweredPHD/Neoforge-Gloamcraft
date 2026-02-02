package net.coffeepowered.gloamcraft;

import net.coffeepowered.gloamcraft.block.ModBlocks;
import net.coffeepowered.gloamcraft.block.entity.ModBlockEntities;
import net.coffeepowered.gloamcraft.block.entity.renderer.DarkEffigyBlockEntityRenderer;
import net.coffeepowered.gloamcraft.item.ModCreativeModeTabs;
import net.coffeepowered.gloamcraft.item.ModItems;
import net.coffeepowered.gloamcraft.network.ModNetworking;
import net.coffeepowered.gloamcraft.modsavedata.ModAttachments;
import net.coffeepowered.gloamcraft.screen.ModMenuTypes;
import net.coffeepowered.gloamcraft.screen.custom.DarkEffigyScreen;
import net.coffeepowered.gloamcraft.util.ModSounds;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Gloamcraft.MOD_ID)
public class Gloamcraft {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "gloamcraft";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();


    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Gloamcraft(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);


        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        ModCreativeModeTabs.register(modEventBus);

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        ModMenuTypes.register(modEventBus);
        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(ModNetworking::register);
        ModSounds.SOUND_EVENTS.register(modEventBus);

        ModAttachments.ATTACHMENT.register(modEventBus);



        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }




    // COMMON SETUP
    private void commonSetup(FMLCommonSetupEvent event) {

    }

    // CREATIVE MODE TAB
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS){
            //event.accept(ModBlocks.DARK_EFFIGY);
        }

    }


    // ON STARTUP
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }



        @SubscribeEvent
        public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.DARKEFFIGY_BE.get(), DarkEffigyBlockEntityRenderer::new);
        }

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenuTypes.DARKEFFIGY_MENU.get(), DarkEffigyScreen::new);
        }
    }
}
