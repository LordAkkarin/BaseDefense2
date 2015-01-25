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

package rocks.spud.mc.basedefense.api.registry.annotation.common;

import appeng.api.networking.IGridCache;
import rocks.spud.mc.basedefense.api.registry.criteria.AppliedEnergisticsIntegrationCriteria;
import rocks.spud.mc.basedefense.api.registry.scanner.common.GridCacheRegistryScanner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides an annotation for automated grid cache registrations.
 *
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
@Retention (RetentionPolicy.RUNTIME)
@Target (ElementType.TYPE)
@RegistrationAnnotation (value = GridCacheRegistryScanner.class, type = IGridCache.class)
@RegistrationCriteria (AppliedEnergisticsIntegrationCriteria.class)
public @interface GridCacheType {

	/**
	 * Defines the parent interface.
	 *
	 * @return The interface.
	 */
	public Class<? extends IGridCache> value ();
}
