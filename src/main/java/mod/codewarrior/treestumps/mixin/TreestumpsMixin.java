package mod.codewarrior.treestumps.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LogBlock;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Facing;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Block.class)
public class TreestumpsMixin {
	@Inject(at = @At("RETURN"), method = "calcBlockBreakingDelta", cancellable = true)
	private void onCalcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos, CallbackInfoReturnable<Float> info) {

		if (isLog(world, pos) && isDirt(world, pos.down()))
		{
			info.setReturnValue(info.getReturnValue() * 0.05f);
		}
		else if (isLog(world, pos.up()) && isDirt(world, pos))
		{
			info.setReturnValue(info.getReturnValue() * 0.01f);
		}

	}

	private boolean isLog(BlockView world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if(state.getBlock() instanceof LogBlock) {
			if (state.getProperties().contains(LogBlock.AXIS)) {
				return state.get(LogBlock.AXIS).equals(Facing.Axis.Y);
			}
		}

		return state.matches(BlockTags.LOGS);
	}

	private boolean isDirt(BlockView world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		return state.getMaterial() == Material.EARTH || state.getMaterial() == Material.ORGANIC;
	}
}
