/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 22, 2015, 7:46:55 PM (GMT)]
 */
package vazkii.botania.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockModDoubleFlower extends BlockDoublePlant implements ILexiconable {

	private static final int COUNT = 8;

	IIcon[] doublePlantTopIcons, doublePlantBottomIcons;
	final int offset;

	public BlockModDoubleFlower(boolean second) {
		offset = second ? 8 : 0;
		setBlockName(LibBlockNames.DOUBLE_FLOWER + (second ? 2 : 1));
		setHardness(0F);
		setStepSound(soundTypeGrass);
		setTickRandomly(false);
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
	}

	@Override
	public Block setBlockName(String par1Str) {
		if(!par1Str.equals("doublePlant"))
			GameRegistry.registerBlock(this, ItemBlockWithMetadataAndName.class, par1Str);
		return super.setBlockName(par1Str);
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return null;
	}

	@Override
	public int damageDropped(int p_149692_1_) {
		return p_149692_1_ & 7;
	}

	@Override
	public void func_149889_c(World p_149889_1_, int p_149889_2_, int p_149889_3_, int p_149889_4_, int p_149889_5_, int p_149889_6_) {
		p_149889_1_.setBlock(p_149889_2_, p_149889_3_, p_149889_4_, this, p_149889_5_, p_149889_6_);
		p_149889_1_.setBlock(p_149889_2_, p_149889_3_ + 1, p_149889_4_, this, p_149889_5_ | 8, p_149889_6_);
	}

	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		p_149689_1_.setBlock(p_149689_2_, p_149689_3_ + 1, p_149689_4_, this, p_149689_6_.getItemDamage() | 8, 2);
	}

	@Override
	public boolean func_149851_a(World world, int x, int y, int z, boolean fuckifiknow) {
		return false;
	}

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z) {
		return true;
	}

	@Override
	public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(this, 1, world.getBlockMetadata(x, y, z) & 7));
		return ret;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
		return new ArrayList();
	}

	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return (func_149887_c(p_149691_2_) ? doublePlantTopIcons : doublePlantBottomIcons)[p_149691_2_ & 7];
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		int meta = world.getBlockMetadata(x, y, z);
		boolean top = func_149887_c(meta);
		if(top)
			meta = world.getBlockMetadata(x, y - 1, z);

		return (top ? doublePlantBottomIcons : doublePlantTopIcons)[meta & 7];
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		doublePlantTopIcons = new IIcon[COUNT];
		doublePlantBottomIcons = new IIcon[COUNT];
		for(int i = 0; i < COUNT; i++) {
			int off = offset(i);
			doublePlantTopIcons[i] = IconHelper.forName(register, "flower" + off + "Tall0");
			doublePlantBottomIcons[i] = IconHelper.forName(register, "flower" + off + "Tall1");
		}
	}

	@Override
	public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
		return 16777215;
	}

	@Override
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
		for(int i = 0; i < COUNT; ++i)
			p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
	}

	@Override
	public int getRenderType() {
		return LibRenderIDs.idDoubleFlower;
	}

	@Override
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
		int meta = par1World.getBlockMetadata(par2, par3, par4);
		float[] color = EntitySheep.fleeceColorTable[offset(meta & 7)];

		if(par5Random.nextDouble() < ConfigHandler.flowerParticleFrequency)
			Botania.proxy.sparkleFX(par1World, par2 + 0.3 + par5Random.nextFloat() * 0.5, par3 + 0.5 + par5Random.nextFloat() * 0.5, par4 + 0.3 + par5Random.nextFloat() * 0.5, color[0], color[1], color[2], par5Random.nextFloat(), 5);
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.flowers;
	}

	int offset(int meta) {
		return meta + offset;
	}

}
