/*
 * Copyright 2015 Johannes Donath <johannesd@torchmind.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rocks.spud.mc.basedefense.common.block.surveillance;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import rocks.spud.mc.basedefense.BaseDefenseModification;
import rocks.spud.mc.basedefense.api.registry.annotation.common.BlockType;
import rocks.spud.mc.basedefense.api.registry.annotation.common.RegistrationCriteria;
import rocks.spud.mc.basedefense.api.registry.criteria.SurveillanceFeatureCriteria;
import rocks.spud.mc.basedefense.common.block.entity.surveillance.CameraBlockEntity;
import rocks.spud.mc.basedefense.common.item.block.surveillance.CameraBlockItem;

/**
 * Provides a detector implementation.
 *
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
@BlockType (value = CameraBlock.IDENTIFIER, item = CameraBlockItem.class)
@RegistrationCriteria (SurveillanceFeatureCriteria.class)
public class CameraBlock extends Block implements ITileEntityProvider {

	/**
	 * Defines the block identifier.
	 */
	public static final String IDENTIFIER = "surveillance_camera";

	/**
	 * Constructs a new CameraBlock.
	 */
	protected CameraBlock () {
		super (Material.iron);

		this.setBlockName (BaseDefenseModification.getTranslation ("surveillance", "camera"));
		this.setBlockTextureName ("basedefense2:surveillance/camera");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileEntity createNewTileEntity (World p_149915_1_, int p_149915_2_) {
		return (new CameraBlockEntity ());
	}

	protected AxisAlignedBB getBoundingBox (World world, int x, int y, int z, int metadata) {
		int direction = (metadata >> 1);

		switch (direction) {
			case 0:
			case 2:
				return AxisAlignedBB.getBoundingBox ((x + 0.20d), (y + 0.3d), z, (x + 0.80d), (y + 0.85d), (z + 1.0d));
			case 1:
			case 3:
				return AxisAlignedBB.getBoundingBox (x, (y + 0.3d), (z + 0.20d), (x + 1.0d), (y + 0.85d), (z + 0.80d));
			case 5:
				return AxisAlignedBB.getBoundingBox ((x + 0.25d), (y + 0.4d), (z + 0.25d), (x + 0.75d), (y + 1.0d), (z + 0.75d));
			default:
				return AxisAlignedBB.getBoundingBox ((x + 0.25d), y, (z + 0.25d), (x + 0.75d), (y + 0.6d), (z + 0.75d));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean renderAsNormalBlock () {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean shouldSideBeRendered (IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isBlockSolid (IBlockAccess p_149747_1_, int p_149747_2_, int p_149747_3_, int p_149747_4_, int p_149747_5_) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool (World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
		return this.getBoundingBox (p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_, p_149668_1_.getBlockMetadata (p_149668_2_, p_149668_3_, p_149668_4_));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool (World p_149633_1_, int p_149633_2_, int p_149633_3_, int p_149633_4_) {
		return this.getBoundingBox (p_149633_1_, p_149633_2_, p_149633_3_, p_149633_4_, p_149633_1_.getBlockMetadata (p_149633_2_, p_149633_3_, p_149633_4_));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOpaqueCube () {
		return false;
	}
}
