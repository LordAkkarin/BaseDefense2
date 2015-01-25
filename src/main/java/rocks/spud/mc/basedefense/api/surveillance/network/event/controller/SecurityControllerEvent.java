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
import rocks.spud.mc.basedefense.api.surveillance.network.event.SecurityNetworkEvent;

/**
 * Provides a base event for security controllers.
 *
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public abstract class SecurityControllerEvent extends SecurityNetworkEvent {

	/**
	 * Stores the controller that issued the event.
	 */
	@Getter
	private final ISecurityNetworkController controller;

	/**
	 * Constructs a new SecurityControllerEvent.
	 *
	 * @param controller The controller.
	 */
	public SecurityControllerEvent (ISecurityNetworkController controller) {
		super ();

		this.controller = controller;
	}
}
