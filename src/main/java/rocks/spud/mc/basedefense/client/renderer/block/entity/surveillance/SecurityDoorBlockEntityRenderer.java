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

package rocks.spud.mc.basedefense.client.renderer.block.entity.surveillance;

import lombok.Getter;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import rocks.spud.mc.basedefense.api.registry.annotation.client.BlockEntityRendererType;
import rocks.spud.mc.basedefense.common.block.entity.surveillance.SecurityDoorBlockEntity;

/**
 * Provides a block entity renderer for security doors.
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
@BlockEntityRendererType (SecurityDoorBlockEntity.class)
public class SecurityDoorBlockEntityRenderer extends TileEntitySpecialRenderer {

	/**
	 * Stores the model.
	 */
	@Getter
	private static final IModelCustom model = AdvancedModelLoader.loadModel (new ResourceLocation ("basedefense2", "models/surveillance/security_door.obj"));

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void renderTileEntityAt (TileEntity p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_) {
		GL11.glPushMatrix ();
		{
			int metadata = p_147500_1_.getWorldObj ().getBlockMetadata (p_147500_1_.xCoord, p_147500_1_.yCoord, p_147500_1_.zCoord);

			GL11.glTranslated (p_147500_2_, p_147500_4_, p_147500_6_);
			GL11.glScalef (0.5f, 0.5f, 0.5f);
			GL11.glTranslatef (1f, 1f, 1f);

			GL11.glRotatef (90.0f, (metadata > 4 ? 1.0f : 0.0f), (metadata > 2 && metadata < 4 ? 1.0f : 0.0f), 0.0f);

			this.bindTexture (new ResourceLocation ("basedefense2", "textures/blocks/surveillance/security_door.png"));
			getModel ().renderAll ();
		}
		GL11.glPopMatrix ();
	}
}
