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
import rocks.spud.mc.basedefense.api.registry.annotation.common.RegistrationCriteria;
import rocks.spud.mc.basedefense.api.registry.criteria.SurveillanceFeatureCriteria;
import rocks.spud.mc.basedefense.common.block.entity.surveillance.CameraBlockEntity;

/**
 * Provides a renderer for camera block entities.
 *
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
@BlockEntityRendererType (CameraBlockEntity.class)
@RegistrationCriteria (SurveillanceFeatureCriteria.class)
public class CameraBlockEntityRenderer extends TileEntitySpecialRenderer {

	/**
	 * Stores the ceiling camera model.
	 */
	private static final IModelCustom ceilingModel = AdvancedModelLoader.loadModel (new ResourceLocation ("basedefense2", "models/surveillance/camera_ceiling.obj"));

	/**
	 * Stores the camera model.
	 */
	@Getter
	private static final IModelCustom model = AdvancedModelLoader.loadModel (new ResourceLocation ("basedefense2", "models/surveillance/camera.obj"));

	/**
	 * Renders a ceiling camera.
	 *
	 * @param camera The block entity.
	 * @param p_147500_2_ The X-Coordinate.
	 * @param p_147500_4_ The Y-Coordinate.
	 * @param p_147500_6_ The Z-Coordinate.
	 * @param p_147500_8_ Some tick nonsense.
	 * @param active Defines whether the camera will be rendered active or not.
	 * @param textureState Defines the texture state (e.g. the currently active frame in the active animation).
	 * @param rotation The rotation metadata (5 = ceiling).
	 */
	protected void renderCeilingCamera (CameraBlockEntity camera, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_, boolean active, boolean textureState, int rotation) {
		GL11.glTranslatef (0.5f, 0.8f, 0.5f);
		GL11.glScalef (.2f, .2f, .2f);

		// rotate around X axis for standing cameras
		if (rotation != 5) {
			GL11.glRotatef (180.0f, 1.0f, 0.0f, 0.0f);
			GL11.glTranslatef (0.0f, 3.0f, 0.0f);
		}

		this.bindTexture (new ResourceLocation ("basedefense2:textures/models/surveillance/camera_ceiling_base.png"));
		ceilingModel.renderPart ("Base_Cube01");

		if (active) {
			this.bindTexture (new ResourceLocation ("basedefense2:textures/models/surveillance/camera_ceiling_base_active" + (textureState ? "0" : "1") + ".png"));
			ceilingModel.renderPart ("Base_Cube01");
		}

		this.bindTexture (new ResourceLocation ("basedefense2:textures/models/surveillance/camera_ceiling_lens.png"));
		ceilingModel.renderPart ("Lens_Cube02");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void renderTileEntityAt (TileEntity p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_) {
		CameraBlockEntity camera = ((CameraBlockEntity) p_147500_1_);

		GL11.glPushMatrix ();
		{
			// translate to actual block position within world
			GL11.glTranslated (p_147500_2_, p_147500_4_, p_147500_6_);


			// prepare animation variables
			boolean textureState = ((System.currentTimeMillis () % 1000) < 500);
			boolean active = camera.isActive ();
			int rotation = camera.getRotation ();

			if (rotation < 5)
				this.renderWallCamera (camera, p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_, active, textureState, rotation);
			else
				this.renderCeilingCamera (camera, p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_, active, textureState, rotation);
		}
		GL11.glPopMatrix ();
	}

	/**
	 * Renders a wall camera.
	 * {@see rocks.spud.mc.basedefense.client.renderer.block.entity.surveillance.CameraBlockEntityRenderer#renderCeilingCamera (}
	 */
	protected void renderWallCamera (CameraBlockEntity camera, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_, boolean active, boolean textureState, int rotation) {
		// apply general rotation
		GL11.glTranslatef (0.5f, 0.5f, 0.5f);
		GL11.glRotatef ((rotation * 90.0f), 0.0f, 1.0f, 0.0f);
		GL11.glTranslatef (-0.5f, -0.5f, -0.5f);

		// fix model position
		GL11.glTranslatef (0.5f, 0.5f, 1.0f);
		GL11.glScalef (.15f, .15f, .15f);

		// draw base
		this.bindTexture (new ResourceLocation ("basedefense2:textures/models/surveillance/camera_mount.png"));
		model.renderPart ("Mount_Cube04");

		this.bindTexture (new ResourceLocation ("basedefense2:textures/models/surveillance/camera_base.png"));
		model.renderPart ("Base_Cube01");

		if (active) {
			this.bindTexture (new ResourceLocation ("basedefense2:textures/models/surveillance/camera_base_active" + (textureState ? "0" : "1") + ".png"));
			model.renderPart ("Base_Cube01");
		}

		// render body & lens
		GL11.glPushMatrix ();
		{
			// animate rotation around pivot
			GL11.glTranslatef (0.0f, 0.0f, -4.0f);
			GL11.glRotatef (camera.getBodyRotation (active), 0.0f, -1.0f, 0.0f);
			GL11.glTranslatef (0.0f, 0.0f, 4.0f);

			// draw Body & Lens
			this.bindTexture (new ResourceLocation ("basedefense2:textures/models/surveillance/camera_body.png"));
			model.renderPart ("Body_Cube02");

			if (active) {
				this.bindTexture (new ResourceLocation ("basedefense2:textures/models/surveillance/camera_body_active" + (textureState ? "0" : "1") + ".png"));
				model.renderPart ("Body_Cube02");
			}

			this.bindTexture (new ResourceLocation ("basedefense2:textures/models/surveillance/camera_lens.png"));
			model.renderPart ("Lens_Cube03");
		}
		GL11.glPopMatrix ();
	}
}
