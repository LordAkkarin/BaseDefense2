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

package rocks.spud.mc.basedefense.client.renderer.block.item.surveillance;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import rocks.spud.mc.basedefense.api.registry.annotation.client.BlockItemRendererType;
import rocks.spud.mc.basedefense.api.registry.annotation.common.RegistrationCriteria;
import rocks.spud.mc.basedefense.api.registry.criteria.SurveillanceFeatureCriteria;
import rocks.spud.mc.basedefense.client.renderer.block.entity.surveillance.CameraBlockEntityRenderer;
import rocks.spud.mc.basedefense.common.block.surveillance.CameraBlock;

/**
 * Provides an item renderer for camera blocks.
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
@BlockItemRendererType (CameraBlock.class)
@RegistrationCriteria (SurveillanceFeatureCriteria.class)
public class CameraBlockItemRenderer implements IItemRenderer {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean handleRenderType (ItemStack item, ItemRenderType type) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean shouldUseRenderHelper (ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void renderItem (ItemRenderType type, ItemStack item, Object... data) {
		GL11.glPushMatrix ();
		{
			switch (type) {
				case ENTITY:
					this.renderItemEntity (item, data);
					break;
				case INVENTORY:
					this.renderItemInventory (item, data);
					break;
				case EQUIPPED:
				case EQUIPPED_FIRST_PERSON:
				default:
					this.renderItemEquipped (type, item, data);
			}
		}
		GL11.glPopMatrix ();
	}

	/**
	 * Renders an item entity (item on ground).
	 * @param itemStack The item stack.
	 * @param data The data.
	 */
	protected void renderItemEntity (ItemStack itemStack, Object... data) {
		Minecraft.getMinecraft ().renderEngine.bindTexture (new ResourceLocation ("basedefense2:textures/models/surveillance/camera_mount.png"));
		CameraBlockEntityRenderer.getModel ().renderPart ("Mount_Cube04");

		Minecraft.getMinecraft ().renderEngine.bindTexture (new ResourceLocation ("basedefense2:textures/models/surveillance/camera_base.png"));
		CameraBlockEntityRenderer.getModel ().renderPart ("Base_Cube01");

		Minecraft.getMinecraft ().renderEngine.bindTexture (new ResourceLocation ("basedefense2:textures/models/surveillance/camera_body.png"));
		CameraBlockEntityRenderer.getModel ().renderPart ("Body_Cube02");

		Minecraft.getMinecraft ().renderEngine.bindTexture (new ResourceLocation ("basedefense2:textures/models/surveillance/camera_lens.png"));
		CameraBlockEntityRenderer.getModel ().renderPart ("Lens_Cube03");
	}

	/**
	 * Renders an item stack in an inventory.
	 * @param itemStack The item stack.
	 * @param data The data.
	 */
	protected void renderItemInventory (ItemStack itemStack, Object... data) {
		GL11.glRotatef (180.0f, 0.0f, 1.0f, 0.0f);
		GL11.glScalef (0.25f, 0.25f, 0.25f);
		GL11.glTranslatef (-1.5f, 0.5f, 1.5f);

		Minecraft.getMinecraft ().renderEngine.bindTexture (new ResourceLocation ("basedefense2:textures/models/surveillance/camera_mount.png"));
		CameraBlockEntityRenderer.getModel ().renderPart ("Mount_Cube04");

		Minecraft.getMinecraft ().renderEngine.bindTexture (new ResourceLocation ("basedefense2:textures/models/surveillance/camera_base.png"));
		CameraBlockEntityRenderer.getModel ().renderPart ("Base_Cube01");

		Minecraft.getMinecraft ().renderEngine.bindTexture (new ResourceLocation ("basedefense2:textures/models/surveillance/camera_body.png"));
		CameraBlockEntityRenderer.getModel ().renderPart ("Body_Cube02");

		Minecraft.getMinecraft ().renderEngine.bindTexture (new ResourceLocation ("basedefense2:textures/models/surveillance/camera_lens.png"));
		CameraBlockEntityRenderer.getModel ().renderPart ("Lens_Cube03");
	}

	/**
	 * Renders an equipped item (in hand).
	 * @param renderType The renderer type.
	 * @param itemStack The item stack.
	 * @param data The data.
	 */
	protected void renderItemEquipped (ItemRenderType renderType, ItemStack itemStack, Object... data) {
		if (renderType == ItemRenderType.EQUIPPED) {
			GL11.glScalef (0.25f, 0.25f, 0.25f);
			GL11.glRotatef (25.0f, 0.0f, 1.0f, 0.0f);
			GL11.glTranslatef (2.0f, 2.0f, 5.0f);
		} else {
			GL11.glScalef (0.3f, 0.3f, 0.3f);
			GL11.glRotatef (-25.0f, 0.0f, 1.0f, 0.0f);
			GL11.glTranslatef (4.0f, 0.0f, 3.0f);

			Minecraft.getMinecraft ().renderEngine.bindTexture (new ResourceLocation ("basedefense2:textures/models/surveillance/camera_mount.png"));
			CameraBlockEntityRenderer.getModel ().renderPart ("Mount_Cube04");
		}

		Minecraft.getMinecraft ().renderEngine.bindTexture (new ResourceLocation ("basedefense2:textures/models/surveillance/camera_base.png"));
		CameraBlockEntityRenderer.getModel ().renderPart ("Base_Cube01");

		Minecraft.getMinecraft ().renderEngine.bindTexture (new ResourceLocation ("basedefense2:textures/models/surveillance/camera_body.png"));
		CameraBlockEntityRenderer.getModel ().renderPart ("Body_Cube02");

		Minecraft.getMinecraft ().renderEngine.bindTexture (new ResourceLocation ("basedefense2:textures/models/surveillance/camera_lens.png"));
		CameraBlockEntityRenderer.getModel ().renderPart ("Lens_Cube03");
	}
}
