package blusunrize.immersiveengineering.common.blocks.metal;

import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;

public class TileEntityRedstoneBreaker extends TileEntityBreakerSwitch implements ITickable
{
	@Override
	public void update()
	{
		if (!worldObj.isRemote&&(worldObj.isBlockIndirectlyGettingPowered(getPos())>0)==active)
		{
			active = !active;
			ImmersiveNetHandler.INSTANCE.resetCachedIndirectConnections();
		}
	}
	@Override
	protected boolean canTakeHV()
	{
		return true;
	}

	@Override
	public boolean allowEnergyToPass(Connection con)
	{
		return active;
	}

	@Override
	public float[] getBlockBounds()
	{
		Vec3d start = new Vec3d(0,.125f,0);
		Vec3d end = new Vec3d(1,.875f,1);
		Matrix4 mat = new Matrix4(facing);
		mat.translate(.5, .5, 0).rotate(Math.PI/2*rotation, 0, 0, 1).translate(-.5, -.5, 0);
		start = mat.apply(start);
		end = mat.apply(end);
		return new float[]{(float) start.xCoord, (float) start.yCoord, (float) start.zCoord,
				(float) end.xCoord, (float) end.yCoord, (float) end.zCoord};
	}

	@Override
	public Vec3d getRaytraceOffset(IImmersiveConnectable link)
	{
		Matrix4 mat = new Matrix4(facing);
		mat.translate(.5, .5, 0).rotate(Math.PI/2*rotation, 0, 0, 1).translate(-.5, -.5, 0);
		Vec3d ret = mat.apply(new Vec3d(.5, .5, .99));
		return ret;
	}
	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		Matrix4 mat = new Matrix4(facing);
		mat.translate(.5, .5, 0).rotate(Math.PI/2*rotation, 0, 0, 1).translate(-.5, -.5, 0);
		if (endOfLeftConnection==null)
			calculateLeftConn(mat);
		boolean isLeft = con.end==endOfLeftConnection||con.start==endOfLeftConnection;
		Vec3d ret = mat.apply(isLeft?new Vec3d(.125, .5, 1.03125):new Vec3d(.875, .5, 1.03125));
		return ret;
	}

	@Override
	public int getWeakRSOutput(IBlockState state, EnumFacing side)
	{
		return 0;
	}
	@Override
	public int getStrongRSOutput(IBlockState state, EnumFacing side)
	{
		return 0;
	}
	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		return false;
	}
}