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

package rocks.spud.mc.basedefense.client.renderer.block.surveillance;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import rocks.spud.mc.basedefense.BaseDefenseModification;
import rocks.spud.mc.basedefense.api.registry.annotation.client.BlockRendererType;
import rocks.spud.mc.basedefense.api.registry.annotation.common.RegistrationCriteria;
import rocks.spud.mc.basedefense.api.registry.criteria.SurveillanceFeatureCriteria;
import rocks.spud.mc.basedefense.common.block.surveillance.ControllerBlock;

/**
 * Provides a renderer for the controller block.
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
@BlockRendererType
@RegistrationCriteria (SurveillanceFeatureCriteria.class)
public class ControllerBlockRenderer implements ISimpleBlockRenderingHandler {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void renderInventoryBlock (Block block, int metadata, int modelId, RenderBlocks renderer) {
		renderer.renderBlockAsItem (block, 1, 0.0f);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		if (!(block instanceof ControllerBlock)) return false;
		ControllerBlock controllerBlock = ((ControllerBlock) block);

		renderer.setOverrideBlockTexture (controllerBlock.getTextureNormal ());
		renderer.renderStandardBlock (block, x, y, z);

		switch (world.getBlockMetadata (x, y, z)) {
			case 1:
				renderer.setOverrideBlockTexture (controllerBlock.getTextureActive ());
				renderer.renderStandardBlock (block, x, y, z);
				break;
			case 2:
				renderer.setOverrideBlockTexture (controllerBlock.getTextureConflict ());
				renderer.renderStandardBlock (block, x, y, z);
				break;
		}

		renderer.clearOverrideBlockTexture ();
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean shouldRender3DInInventory (int modelId) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getRenderId () {
		return BaseDefenseModification.getInstance ().getProxy ().getRegistry ().getRendererIdentifier (this.getClass ());
	}
}
