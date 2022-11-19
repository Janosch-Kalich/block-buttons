package janoschkalich.blockbuttons.client.BlockEntityRenderer;

import janoschkalich.blockbuttons.block.BlockButtonBlock;
import janoschkalich.blockbuttons.block.entity.BlockButtonBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;

import javax.swing.*;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class BlockButtonBlockEntityRenderer implements BlockEntityRenderer<BlockButtonBlockEntity> {
    public BlockButtonBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(BlockButtonBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumer, int light, int overlay) {
        if (blockEntity.isRemoved() || !blockEntity.getWorld().getBlockState(blockEntity.getPos()).get(BlockButtonBlock.TRANSPARENT)) return;
        Block mimicBlock = Block.getBlockFromItem(blockEntity.getStack(0).getItem());
        if (!mimicBlock.getDefaultState().contains(StairsBlock.FACING)) {
            BlockRotation block_rotation = BlockRotation.NONE;
            switch (blockEntity.getWorld().getBlockState(blockEntity.getPos()).get(Properties.FACING)) {
                case EAST: {
                    block_rotation = BlockRotation.CLOCKWISE_90;
                    break;
                }
                case SOUTH: {
                    block_rotation = BlockRotation.CLOCKWISE_180;
                    break;
                }
                case WEST: {
                    block_rotation = BlockRotation.COUNTERCLOCKWISE_90;
                    break;
                }
                default: {
                    break;
                }
            }

            if (mimicBlock.getDefaultState().contains(PillarBlock.AXIS)) {
                MinecraftClient.getInstance().getBlockRenderManager().renderBlock(mimicBlock.getDefaultState().rotate(block_rotation).with(PillarBlock.AXIS, blockEntity.getWorld().getBlockState(blockEntity.getPos()).get(PillarBlock.AXIS)), blockEntity.getPos(), blockEntity.getWorld(), matrices, vertexConsumer.getBuffer(RenderLayers.getBlockLayer(blockEntity.getCachedState())), false, Random.create());
            }
            else {
                MinecraftClient.getInstance().getBlockRenderManager().renderBlock(mimicBlock.getDefaultState().rotate(block_rotation), blockEntity.getPos(), blockEntity.getWorld(), matrices, vertexConsumer.getBuffer(RenderLayers.getBlockLayer(blockEntity.getCachedState())), false, Random.create());
            }
        }
        else {
            MinecraftClient.getInstance().getBlockRenderManager().renderBlock(mimicBlock.getDefaultState().with(StairsBlock.FACING, blockEntity.getWorld().getBlockState(blockEntity.getPos()).get(Properties.FACING).getOpposite()), blockEntity.getPos(), blockEntity.getWorld(), matrices, vertexConsumer.getBuffer(RenderLayers.getBlockLayer(blockEntity.getCachedState())), false, Random.create());
        }
    }
}
