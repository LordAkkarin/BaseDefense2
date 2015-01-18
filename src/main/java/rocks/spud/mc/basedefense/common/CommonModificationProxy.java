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
import lombok.AccessLevel;
import lombok.Getter;
import net.minecraftforge.common.config.Configuration;
import rocks.spud.mc.basedefense.BaseDefenseModification;
import rocks.spud.mc.basedefense.api.registry.IModificationRegistry;
import rocks.spud.mc.basedefense.common.registry.ModificationRegistry;

/**
 * Provides a common (server side) proxy.
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public class CommonModificationProxy {

	/**
	 * Stores the active configuration.
	 */
	@Getter (AccessLevel.PROTECTED)
	private Configuration configuration;

	/**
	 * Stores the modification registry.
	 */
	@Getter
	private IModificationRegistry registry;

	/**
	 * Handles the {@link cpw.mods.fml.common.event.FMLInitializationEvent} event.
	 *
	 * @param event The event.
	 */
	public void initialize (FMLInitializationEvent event) {
		this.getRegistry ().scanPackage ("rocks.spud.mc.basedefense");
	}

	/**
	 * Handles the {@link cpw.mods.fml.common.event.FMLPostInitializationEvent} event
	 *
	 * @param event The event.
	 */
	public void postInitialize (FMLPostInitializationEvent event) {
		BaseDefenseModification.getInstance ().getLogger ().info ("Updating configuration ...");
		this.configuration.save ();
		BaseDefenseModification.getInstance ().getLogger ().info ("Modification configuration was written back to disk.");
	}

	/**
	 * Handles the {@link cpw.mods.fml.common.event.FMLPreInitializationEvent} event.
	 *
	 * @param event The event.
	 */
	public void preInitialize (FMLPreInitializationEvent event) {
		this.configuration = new Configuration (event.getSuggestedConfigurationFile ());
		this.configuration.load ();

		this.registry = new ModificationRegistry (this.getConfiguration ());
	}
}
