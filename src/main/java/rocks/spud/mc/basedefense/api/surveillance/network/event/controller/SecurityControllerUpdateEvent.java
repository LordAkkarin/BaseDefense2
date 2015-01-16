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

package rocks.spud.mc.basedefense.api.surveillance.network.event.controller;

import lombok.Getter;
import rocks.spud.mc.basedefense.api.surveillance.network.entity.ISecurityNetworkController;
import rocks.spud.mc.basedefense.common.network.cache.SecurityControllerState;

/**
 * Occurs when the controller state changes.
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public class SecurityControllerUpdateEvent extends SecurityControllerEvent {

	/**
	 * Stores the new state.
	 */
	@Getter
	private final SecurityControllerState state;

	/**
	 * Constructs a new SurveillanceNetworkControllerUpdateEvent
	 * @param controller The controller.
	 */
	public SecurityControllerUpdateEvent (ISecurityNetworkController controller, SecurityControllerState state) {
		super (controller);
		this.state = state;
	}
}
