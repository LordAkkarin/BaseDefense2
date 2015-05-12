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
package basedefense.client;

import basedefense.BaseDefenseModification;
import basedefense.client.component.IClientComponent;
import basedefense.client.renderer.gui.OutdatedVersionGUIRenderer;
import basedefense.common.CommonProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

/**
 * Handles the {@link cpw.mods.fml.relauncher.Side#CLIENT} initialization.
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
public class ClientProxy extends CommonProxy {

        /**
         * {@inheritDoc}
         */
        @Override
        public void onInitialization (FMLInitializationEvent event) {
                super.onInitialization (event);

                this.getComponentManager ().run (c -> {
                        IClientComponent component = ((IClientComponent) c);

                        component.registerBlockRenderers ();
                        component.registerItemRenderers ();
                        component.registerBlockEntityRenderers ();
                });
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onPostInitialization (FMLPostInitializationEvent event) {
                super.onPostInitialization (event);

                BaseDefenseModification.getInstance ().getLatestVersion ().ifPresent (v -> {
                        if (v.equalsIgnoreCase (BaseDefenseModification.getInstance ().getVersion ())) return;
                        MinecraftForge.EVENT_BUS.register (new OutdatedVersionGUIRenderer (BaseDefenseModification.getInstance ().getVersion (), v));
                });
        }
}
