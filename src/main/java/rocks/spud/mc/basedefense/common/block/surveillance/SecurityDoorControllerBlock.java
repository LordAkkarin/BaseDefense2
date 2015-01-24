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
import net.minecraft.world.World;
import rocks.spud.mc.basedefense.BaseDefenseModification;
import rocks.spud.mc.basedefense.api.registry.annotation.common.BlockType;
import rocks.spud.mc.basedefense.api.registry.annotation.common.RegistrationCriteria;
import rocks.spud.mc.basedefense.api.registry.criteria.SurveillanceFeatureCriteria;
import rocks.spud.mc.basedefense.client.renderer.block.surveillance.SecurityDoorControllerBlockRenderer;
import rocks.spud.mc.basedefense.common.block.entity.surveillance.SecurityDoorControllerBlockEntity;
import rocks.spud.mc.basedefense.common.item.block.surveillance.SecurityDoorControllerBlockItem;

/**
 * Provides a door controller block implementation.
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
@BlockType (value = SecurityDoorControllerBlock.IDENTIFIER, item = SecurityDoorControllerBlockItem.class)
@RegistrationCriteria (SurveillanceFeatureCriteria.class)
public class SecurityDoorControllerBlock extends Block implements ITileEntityProvider {

	/**
	 * Defines the block identifier.
	 */
	public static final String IDENTIFIER = "surveillance_security_door_controller";

	/**
	 * Stores the normal texture.
	 */
	@Getter
	private IIcon textureNormal = null;

	/**
	 * Stores the active texture.
	 */
	@Getter
	private IIcon textureActive = null;

	/**
	 * Stores the face texture.
	 */
	@Getter
	private IIcon textureFace = null;

	/**
	 * Constructs a new SecurityDoorControllerBlock.
	 */
	public SecurityDoorControllerBlock () {
		super (Material.iron);

		this.setBlockName (BaseDefenseModification.getTranslation ("surveillance", "security_door_controller"));
		this.setBlockTextureName ("basedefense2:surveillance/security_door_controller");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileEntity createNewTileEntity (World p_149915_1_, int p_149915_2_) {
		return (new SecurityDoorControllerBlockEntity ());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getRenderType () {
		return BaseDefenseModification.getInstance ().getProxy ().getRegistry ().getRendererIdentifier (SecurityDoorControllerBlockRenderer.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerBlockIcons (IIconRegister p_149651_1_) {
		super.registerBlockIcons (p_149651_1_);

		this.textureNormal = p_149651_1_.registerIcon ("basedefense2:surveillance/security_door_controller");
		this.textureActive = p_149651_1_.registerIcon ("basedefense2:surveillance/security_door_controller_active");
		this.textureFace = p_149651_1_.registerIcon ("basedefense2:surveillance/security_door_controller_face");
	}
}
