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
package basedefense.common.block.surveillance;

import basedefense.api.surveillance.ISurveillanceGridCache.ControllerState;
import basedefense.client.renderer.block.surveillance.ControllerBlockRenderer;
import basedefense.common.block.AbstractBlock;
import basedefense.common.block.entity.surveillance.ControllerBlockEntity;
import basedefense.common.creative.SurveillanceCreativeTab;
import basedefense.util.RegistrationUtility;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.Getter;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
public class ControllerBlock extends AbstractBlock implements ITileEntityProvider {
        public static final String NAME = RegistrationUtility.getName ("surveillance", "controller");
        public static final ControllerBlock BLOCK = new ControllerBlock ();

        @Getter
        private IIcon baseTexture;
        @Getter
        private IIcon activePrimitiveTexture;
        @Getter
        private IIcon activeFacialRecognitionTexture;
        @Getter
        private IIcon conflictTexture;

        protected ControllerBlock () {
                super (Material.iron);

                this.setBlockName (RegistrationUtility.getTranslation ("surveillance", "controller"));
                this.setBlockTextureName ("basedefense2:surveillance_controller");
                this.setCreativeTab (SurveillanceCreativeTab.TAB);
        }

        /**
         * Builds the metadata value based on the current controller state.
         * @param state The state.
         * @param metadata The previous metadata (for retaining the actual controller type).
         * @return The state.
         */
        public int buildState (ControllerState state, int metadata) {
                int newMetadata = (metadata & 0x4);

                if (state == ControllerState.OFFLINE)
                        return newMetadata;

                if (state == ControllerState.CONFLICT)
                        return (newMetadata | 0x3);

                return (state == ControllerState.ACTIVE_FACE_RECOGNITION ? 0x5 : 0x1);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public TileEntity createNewTileEntity (World p_149915_1_, int p_149915_2_) {
                return (new ControllerBlockEntity ());
        }

        /**
         * Retrieves the controller state.
         * @param metadata The metadata.
         * @return The state.
         */
        public ControllerState getState (int metadata) {
                if ((metadata & 0x1) == 0x0) return ControllerState.OFFLINE;
                if ((metadata & 0x2) == 0x2) return ControllerState.CONFLICT;
                return ((metadata & 0x4) == 0x4 ? ControllerState.ACTIVE_FACE_RECOGNITION : ControllerState.ACTIVE_PRIMITIVE);
        }

        /**
         * Retrieves the controller type.
         * @param metadata The metadata.
         * @return The type.
         */
        public ControllerState getType (int metadata) {
                if ((metadata & 0x2) == 0x2) return ControllerState.ACTIVE_FACE_RECOGNITION;
                return ControllerState.ACTIVE_PRIMITIVE;
        }

        /**
         * {@inheritDoc}
         */
        @SideOnly (Side.CLIENT)
        @Override
        protected ISimpleBlockRenderingHandler registerRenderType () {
                return RegistrationUtility.registerBlockRenderer (ControllerBlockRenderer.class);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void registerBlockIcons (IIconRegister p_149651_1_) {
                this.baseTexture = p_149651_1_.registerIcon ("basedefense2:surveillance_controller");
                this.activePrimitiveTexture = p_149651_1_.registerIcon ("basedefense2:surveillance_controller_active");
                this.activeFacialRecognitionTexture = p_149651_1_.registerIcon ("basedefense2:surveillance_controller_active_facial");
                this.conflictTexture = p_149651_1_.registerIcon ("basedefense2:surveillance_controller_conflict");
        }
}
