package com.divinearsenal;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DivineArsenal.MOD_ID)
public class DivineArsenal {
    public static final String MOD_ID = "divinearsenal";
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final RegistryObject<Item> LONGINUS = ITEMS.register("longinus", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BABYLON = ITEMS.register("babylon", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> APOLLO = ITEMS.register("apollo", () -> new Item(new Item.Properties().stacksTo(1)));
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
    static {
        TABS.register("divine_arsenal", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.divinearsenal"))
            .icon(() -> new ItemStack(LONGINUS.get()))
            .displayItems((params, output) -> {
                output.accept(LONGINUS.get());
                output.accept(BABYLON.get());
                output.accept(APOLLO.get());
            }).build());
    }
    public DivineArsenal(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
        ITEMS.register(bus);
        TABS.register(bus);
    }
}
