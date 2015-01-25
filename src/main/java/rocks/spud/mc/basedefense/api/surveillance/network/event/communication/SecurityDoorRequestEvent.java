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

package rocks.spud.mc.basedefense.api.surveillance.network.event.communication;

import lombok.Getter;
import lombok.Setter;

/**
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public class SecurityDoorRequestEvent extends SecurityDoorEvent {

	/**
	 * Indicates whether the door cooldown shall be honored.
	 */
	@Getter
	@Setter
	private boolean cooldownEnabled = true;

	/**
	 * Stores the door state (closed = false, open = true).
	 */
	@Getter
	@Setter
	private boolean state = false;

	/**
	 * Constructs a new SecurityDoorRequestEvent.
	 *
	 * @param groupName The group name.
	 * @param state The state.
	 * @param cooldownEnabled Indicates whether door cooldowns shall be honored.
	 */
	public SecurityDoorRequestEvent (String groupName, boolean state, boolean cooldownEnabled) {
		super (groupName);

		this.setState (state);
		this.setCooldownEnabled (cooldownEnabled);
	}

	/**
	 * Constructs a new SecurityDoorRequestEvent.
	 *
	 * @param groupName The group name.
	 * @param state The state.
	 */
	public SecurityDoorRequestEvent (String groupName, boolean state) {
		this (groupName, state, true);
	}
}
