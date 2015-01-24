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
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import rocks.spud.mc.basedefense.BaseDefenseModification;
import rocks.spud.mc.basedefense.api.registry.annotation.client.BlockRendererType;
import rocks.spud.mc.basedefense.api.registry.annotation.common.RegistrationCriteria;
import rocks.spud.mc.basedefense.api.registry.criteria.SurveillanceFeatureCriteria;
import rocks.spud.mc.basedefense.common.block.entity.surveillance.SecurityDoorControllerBlockEntity;
import rocks.spud.mc.basedefense.common.block.surveillance.SecurityDoorControllerBlock;

/**
 * Provides a
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
@BlockRendererType
@RegistrationCriteria (SurveillanceFeatureCriteria.class)
public class SecurityDoorControllerBlockRenderer implements ISimpleBlockRenderingHandler {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void renderInventoryBlock (Block block, int metadata, int modelId, RenderBlocks renderer) {
		if (!(block instanceof SecurityDoorControllerBlock)) return;
		SecurityDoorControllerBlock controllerBlock = ((SecurityDoorControllerBlock) block);
		Tessellator tessellator = Tessellator.instance;

		tessellator.startDrawingQuads ();
		tessellator.setNormal (-1.0f, 0.0f, 0.0f);
		tessellator.setColorOpaque_I (0xFFFFFF);
		renderer.renderFaceXNeg (block, 0.0d, 0.0d, 0.0d, controllerBlock.getTextureNormal ());
		tessellator.draw ();

		tessellator.startDrawingQuads ();
		tessellator.setNormal (1.0f, 0.0f, 0.0f);
		tessellator.setColorOpaque_I (0xFFFFFF);
		renderer.renderFaceXPos (block, 0.0d, 0.0d, 0.0d, controllerBlock.getTextureNormal ());
		tessellator.draw ();

		tessellator.startDrawingQuads ();
		tessellator.setNormal (0.0f, -1.0f, 0.0f);
		tessellator.setColorOpaque_I (0xFFFFFF);
		renderer.renderFaceYNeg (block, 0.0d, 0.0d, 0.0d, controllerBlock.getTextureNormal ());
		tessellator.draw ();

		tessellator.startDrawingQuads ();
		tessellator.setNormal (0.0f, 1.0f, 0.0f);
		tessellator.setColorOpaque_I (0xFFFFFF);
		renderer.renderFaceYPos (block, 0.0d, 0.0d, 0.0d, controllerBlock.getTextureNormal ());
		tessellator.draw ();

		tessellator.startDrawingQuads ();
		tessellator.setNormal (0.0f, 0.0f, -1.0f);
		tessellator.setColorOpaque_I (0xFFFFFF);
		renderer.renderFaceZNeg (block, 0.0d, 0.0d, 0.0d, controllerBlock.getTextureNormal ());
		tessellator.draw ();

		tessellator.startDrawingQuads ();
		tessellator.setNormal (0.0f, 0.0f, 1.0f);
		tessellator.setColorOpaque_I (0xFFFFFF);
		renderer.renderFaceZPos (block, 0.0d, 0.0d, 0.0d, controllerBlock.getTextureNormal ());
		tessellator.draw ();

		renderer.clearOverrideBlockTexture ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		if (!(block instanceof SecurityDoorControllerBlock)) return false;
		SecurityDoorControllerBlock controllerBlock = ((SecurityDoorControllerBlock) block);

		Tessellator tessellator = Tessellator.instance;
		renderer.setOverrideBlockTexture (controllerBlock.getTextureNormal ());
		renderer.renderStandardBlock (block, x, y, z);

		SecurityDoorControllerBlockEntity entity = ((SecurityDoorControllerBlockEntity) world.getTileEntity (x, y, z));

		if (entity != null) {
			// render output face
			renderer.setOverrideBlockTexture (controllerBlock.getTextureFace ());
			switch (entity.getRotation ()) {
				case UP:
					tessellator.setNormal (0.0f, 1.0f, 0.0f);
					renderer.renderFaceYPos (block, x, y, z, controllerBlock.getTextureFace ());
					break;
				case DOWN:
					tessellator.setNormal (0.0f, -1.0f, 0.0f);
					renderer.renderFaceYNeg (block, x, y, z, controllerBlock.getTextureFace ());
					break;
				case NORTH:
					tessellator.setNormal (0.0f, 0.0f, -1.0f);
					renderer.renderFaceZNeg (block, x, y, z, controllerBlock.getTextureFace ());
					break;
				case SOUTH:
					tessellator.setNormal (0.0f, 0.0f, 1.0f);
					renderer.renderFaceZPos (block, x, y, z, controllerBlock.getTextureFace ());
					break;
				case WEST:
					tessellator.setNormal (-1.0f, 0.0f, 0.0f);
					renderer.renderFaceXNeg (block, x, y, z, controllerBlock.getTextureFace ());
					break;
				case EAST:
					tessellator.setNormal (1.0f, 0.0f, 0.0f);
					renderer.renderFaceXPos (block, x, y, z, controllerBlock.getTextureFace ());
					break;
			}

			// render active animation on top
			if (entity.isActive ()) {
				renderer.setOverrideBlockTexture (controllerBlock.getTextureActive ());
				renderer.renderStandardBlock (block, x, y, z);
			}
		}

		renderer.clearOverrideBlockTexture ();

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean shouldRender3DInInventory (int modelId) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getRenderId () {
		return BaseDefenseModification.getInstance ().getProxy ().getRegistry ().getRendererIdentifier (this.getClass ());
	}
}
