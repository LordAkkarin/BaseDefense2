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
package basedefense.common;

import basedefense.common.achievement.BaseDefenseAchievementPage;
import basedefense.common.component.ComponentManager;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import lombok.Getter;
import net.minecraftforge.common.AchievementPage;

/**
 * Provides a base proxy implementation.
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
public abstract class AbstractProxy {
        @Getter
        private final ComponentManager componentManager = new ComponentManager ();

        public AbstractProxy () {
                this.registerComponents ();
                this.registerIntegrations ();
        }

        /**
         * Handles the modification pre-initialization.
         * @param event The event.
         */
        public void onPreInitialization (FMLPreInitializationEvent event) {
                this.componentManager.activateComponents (event);
        }

        /**
         * Handles the modification initialization.
         * @param event The event.
         */
        public void onInitialization (FMLInitializationEvent event) {
                this.componentManager.run (c -> {
                        c.registerBlocks ();
                        c.registerItems ();

                        c.registerBlockEntities ();
                        c.registerEntities ();

                        c.registerAchievements ();
                        c.registerDungeonLoot ();
                });

                AchievementPage.registerAchievementPage (BaseDefenseAchievementPage.PAGE);
        }

        /**
         * Handles the modification post-initialization.
         * @param event The event.
         */
        public void onPostInitialization (FMLPostInitializationEvent event) {
                this.componentManager.activateIntegrations (event);
        }

        /**
         * Registers all modification components.
         */
        protected abstract void registerComponents ();

        /**
         * Registers all modification integrations.
         */
        protected abstract void registerIntegrations ();
}
