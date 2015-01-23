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

package rocks.spud.mc.basedefense.api.surveillance.network.cache;

import appeng.api.networking.IGridCache;
import rocks.spud.mc.basedefense.api.surveillance.network.entity.ISecurityNetworkController;

/**
 * Provides a base definition for the security grid cache.
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public interface ISecurityGridCache extends IGridCache {

	/**
	 * Returns the currently active controller (or null if the state is invalid).
	 * @return The controller.
	 */
	public ISecurityNetworkController getController ();

	/**
	 * Returns the current controller state within the network.
	 * @return The controller state.
	 */
	public SecurityControllerState getControllerState ();
}
