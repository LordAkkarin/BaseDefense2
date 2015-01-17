/*
 * Copyright 2015 Johannes Donath <johannesd@torchmind.com>
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

package rocks.spud.mc.basedefense.api.registry.criteria;

import cpw.mods.fml.common.Loader;
import net.minecraftforge.common.config.Configuration;
import rocks.spud.mc.basedefense.api.registry.IRegistrationCriteria;

/**
 * Provides a criteria implementation for the AE2 integration.
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public class AppliedEnergisticsIntegrationCriteria implements IRegistrationCriteria {

	/**
	 * Defines the modification identifier of AE.
	 */
	public static final String AE_MODIFICATION_IDENTIFIER = "appliedenergistics2";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMet (Configuration configuration) {
		return (configuration.getBoolean ("appliedenergistics", "integration", true, "Enables the applied energistics configuration (required by some modules)") && Loader.isModLoaded (AE_MODIFICATION_IDENTIFIER));
	}
}
