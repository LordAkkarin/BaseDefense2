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

import rocks.spud.mc.basedefense.api.registry.IRegistrationCriteria;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides an annotation for adding criteria chains to automatically registered components.
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
@Retention (RetentionPolicy.RUNTIME)
@Target ({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
public @interface RegistrationCriteria {

	/**
	 * Defines a list of criteria to check.
	 * @return The criteria array.
	 */
	public Class<? extends IRegistrationCriteria>[] value ();
}
