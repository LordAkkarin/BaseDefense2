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
 * Provides an event for forcefully disabling one or more cameras.
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public class CameraForceOfflineRequestEvent extends CameraCommunicationEvent {



	/**
	 * Defines the force offline state.
	 * false = regular mode, true = forced offline
	 */
	@Getter
	@Setter
	private boolean forceOffline = false;

	/**
	 * Constructs a new CameraForceOfflineRequestEvent.
	 * @param groupName The group name.
	 * @param forceOffline The forced offline state.
	 */
	public CameraForceOfflineRequestEvent (String groupName, boolean forceOffline) {
		super (groupName);

		this.setForceOffline (forceOffline);
	}
}
