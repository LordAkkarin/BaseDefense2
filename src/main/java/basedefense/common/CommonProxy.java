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

import basedefense.client.component.ClientSurveillanceComponent;
import basedefense.client.component.integration.ClientAppliedEnergisticsIntegration;
import basedefense.client.component.integration.ClientNotEnoughItemsIntegration;
import basedefense.server.component.ServerSurveillanceComponent;
import basedefense.server.component.integration.ServerAppliedEnergisticsIntegration;
import basedefense.server.component.integration.ServerNotEnoughItemsIntegration;

/**
 * Provides instructions required on both {@link cpw.mods.fml.relauncher.Side#CLIENT} and {@link cpw.mods.fml.relauncher.Side#SERVER}.
 *
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
public class CommonProxy extends AbstractProxy {

        /**
         * {@inheritDoc}
         */
        @Override
        protected void registerComponents () {
                this.getComponentManager ().registerComponent (ServerSurveillanceComponent.class, ClientSurveillanceComponent.class);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void registerIntegrations () {
                this.getComponentManager ().registerIntegration (ServerAppliedEnergisticsIntegration.class, ClientAppliedEnergisticsIntegration.class);
                this.getComponentManager ().registerIntegration (ServerNotEnoughItemsIntegration.class, ClientNotEnoughItemsIntegration.class);
        }
}
