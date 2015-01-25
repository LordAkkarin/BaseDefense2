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

import rocks.spud.mc.basedefense.api.registry.IRegistryScanner;
import rocks.spud.mc.basedefense.api.registry.ScannerPriorityType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides an annotation for automating registry scanners.
 *
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
@Retention (RetentionPolicy.RUNTIME)
@Target (ElementType.ANNOTATION_TYPE)
@ScannerPriority (ScannerPriorityType.HIGHEST)
public @interface RegistrationAnnotation {

	/**
	 * Defines a type restriction for a registration annotation.
	 *
	 * @return The restriction.
	 */
	public Class<?> type () default Object.class;

	/**
	 * Defines the scanner associated with the annotation.
	 *
	 * @return The scanner type.
	 */
	public Class<? extends IRegistryScanner> value ();
}
