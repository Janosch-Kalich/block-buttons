package janoschkalich.blockbuttons.client;

import janoschkalich.blockbuttons.Blockbuttons;
import janoschkalich.blockbuttons.HandledScreen.BlockButtonScreen;
import janoschkalich.blockbuttons.client.BlockEntityRenderer.BlockButtonBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;

import java.rmi.registry.Registry;

import static janoschkalich.blockbuttons.Blockbuttons.BLOCK_BUTTON_BLOCK_ENTITY;
import static janoschkalich.blockbuttons.Blockbuttons.BLOCK_BUTTON_SCREEN_HANDLER;

@Environment(EnvType.CLIENT)
public class BlockbuttonsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(Blockbuttons.BLOCK_BUTTON_BLOCK, RenderLayer.getCutout());

        HandledScreens.register(BLOCK_BUTTON_SCREEN_HANDLER, BlockButtonScreen::new);
        BlockEntityRendererRegistry.register(BLOCK_BUTTON_BLOCK_ENTITY, BlockButtonBlockEntityRenderer::new);
    }
}
