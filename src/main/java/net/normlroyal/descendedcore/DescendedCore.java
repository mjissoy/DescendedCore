package net.normlroyal.descendedcore;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.normlroyal.descendedcore.content.entity.CoreEntities;
import net.normlroyal.descendedcore.content.item.CoreCreativeTabs;
import net.normlroyal.descendedcore.content.item.CoreItems;
import net.normlroyal.descendedcore.content.particle.CoreParticles;
import software.bernie.geckolib.GeckoLib;
import org.slf4j.Logger;

@Mod(DescendedCore.MOD_ID)
public class DescendedCore
{
    public static final String MOD_ID = "descendedcore";
    private static final Logger LOGGER = LogUtils.getLogger();
    public DescendedCore(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);

        GeckoLib.initialize();
        CoreCreativeTabs.register(modEventBus);
        CoreItems.register(modEventBus);
        CoreEntities.ENTITY_TYPES.register(modEventBus);
        CoreParticles.PARTICLES.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }
}
