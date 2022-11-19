package janoschkalich.blockbuttons;

import janoschkalich.blockbuttons.Screenhandler.BlockButtonScreenHandler;
import janoschkalich.blockbuttons.block.BlockButtonBlock;
import janoschkalich.blockbuttons.block.entity.BlockButtonBlockEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Blockbuttons implements ModInitializer {
    public static final Block BLOCK_BUTTON_BLOCK;
    public static final BlockItem BLOCK_BUTTON_ITEM;
    public static final BlockEntityType<BlockButtonBlockEntity> BLOCK_BUTTON_BLOCK_ENTITY;
    public static final ScreenHandlerType<BlockButtonScreenHandler> BLOCK_BUTTON_SCREEN_HANDLER;

    static {
        BLOCK_BUTTON_BLOCK = Registry.register(Registry.BLOCK, new Identifier("blockbuttons", "block_button"), new BlockButtonBlock(FabricBlockSettings.of(Material.GLASS).hardness(1.0f).nonOpaque()));
        BLOCK_BUTTON_ITEM = Registry.register(Registry.ITEM, new Identifier("blockbuttons", "block_button"), new BlockItem(BLOCK_BUTTON_BLOCK, new FabricItemSettings().group(ItemGroup.REDSTONE)));
        BLOCK_BUTTON_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("blockbuttons", "block_button"), FabricBlockEntityTypeBuilder.create(BlockButtonBlockEntity::new, BLOCK_BUTTON_BLOCK).build(null));
        BLOCK_BUTTON_SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER, new Identifier("blockbuttons", "block_button"), new ScreenHandlerType<>(BlockButtonScreenHandler::new));
    }

    @Override
    public void onInitialize() {
    }
}
