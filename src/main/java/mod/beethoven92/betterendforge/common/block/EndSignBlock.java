package mod.beethoven92.betterendforge.common.block;

import mod.beethoven92.betterendforge.common.tileentity.ESignTileEntity;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SOpenSignMenuPacket;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EndSignBlock extends AbstractSignBlock {
	public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_0_15;
	public static final BooleanProperty FLOOR = BooleanProperty.create("floor");
	private static final VoxelShape[] WALL_SHAPES = new VoxelShape[] {
			Block.makeCuboidShape(0.0D, 4.5D, 14.0D, 16.0D, 12.5D, 16.0D),
			Block.makeCuboidShape(0.0D, 4.5D, 0.0D, 2.0D, 12.5D, 16.0D),
			Block.makeCuboidShape(0.0D, 4.5D, 0.0D, 16.0D, 12.5D, 2.0D),
			Block.makeCuboidShape(14.0D, 4.5D, 0.0D, 16.0D, 12.5D, 16.0D) };

	public EndSignBlock(AbstractBlock.Properties builder) {
		super(builder, WoodType.OAK);
		this.setDefaultState(
				this.stateContainer.getBaseState().with(ROTATION, 0).with(FLOOR, true).with(WATERLOGGED, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(ROTATION, FLOOR, WATERLOGGED);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return state.get(FLOOR) ? SHAPE : WALL_SHAPES[state.get(ROTATION) >> 2];
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new ESignTileEntity();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player,
			Hand hand, BlockRayTraceResult hit) {
		ItemStack itemStack = player.getHeldItem(hand);
		boolean bl = itemStack.getItem() instanceof DyeItem && player.abilities.allowEdit;
		if (world.isRemote) {
			return bl ? ActionResultType.SUCCESS : ActionResultType.CONSUME;
		} else {
			TileEntity blockEntity = world.getTileEntity(pos);
			if (blockEntity instanceof ESignTileEntity) {
				ESignTileEntity signBlockEntity = (ESignTileEntity) blockEntity;
				if (bl) {
					boolean bl2 = signBlockEntity.setTextColor(((DyeItem) itemStack.getItem()).getDyeColor());
					if (bl2 && !player.isCreative()) {
						itemStack.shrink(1);
					}
				}
				return signBlockEntity.onActivate(player) ? ActionResultType.SUCCESS : ActionResultType.PASS;
			} else {
				return ActionResultType.PASS;
			}
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (placer instanceof PlayerEntity) {
			ESignTileEntity sign = (ESignTileEntity) world.getTileEntity(pos);
			if (!world.isRemote) {
				if (sign != null) sign.setEditor((PlayerEntity) placer);
				((ServerPlayerEntity) placer).connection.sendPacket(new SOpenSignMenuPacket(pos));
			} else if (sign != null) {
				sign.setEditable(true);
			}
		}
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState,
			IWorld world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(WATERLOGGED)) {
			world.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}

		return super.updatePostPlacement(state, facing, neighborState, world, pos, neighborPos);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		if (ctx.getFace() == Direction.UP) {
			FluidState fluidState = ctx.getWorld().getFluidState(ctx.getPos());
			return this.getDefaultState().with(FLOOR, true)
					.with(ROTATION, MathHelper.floor((180.0 + ctx.getPlacementYaw() * 16.0 / 360.0) + 0.5 - 12) & 15)
					.with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
		} else if (ctx.getFace() != Direction.DOWN) {
			BlockState blockState = this.getDefaultState();
			FluidState fluidState = ctx.getWorld().getFluidState(ctx.getPos());
			IWorld worldView = ctx.getWorld();
			BlockPos blockPos = ctx.getPos();
			Direction[] directions = ctx.getNearestLookingDirections();
			Direction[] var7 = directions;
			int var8 = directions.length;

			for (int var9 = 0; var9 < var8; ++var9) {
				Direction direction = var7[var9];
				if (direction.getAxis().isHorizontal()) {
					Direction direction2 = direction.getOpposite();
					int rot = MathHelper.floor((180.0 + direction2.getHorizontalAngle() * 16.0 / 360.0) + 0.5 + 4) & 15;
					blockState = blockState.with(ROTATION, rot);
					if (blockState.isValidPosition(worldView, blockPos)) {
						return blockState.with(FLOOR, false).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
					}
				}
			}
		}

		return null;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.with(ROTATION, rotation.rotate(state.get(ROTATION), 16));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.with(ROTATION, mirror.mirrorRotation(state.get(ROTATION), 16));
	}
}