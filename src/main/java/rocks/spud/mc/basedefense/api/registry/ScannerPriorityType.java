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

/**
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public enum ScannerPriorityType {
	HIGHEST (2),
	HIGH (1),
	NORMAL (0),
	LOW (-1),
	LOWEST (-2);

	/**
	 * Defines the numeric version.
	 */
	public final int numeric;

	/**
	 * Constructs a new ScannerPriorityType.
	 * @param numeric The numeric.
	 */
	private ScannerPriorityType (int numeric) {
		this.numeric = numeric;
	}
}
