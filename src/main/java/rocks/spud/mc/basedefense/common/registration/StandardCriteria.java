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

package rocks.spud.mc.basedefense.common.registration;

import cpw.mods.fml.common.Loader;
import net.minecraftforge.common.config.Configuration;

/**
 * Provides default criteria to pass to the {@link rocks.spud.mc.basedefense.common.registration.annotation.Criteria} annotation.
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public enum StandardCriteria implements ICriteria {
	// Configuration
	MODULE_SURVEILLANCE {
		@Override
		public boolean isMet (Configuration configuration) {
			if (!configuration.getBoolean ("surveillance", "feature", true, "Enables surveillance features")) return false;
			return INTEGRATION_AE2.isMet (configuration);
		}
	},

	// Integrations
	INTEGRATION_AE2 {
		@Override
		public boolean isMet (Configuration configuration) {
			return Loader.isModLoaded ("appliedenergistics2");
		}
	}
}
