package mod.codewarrior.treestumps;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mod(modid = Treestumps.MODID, version = Treestumps.VERSION)
public class Treestumps
{
    public static final String MODID = "treestumps";
    public static final String VERSION = "1.0";
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void breakSpeed(PlayerEvent.BreakSpeed breakSpeedEvent) {
        IBlockState state = breakSpeedEvent.getState();
        EntityPlayer player = breakSpeedEvent.getEntityPlayer();
        World world = player.getEntityWorld();
        BlockPos pos = breakSpeedEvent.getPos();
        ItemStack item = state.getBlock().getPickBlock(state, null, world, pos, null);
        IBlockState below = world.getBlockState(pos.offset(EnumFacing.DOWN));
        IBlockState above = world.getBlockState(pos.offset(EnumFacing.UP));
        int logWood = OreDictionary.getOreID("logWood");

        int[] ids;

        if (item != null) {
            ids = OreDictionary.getOreIDs(item);

            // Make a log harder if it is above dirt.
            if (ArrayUtils.contains(ids, logWood)) {
                if (below.getBlock() == Blocks.DIRT) {
                    breakSpeedEvent.setNewSpeed(breakSpeedEvent.getNewSpeed() * 0.05f);
                }
            }
        }

        item = above.getBlock().getPickBlock(above, null, world, pos.offset(EnumFacing.UP), null);
        if(item != null) {
            ids = OreDictionary.getOreIDs(item);

            // Make dirt harder if it is below a log.
            if (ArrayUtils.contains(ids, logWood)) {
                if (state.getBlock() == Blocks.DIRT) {
                    breakSpeedEvent.setNewSpeed(breakSpeedEvent.getNewSpeed() * 0.01f);
                }
            }
        }
    }

}
