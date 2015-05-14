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
package basedefense.util;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;

/**
 * Provides utility methods for registering game elements.
 *
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
public class RegistrationUtility {
        private RegistrationUtility () { }

        /**
         * Retrieves a registration name.
         *
         * @param category The category.
         * @param name     The name.
         * @return The name.
         */
        public static String getName (String category, @NonNull String name) {
                if (category == null) return name;
                return category + "_" + name;
        }

        /**
         * Retrieves a stat name.
         *
         * @param category The category.
         * @param name     The name.
         * @return The stat name.
         */
        public static String getStatName (String category, @NonNull String name) {
                if (category == null) return "basedefense_" + name;
                return "basedefense_" + category + "_" + name;
        }

        /**
         * Retrieves a translation name.
         *
         * @param category The category.
         * @param name     The name.
         * @return The translation name.
         */
        public static String getTranslation (String category, @NonNull String name) {
                if (category == null) return "basedefense." + name;
                return "basedefense." + category + "." + name;
        }

        /**
         * Registers a block renderer.
         *
         * @param blockRenderingHandler The rendering handler.
         * @return The render type identifier.
         */
        @SideOnly (Side.CLIENT)
        @SneakyThrows
        public static ISimpleBlockRenderingHandler registerBlockRenderer (Class<? extends ISimpleBlockRenderingHandler> blockRenderingHandler) {
                Constructor<? extends ISimpleBlockRenderingHandler> constructor = blockRenderingHandler.getDeclaredConstructor (Integer.TYPE);
                constructor.setAccessible (true);
                ISimpleBlockRenderingHandler renderingHandler = constructor.newInstance (RenderingRegistry.getNextAvailableRenderId ());

                RenderingRegistry.registerBlockHandler (renderingHandler);
                return renderingHandler;
        }
}
