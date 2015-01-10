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

package rocks.spud.mc.basedefense.client;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import rocks.spud.mc.basedefense.common.CommonModificationProxy;

/**
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public class ClientModificationProxy extends CommonModificationProxy {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize (FMLInitializationEvent event) {
		super.initialize (event);
		this.getRegistrationHelper ().scanAnnotations (Side.CLIENT);
	}
}
