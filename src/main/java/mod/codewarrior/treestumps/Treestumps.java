package mod.codewarrior.treestumps;

import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
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
public class Treestumps {
    public static final String MODID = "treestumps";
    public static final String VERSION = "$$VERSION$$";

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    private boolean isLog(World world, BlockPos pos) {
        int logWood = OreDictionary.getOreID("logWood");
        IBlockState state = world.getBlockState(pos);
        if(state.getBlock() instanceof BlockLog) {
            if (state.getPropertyKeys().contains(BlockLog.LOG_AXIS)) {
                return state.getValue(BlockLog.LOG_AXIS).equals(BlockLog.EnumAxis.Y);
            }
        }

        ItemStack item = new ItemStack(state.getBlock(), 1, state.getBlock().damageDropped(state));
        if (!item.isEmpty()) {
            int[] ids = OreDictionary.getOreIDs(item);
            if (ArrayUtils.contains(ids, logWood)) {
                return true;
            }
        }
        return false;
    }

    private boolean isDirt(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getMaterial() == Material.GROUND || state.getMaterial() == Material.GRASS;
    }

    @SubscribeEvent
    public void breakSpeed(PlayerEvent.BreakSpeed breakSpeedEvent) {
        // IBlockState state = breakSpeedEvent.getState();
        // C&B lies about this state. Get the state from the world instead.

        EntityPlayer player = breakSpeedEvent.getEntityPlayer();
        World world = player.getEntityWorld();
        BlockPos pos = breakSpeedEvent.getPos();

        if (isLog(world, pos) && isDirt(world, pos.down()))
        {
            breakSpeedEvent.setNewSpeed(breakSpeedEvent.getNewSpeed() * 0.05f);
        }
        else if (isLog(world, pos.up()) && isDirt(world, pos))
        {
            breakSpeedEvent.setNewSpeed(breakSpeedEvent.getNewSpeed() * 0.01f);
        }

    }

}
