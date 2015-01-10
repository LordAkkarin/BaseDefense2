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

package rocks.spud.mc.basedefense.common;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import lombok.AccessLevel;
import lombok.Getter;
import net.minecraftforge.common.config.Configuration;
import rocks.spud.mc.basedefense.common.registration.RegistrationHelper;

/**
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public class CommonModificationProxy {

	/**
	 * Stores the active configuration.
	 */
	@Getter (AccessLevel.PROTECTED)
	private Configuration configuration;

	/**
	 * Provides an instance of RegistrationHelper.
	 */
	@Getter (AccessLevel.PROTECTED)
	private RegistrationHelper registrationHelper;

	/**
	 * Handles the {@link cpw.mods.fml.common.event.FMLInitializationEvent} event.
	 *
	 * @param event The event.
	 */
	public void initialize (FMLInitializationEvent event) {
		this.getRegistrationHelper ().scanAnnotations (Side.SERVER);
	}

	/**
	 * Handles the {@link cpw.mods.fml.common.event.FMLPostInitializationEvent} event
	 *
	 * @param event The event.
	 */
	public void postInitialize (FMLPostInitializationEvent event) {
		this.configuration.save ();
	}

	/**
	 * Handles the {@link cpw.mods.fml.common.event.FMLPreInitializationEvent} event.
	 *
	 * @param event The event.
	 */
	public void preInitialize (FMLPreInitializationEvent event) {
		this.configuration = new Configuration (event.getSuggestedConfigurationFile ());
		this.configuration.load ();

		this.registrationHelper = new RegistrationHelper (this.getConfiguration ());
	}
}
