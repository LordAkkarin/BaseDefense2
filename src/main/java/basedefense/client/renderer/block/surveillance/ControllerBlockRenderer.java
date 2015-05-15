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
package basedefense.client.renderer.block.surveillance;

import basedefense.api.surveillance.ISurveillanceGridCache.ControllerState;
import basedefense.client.renderer.block.AbstractBlockRenderer;
import basedefense.common.block.surveillance.ControllerBlock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

/**
 * Provides a block renderer implementation for {@link ControllerBlock}.
 *
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
public class ControllerBlockRenderer extends AbstractBlockRenderer {

        protected ControllerBlockRenderer (int renderID) {
                super (renderID);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void renderInventoryBlock (Block block, int metadata, int modelId, RenderBlocks renderer) {
                if (!(block instanceof ControllerBlock)) return;
                ControllerBlock controllerBlock = ((ControllerBlock) block);
                Tessellator tessellator = Tessellator.instance;

                tessellator.startDrawingQuads ();
                tessellator.setNormal (-1.0f, 0.0f, 0.0f);
                tessellator.setColorOpaque_I (0xFFFFFF);
                renderer.renderFaceXNeg (block, 0.0d, 0.0d, 0.0d, controllerBlock.getBaseTexture ());
                tessellator.draw ();

                tessellator.startDrawingQuads ();
                tessellator.setNormal (1.0f, 0.0f, 0.0f);
                tessellator.setColorOpaque_I (0xFFFFFF);
                renderer.renderFaceXPos (block, 0.0d, 0.0d, 0.0d, controllerBlock.getBaseTexture ());
                tessellator.draw ();

                tessellator.startDrawingQuads ();
                tessellator.setNormal (0.0f, -1.0f, 0.0f);
                tessellator.setColorOpaque_I (0xFFFFFF);
                renderer.renderFaceYNeg (block, 0.0d, 0.0d, 0.0d, controllerBlock.getBaseTexture ());
                tessellator.draw ();

                tessellator.startDrawingQuads ();
                tessellator.setNormal (0.0f, 1.0f, 0.0f);
                tessellator.setColorOpaque_I (0xFFFFFF);
                renderer.renderFaceYPos (block, 0.0d, 0.0d, 0.0d, controllerBlock.getBaseTexture ());
                tessellator.draw ();

                tessellator.startDrawingQuads ();
                tessellator.setNormal (0.0f, 0.0f, -1.0f);
                tessellator.setColorOpaque_I (0xFFFFFF);
                renderer.renderFaceZNeg (block, 0.0d, 0.0d, 0.0d, controllerBlock.getBaseTexture ());
                tessellator.draw ();

                tessellator.startDrawingQuads ();
                tessellator.setNormal (0.0f, 0.0f, 1.0f);
                tessellator.setColorOpaque_I (0xFFFFFF);
                renderer.renderFaceZPos (block, 0.0d, 0.0d, 0.0d, controllerBlock.getBaseTexture ());
                tessellator.draw ();

                renderer.clearOverrideBlockTexture ();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
                if (!(block instanceof ControllerBlock)) return false;
                ControllerBlock controllerBlock = ((ControllerBlock) block);

                renderer.setOverrideBlockTexture (controllerBlock.getBaseTexture ());
                renderer.renderStandardBlock (block, x, y, z);

                ControllerState state = controllerBlock.getState (world.getBlockMetadata (x, y, z));

                if (state != ControllerState.OFFLINE) {
                        switch (state) {
                                case CONFLICT:
                                        renderer.setOverrideBlockTexture (controllerBlock.getConflictTexture ());
                                        break;
                                case ACTIVE_PRIMITIVE:
                                        renderer.setOverrideBlockTexture (controllerBlock.getActivePrimitiveTexture ());
                                        break;
                                case ACTIVE_FACE_RECOGNITION:
                                        renderer.setOverrideBlockTexture (controllerBlock.getActiveFacialRecognitionTexture ());
                                        break;
                        }

                        renderer.renderStandardBlock (block, x, y, z);
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
}
