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

package rocks.spud.mc.basedefense.api.error;

/**
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public class RegistryInitializationException extends RegistryRegistrationException {

	/**
	 * Constructs a new RegistryInitializationException.
	 */
	public RegistryInitializationException () {
		super ();
	}

	/**
	 * Constructs a new RegistryInitializationException.
	 * @param s The error message.
	 */
	public RegistryInitializationException (String s) {
		super (s);
	}

	/**
	 * Constructs a new RegistryInitializationException.
	 * @param s The error message.
	 * @param throwable The error cause.
	 */
	public RegistryInitializationException (String s, Throwable throwable) {
		super (s, throwable);
	}

	/**
	 * Constructs a new RegistryInitializationException.
	 * @param throwable The error cause.
	 */
	public RegistryInitializationException (Throwable throwable) {
		super (throwable);
	}
}