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
 * Provides a base exception for registry error cases.
 *
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public abstract class RegistryException extends BaseDefenseException {

	/**
	 * Constructs a new RegistryException.
	 */
	public RegistryException () {
		super ();
	}

	/**
	 * Constructs a new RegistryException.
	 *
	 * @param s The error message.
	 */
	public RegistryException (String s) {
		super (s);
	}

	/**
	 * Constructs a new RegistryException.
	 *
	 * @param s The error message.
	 * @param throwable The error cause.
	 */
	public RegistryException (String s, Throwable throwable) {
		super (s, throwable);
	}

	/**
	 * Constructs a new RegistryException.
	 *
	 * @param throwable The error cause.
	 */
	public RegistryException (Throwable throwable) {
		super (throwable);
	}
}
