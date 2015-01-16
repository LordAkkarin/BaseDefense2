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

package rocks.spud.mc.basedefense.api.surveillance.network.event;

import appeng.api.networking.events.MENetworkEvent;

/**
 * Represents a security network event within the global network.
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public abstract class SecurityNetworkEvent extends MENetworkEvent {

	/**
	 * Constructs a new SecurityNetworkEvent.
	 */
	public SecurityNetworkEvent () {
		super (); // Just in case AE chooses to do things here in the future
	}
}