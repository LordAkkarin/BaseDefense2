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
package basedefense.common.component.integration;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.common.config.Configuration;

/**
 * Provides a base interface for modification integrations.
 *
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
public interface ICommonIntegration {

        /**
         * Activates the integration.
         */
        void activate ();

        /**
         * Checks whether an integration is available.
         *
         * @param event         The initialization event.
         * @param configuration The configuration.
         * @return True if available.
         */
        boolean isAvailable (FMLPostInitializationEvent event, Configuration configuration);
}
