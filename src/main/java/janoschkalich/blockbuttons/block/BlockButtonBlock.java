package janoschkalich.blockbuttons.block;

import janoschkalich.blockbuttons.Blockbuttons;
import janoschkalich.blockbuttons.block.entity.BlockButtonBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;

import static net.minecraft.block.PillarBlock.AXIS;

public class BlockButtonBlock extends BlockWithEntity {
    public static final BooleanProperty TRANSPARENT = BooleanProperty.of("transparent");
    public static final BooleanProperty UPDATE = BooleanProperty.of("update");
    public BlockButtonBlock(AbstractBlock.Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(TRANSPARENT, false).with(UPDATE, false).with(Properties.FACING, Direction.NORTH).with(AXIS, Direction.Axis.Y).with(Properties.POWERED, false));
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.FACING, ctx.getPlayerFacing().getOpposite()).with(AXIS, ctx.getSide().getAxis());
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, Blockbuttons.BLOCK_BUTTON_BLOCK_ENTITY, BlockButtonBlockEntity::tick);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.getMainHandStack().isEmpty()) {
            if (!world.isClient) {
                NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

                if (screenHandlerFactory != null) {
                    player.openHandledScreen(screenHandlerFactory);
                }
            }
        }
        else {
            powerOn(state, world, pos);
        }
        return ActionResult.SUCCESS;
    }

    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(Properties.POWERED) ? 15 : 0;
    }

    public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(Properties.POWERED) ? 15 : 0;
    }

    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if ((Boolean)state.get(Properties.POWERED)) {
            world.setBlockState(pos, (BlockState)state.with(Properties.POWERED, false), 3);
            world.updateNeighborsAlways(pos, this);
            world.emitGameEvent((Entity)null, GameEvent.BLOCK_DEACTIVATE, pos);
        }
    }

    public void powerOn(BlockState state, World world, BlockPos pos) {
        world.setBlockState(pos, state.with(Properties.POWERED, true), 3);
        world.updateNeighborsAlways(pos, this);
        world.createAndScheduleBlockTick(pos, this, 30);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockButtonBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return state.get(TRANSPARENT) ? BlockRenderType.INVISIBLE : BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.POWERED);
        builder.add(Properties.FACING);
        builder.add(TRANSPARENT);
        builder.add(UPDATE);
        builder.add(AXIS);
    }

    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BlockButtonBlockEntity) {
                if (world instanceof ServerWorld) {
                    ItemScatterer.spawn(world, pos, (BlockButtonBlockEntity)blockEntity);
                }
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
}
