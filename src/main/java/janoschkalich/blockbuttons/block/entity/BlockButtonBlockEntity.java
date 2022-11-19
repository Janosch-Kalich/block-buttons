package janoschkalich.blockbuttons.block.entity;

import janoschkalich.blockbuttons.Blockbuttons;
import janoschkalich.blockbuttons.Screenhandler.BlockButtonScreenHandler;
import janoschkalich.blockbuttons.block.BlockButtonBlock;
import janoschkalich.blockbuttons.helper.ImplementedInventory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import javax.swing.text.html.BlockView;
import java.util.Objects;

public class BlockButtonBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    public final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public BlockButtonBlockEntity(BlockPos pos, BlockState state) {
        super(Blockbuttons.BLOCK_BUTTON_BLOCK_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, BlockButtonBlockEntity be) {

    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new BlockButtonScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
    }

    @Override
    public void onClose(PlayerEntity player) {
        if (!player.isSpectator()) {
            assert this.world != null;
            world.setBlockState(this.pos, world.getBlockState(this.pos).with(BlockButtonBlock.UPDATE, !world.getBlockState(this.pos).get(BlockButtonBlock.UPDATE)));
            if(Block.getBlockFromItem(this.getItems().get(0).getItem()).getDefaultState().isFullCube(world, this.getPos()) || Block.getBlockFromItem(this.getItems().get(0).getItem()).getDefaultState().isOpaque()) {
                world.setBlockState(this.pos, world.getBlockState(this.pos).with(BlockButtonBlock.TRANSPARENT, true));
            }
            else {
                world.setBlockState(this.pos, world.getBlockState(this.pos).with(BlockButtonBlock.TRANSPARENT, false));
            }
        }
    }
}
