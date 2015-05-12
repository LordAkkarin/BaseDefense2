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
package basedefense;

import basedefense.common.CommonProxy;
import basedefense.common.component.ComponentManager;
import basedefense.common.version.ModificationVersionCheck;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

/**
 * Provides a modification entry point for Forge.
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
@Mod (modid = BaseDefenseModification.IDENTIFIER, useMetadata = true, acceptedMinecraftVersions = "1.7.10", canBeDeactivated = false)
public class BaseDefenseModification {
        public static final String IDENTIFIER = "basedefense";

        @Getter
        @Mod.Instance (BaseDefenseModification.IDENTIFIER)
        private static BaseDefenseModification instance;

        // FIXME: Can we please not make this static? This is bullshit ...
        @Getter
        @SidedProxy (serverSide = "basedefense.server.ServerProxy", clientSide = "basedefense.client.ClientProxy")
        private static CommonProxy proxy = null;
        @Getter
        private Logger logger = null;
        @Getter
        private String version = null;
        @Getter
        private Optional<String> latestVersion = null;

        /**
         * Stores the component manager.
         * @return The manager.
         */
        public ComponentManager getComponentManager () {
                return getProxy ().getComponentManager ();
        }

        /**
         * Executes a version check using GitHub's API.
         */
        private void executeVersionCheck () {
                this.getLogger ().info ("Version Check");
                long initializationTime = System.currentTimeMillis ();


                if (!Boolean.valueOf (System.getProperty ("basedefense.disableVersionCheck", "false"))) {
                        this.latestVersion = (new ModificationVersionCheck ()).check ();
                        this.latestVersion.ifPresent (v -> {
                                if (v.equalsIgnoreCase (this.getVersion ())) return;

                                this.getLogger ().warn ("+--------------------------------+");
                                this.getLogger ().warn ("+          Base Defense          +");
                                this.getLogger ().warn ("+          is outdated!          +");
                                this.getLogger ().warn ("+--------------------------------+");
                                this.getLogger ().warn ("+  The support for this version  +");
                                this.getLogger ().warn ("+  has been discontinued.        +");
                                this.getLogger ().warn ("+  Please update to a newer      +");
                                this.getLogger ().warn ("+  release!                      +");
                                this.getLogger ().warn ("+--------------------------------+");
                                this.getLogger ().warn ("+  Installed: " + StringUtils.rightPad (this.getVersion (), 17) + "  +");
                                this.getLogger ().warn ("+  Latest:    " + StringUtils.rightPad (v, 17) + "  +");
                                this.getLogger ().warn ("+--------------------------------+");
                        });
                } else {
                        this.latestVersion = Optional.empty ();

                        this.getLogger ().warn ("+---------------------------------+");
                        this.getLogger ().warn ("+          VERSION CHECK          +");
                        this.getLogger ().warn ("+             DISABLED            +");
                        this.getLogger ().warn ("+---------------------------------+");
                        this.getLogger ().warn ("+  Installed: " + StringUtils.rightPad (this.getVersion (), 18) + "  +");
                        this.getLogger ().warn ("+---------------------------------+");
                }

                this.getLogger ().info ("Version Check ended (took " + (System.currentTimeMillis () - initializationTime) + " ms)");
        }

        /**
         * Handles the {@link FMLPreInitializationEvent} event.
         * @param event The event.
         */
        @Mod.EventHandler
        private void onPreInitialization (FMLPreInitializationEvent event) {
                this.logger = event.getModLog ();
                this.version = event.getModMetadata ().version;
                this.executeVersionCheck ();

                this.getLogger ().info ("Pre Initialization");
                long initializationTime = System.currentTimeMillis ();
                getProxy ().onPreInitialization (event);
                this.getLogger ().info ("Pre Initialization ended (took " + (System.currentTimeMillis () - initializationTime) + " ms)");
        }

        /**
         * Handles the {@link FMLInitializationEvent} event.
         * @param event The event.
         */
        @Mod.EventHandler
        private void onInitialization (FMLInitializationEvent event) {
                this.getLogger ().info ("Initialization");
                long initializationTime = System.currentTimeMillis ();
                getProxy ().onInitialization (event);
                this.getLogger ().info ("Initialization ended (took " + (System.currentTimeMillis () - initializationTime) + " ms)");
        }

        /**
         * Handles the {@link FMLPostInitializationEvent} event.
         * @param event The event.
         */
        @Mod.EventHandler
        private void onPostInitialization (FMLPostInitializationEvent event) {
                this.getLogger ().info ("Post Initialization");
                long initializationTime = System.currentTimeMillis ();
                getProxy ().onPostInitialization (event);
                this.getLogger ().info ("Post Initialization ended (took " + (System.currentTimeMillis () - initializationTime) + " ms)");
        }
}
