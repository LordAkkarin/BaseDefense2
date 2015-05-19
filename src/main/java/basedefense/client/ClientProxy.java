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
import basedefense.client.version.ModificationVersionCheck;
import basedefense.common.CommonProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * Handles the {@link cpw.mods.fml.relauncher.Side#CLIENT} initialization.
 *
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
public class ClientProxy extends CommonProxy {
        private Optional<String> latestVersion;

        /**
         * Executes a version check using GitHub's API.
         */
        private void executeVersionCheck () {
                BaseDefenseModification.getInstance ().getLogger ().info ("Version Check");
                long initializationTime = System.currentTimeMillis ();


                if (!Boolean.valueOf (System.getProperty ("basedefense.disableVersionCheck", "false"))) {
                        this.latestVersion = (new ModificationVersionCheck ()).check ();
                        this.latestVersion.ifPresent (v -> {
                                if (v.equalsIgnoreCase (BaseDefenseModification.getInstance ().getVersion ())) return;

                                BaseDefenseModification.getInstance ().getLogger ().warn ("+--------------------------------+");
                                BaseDefenseModification.getInstance ().getLogger ().warn ("+          Base Defense          +");
                                BaseDefenseModification.getInstance ().getLogger ().warn ("+          is outdated!          +");
                                BaseDefenseModification.getInstance ().getLogger ().warn ("+--------------------------------+");
                                BaseDefenseModification.getInstance ().getLogger ().warn ("+  The support for this version  +");
                                BaseDefenseModification.getInstance ().getLogger ().warn ("+  has been discontinued.        +");
                                BaseDefenseModification.getInstance ().getLogger ().warn ("+  Please update to a newer      +");
                                BaseDefenseModification.getInstance ().getLogger ().warn ("+  release!                      +");
                                BaseDefenseModification.getInstance ().getLogger ().warn ("+--------------------------------+");
                                BaseDefenseModification.getInstance ().getLogger ().warn ("+  Installed: " + StringUtils.rightPad (BaseDefenseModification.getInstance ().getVersion (), 17) + "  +");
                                BaseDefenseModification.getInstance ().getLogger ().warn ("+  Latest:    " + StringUtils.rightPad (v, 17) + "  +");
                                BaseDefenseModification.getInstance ().getLogger ().warn ("+--------------------------------+");
                        });
                } else {
                        this.latestVersion = Optional.empty ();

                        BaseDefenseModification.getInstance ().getLogger ().warn ("+---------------------------------+");
                        BaseDefenseModification.getInstance ().getLogger ().warn ("+          VERSION CHECK          +");
                        BaseDefenseModification.getInstance ().getLogger ().warn ("+             DISABLED            +");
                        BaseDefenseModification.getInstance ().getLogger ().warn ("+---------------------------------+");
                        BaseDefenseModification.getInstance ().getLogger ().warn ("+  Installed: " + StringUtils.rightPad (BaseDefenseModification.getInstance ().getVersion (), 18) + "  +");
                        BaseDefenseModification.getInstance ().getLogger ().warn ("+---------------------------------+");
                }

                BaseDefenseModification.getInstance ().getLogger ().info ("Version Check ended (took " + (System.currentTimeMillis () - initializationTime) + " ms)");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onPreInitialization (FMLPreInitializationEvent event) {
                super.onPreInitialization (event);

                this.executeVersionCheck ();
        }

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

                this.latestVersion.ifPresent (v -> {
                        if (v.equalsIgnoreCase (BaseDefenseModification.getInstance ().getVersion ())) return;
                        MinecraftForge.EVENT_BUS.register (new OutdatedVersionGUIRenderer (BaseDefenseModification.getInstance ().getVersion (), v));
                });
        }
}
