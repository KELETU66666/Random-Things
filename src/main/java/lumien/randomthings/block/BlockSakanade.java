package lumien.randomthings.block;

import java.util.List;
import java.util.Random;

import lumien.randomthings.item.ItemIngredient;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.potion.ModPotions;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSakanade extends BlockBase implements IShearable
{
	public BlockSakanade()
	{
		super("sakanade", Material.plants);

		this.setStepSound(soundTypeGrass);
		this.setTickRandomly(true);

		this.setBlockBounds(0, 1f - 0.0625F, 0, 1, 1, 1);
		//this.setCreativeTab(null)
	}

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	@Override
	public java.util.List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		return java.util.Arrays.asList(new ItemStack(ModItems.ingredients, 1, ItemIngredient.INGREDIENT.SAKANADE_SPORES.id));
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return null;
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(worldIn, pos, state, rand);

		if (!worldIn.isRemote)
		{
			AxisAlignedBB bb = new AxisAlignedBB(pos, pos.add(1, 1, 1)).expand(2, -2, 2);

			List<EntityLivingBase> entityList = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, bb);

			for (EntityLivingBase entity : entityList)
			{
				entity.addPotionEffect(new PotionEffect(ModPotions.collapse.id, 200));
			}


			this.checkAndDropBlock(worldIn, pos, state);
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
	{
		return null;
	}

	public boolean isOpaqueCube()
	{
		return false;
	}

	public boolean isFullCube()
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.CUTOUT;
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
		this.checkAndDropBlock(worldIn, pos, state);
	}

	@Override
	public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		super.randomDisplayTick(worldIn, pos, state, rand);
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return super.canPlaceBlockAt(worldIn, pos) && canBlockStay(worldIn, pos, this.getDefaultState());
	}

	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
	{
		Block soil = worldIn.getBlockState(pos.up()).getBlock();
		return soil == Blocks.stone;
	}

	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!this.canBlockStay(worldIn, pos, state))
		{
			worldIn.setBlockState(pos, Blocks.air.getDefaultState(), 3);
		}
	}
}
