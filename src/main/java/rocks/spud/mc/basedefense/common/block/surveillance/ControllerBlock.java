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

import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import rocks.spud.mc.basedefense.BaseDefenseModification;
import rocks.spud.mc.basedefense.api.registry.annotation.common.BlockType;
import rocks.spud.mc.basedefense.client.renderer.block.surveillance.ControllerBlockRenderer;
import rocks.spud.mc.basedefense.common.block.entity.surveillance.ControllerBlockEntity;

/**
 * Provides a block that acts as the core of the surveillance network.
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
@BlockType (ControllerBlock.IDENTIFIER)
public class ControllerBlock extends Block implements ITileEntityProvider {

	/**
	 * Defines the block name.
	 */
	public static final String IDENTIFIER = "surveillance_controller";

	/**
	 * Stores the normal texture (top and bottom).
	 */
	@Getter
	private IIcon textureNormal = null;

	/**
	 * Stores the active texture (top and bottom).
	 */
	@Getter
	private IIcon textureActive = null;

	/**
	 * Stores the conflict texture (top and bottom).
	 */
	@Getter
	private IIcon textureConflict = null;

	/**
	 * Constructs a new ControllerBlock.
	 */
	public ControllerBlock () {
		super (Material.iron);

		this.setBlockName (BaseDefenseModification.getTranslation ("surveillance", "controller"));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileEntity createNewTileEntity (World p_149915_1_, int p_149915_2_) {
		return (new ControllerBlockEntity ());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getLightValue (IBlockAccess world, int x, int y, int z) {
		return (world.getBlockMetadata (x, y, z) != 0 ? 13 : 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getRenderType () {
		return BaseDefenseModification.getInstance ().getProxy ().getRegistry ().getRendererIdentifier (ControllerBlockRenderer.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerBlockIcons (IIconRegister p_149651_1_) {
		this.textureNormal = p_149651_1_.registerIcon ("basedefense2:surveillance/controller");
		this.textureActive = p_149651_1_.registerIcon ("basedefense2:surveillance/controller_active");
		this.textureConflict = p_149651_1_.registerIcon ("basedefense2:surveillance/controller_conflict");
	}
}
