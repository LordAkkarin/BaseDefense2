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

package rocks.spud.mc.basedefense.api.registry;

import cpw.mods.fml.relauncher.Side;
import rocks.spud.mc.basedefense.api.error.RegistryRegistrationException;

/**
 * Provides a base definition for registry scanners.
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public interface IRegistryScanner<T> {

	/**
	 * Returns the scanner side.
	 * @return The side.
	 */
	public Side getSide ();

	/**
	 * Scans a type.
	 * @param registry The parent registry.
	 * @param annotation The annotation.
	 * @param type The type.
	 * @return An to add to the cache (or null if no caching is applied).
	 * @throws rocks.spud.mc.basedefense.api.error.RegistryRegistrationException Thrown when the registration fails.
	 */
	public Object scanType (IModificationRegistry registry, T annotation, Class<?> type) throws RegistryRegistrationException;
}
