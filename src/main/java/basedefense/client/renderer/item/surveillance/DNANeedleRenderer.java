/*
 * Copyright 2015 Johannes Donath <johannesd@torchmind.com>
 * and other copyright owners as documented in the project's IP log.
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
package basedefense.client.renderer.item.surveillance;

import basedefense.client.renderer.item.AbstractItemRenderer;
import basedefense.common.item.surveillance.DNANeedleItem;
import basedefense.common.item.surveillance.DNANeedleItem.VialType;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

/**
 * Provides a renderer for {@link basedefense.common.item.surveillance.DNANeedleItem}.
 *
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
public class DNANeedleRenderer extends AbstractItemRenderer {
        @Getter
        private static final IModelCustom model = AdvancedModelLoader.loadModel (new ResourceLocation ("basedefense2", "models/surveillance_dna_needle.obj"));

        /**
         * {@inheritDoc}
         */
        @Override
        protected void renderEntity (ItemStack itemStack, Object... data) {
                GL11.glScalef (0.125f, 0.125f, 0.125f);
                GL11.glTranslatef (0.0f, 5.0f, 0.0f);

                this.renderModel (itemStack);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void renderEquipped (ItemRenderType type, ItemStack itemStack, Object... data) {
                this.renderModel (itemStack);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void renderInventory (ItemStack itemStack, Object... data) {
                GL11.glScalef (0.125f, 0.125f, 0.125f);
                this.renderModel (itemStack);
        }

        /**
         * Renders the needle model based on item attributes.
         *
         * @param itemStack The item stack.
         */
        private void renderModel (ItemStack itemStack) {
                Minecraft.getMinecraft ().renderEngine.bindTexture (new ResourceLocation ("basedefense2", "textures/items/surveillance_dna_needle.png"));
                getModel ().renderPart ("Needle_Cube");

                VialType vialType = DNANeedleItem.getVialType (itemStack);
                if (vialType == VialType.EMPTY || vialType == VialType.PLAYER || vialType == VialType.ENTITY) {
                        GL11.glEnable (GL11.GL_BLEND);

                        switch (vialType) {
                                case PLAYER:
                                        Minecraft.getMinecraft ().renderEngine.bindTexture (new ResourceLocation ("basedefense2", "textures/items/surveillance_dna_needle_player.png"));
                                        break;
                                case ENTITY:
                                        Minecraft.getMinecraft ().renderEngine.bindTexture (new ResourceLocation ("basedefense2", "textures/items/surveillance_dna_needle_mob.png"));
                                        break;
                        }

                        if (vialType != VialType.EMPTY) getModel ().renderPart ("Vial_Content_Cube.002");

                        Minecraft.getMinecraft ().renderEngine.bindTexture (new ResourceLocation ("basedefense2", "textures/items/surveillance_dna_needle.png"));
                        getModel ().renderPart ("Vial_Cube.001");

                        GL11.glDisable (GL11.GL_BLEND);
                }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void setupEquipped (ItemStack itemStack, Object... data) {
                GL11.glRotatef (45, 0.0f, 1.0f, 0.0f);
                GL11.glRotatef (70, 1.0f, 0.0f, 0.0f);

                GL11.glTranslatef (0.0f, 0.0f, -5.0f);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void setupEquippedCommon (ItemStack itemStack, Object... data) {
                GL11.glScalef (0.25f, 0.25f, 0.25f);
                GL11.glRotatef (180, 0.0f, 1.0f, 0.0f);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void setupEquippedFirstPerson (ItemStack itemStack, Object... data) {
                GL11.glRotatef (-65, 0.0f, 1.0f, 0.0f);
                GL11.glTranslatef (-2.5f, 3.0f, 1.0f);
        }
}
