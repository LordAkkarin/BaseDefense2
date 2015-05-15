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

import appeng.api.AEApi;
import basedefense.api.surveillance.ISurveillanceGridCache;
import basedefense.common.component.CommonSurveillanceComponent;
import basedefense.common.component.ComponentManager;
import basedefense.common.grid.SurveillanceGridCache;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.common.config.Configuration;

/**
 * Provides an integration for Applied Energistics 2.
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
public abstract class CommonAppliedEnergisticsIntegration extends AbstractCommonIntegration {

        public CommonAppliedEnergisticsIntegration (ComponentManager componentManager) {
                super (componentManager);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void activate () {
                AEApi.instance ().registries ().gridCache ().registerGridCache (ISurveillanceGridCache.class, SurveillanceGridCache.class);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isAvailable (FMLPostInitializationEvent event, Configuration configuration) {
                return (this.getComponentManager ().isComponentActive (CommonSurveillanceComponent.class) && Loader.isModLoaded ("appliedenergistics2")); // Note: There is no way to disable this at the moment!
        }
}
