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

import rocks.spud.mc.basedefense.api.error.RegistryInitializationException;

import java.lang.annotation.Annotation;

/**
 * Provides a definition for modification registries.
 *
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public interface IModificationRegistry {

	/**
	 * Checks whether a specific criteria is met.
	 *
	 * @param criteriaType The criteria type.
	 * @return True if the criteria is met.
	 */
	public boolean checkCriteria (Class<? extends IRegistrationCriteria> criteriaType);

	/**
	 * Constructs an instance of a specific type.
	 *
	 * @param type The type.
	 * @param <T> The type.
	 * @return The instance.
	 * @throws rocks.spud.mc.basedefense.api.error.RegistryInitializationException Thrown when the initialization fails.
	 */
	public <T> T constructInstance (Class<T> type) throws RegistryInitializationException;

	/**
	 * Returns the instance of a type.
	 *
	 * @param type The type.
	 * @param <T> The type.
	 * @return The instance.
	 */
	public <T> T getInstance (Class<T> type);

	/**
	 * Returns the identifier assigned to a renderer.
	 *
	 * @return The renderer type.
	 */
	public int getRendererIdentifier (Class<?> rendererType);

	/**
	 * Registers a scanner.
	 *
	 * @param annotationType The annotation type to scan for.
	 * @param filter The filter.
	 * @param scanner The scanner.
	 */
	public void registerScanner (Class<? extends Annotation> annotationType, Class<?> filter, IRegistryScanner scanner);

	/**
	 * Scans a package for classes to register.
	 *
	 * @param packageName The package name.
	 */
	public void scanPackage (String packageName);

	/**
	 * Scans a package for a specific annotation type.
	 *
	 * @param packageName The package name.
	 * @param annotationType The annotation type.
	 */
	public void scanPackage (String packageName, Class<? extends Annotation> annotationType);
}
