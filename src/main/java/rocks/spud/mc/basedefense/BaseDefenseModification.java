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

package rocks.spud.mc.basedefense;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import lombok.Getter;
import org.apache.logging.log4j.Logger;
import rocks.spud.mc.basedefense.common.CommonModificationProxy;

/**
 * Provides the Base Defense 2 modification.
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
@Mod (modid = BaseDefenseModification.IDENTIFIER, version = BaseDefenseModification.VERSION, useMetadata = true, acceptedMinecraftVersions = BaseDefenseModification.SUPPORTED_MINECRAFT_VERSION_RANGE, acceptableSaveVersions = BaseDefenseModification.SUPPORTED_VERSION_RANGE, acceptableRemoteVersions = BaseDefenseModification.SUPPORTED_VERSION_RANGE)
public class BaseDefenseModification {

	/**
	 * Defines the modification identifier.
	 */
	public static final String IDENTIFIER = "basedefense2";

	/**
	 * Defines the default version used in absence of an mcmod.info file.
	 */
	public static final String VERSION = "1.0.0-SNAPSHOT";

	/**
	 * Defines the supported Minecraft version range.
	 */
	public static final String SUPPORTED_MINECRAFT_VERSION_RANGE = "[1.7.10,1.8)";

	/**
	 * Stores the modification instance.
	 */
	@Mod.Instance (BaseDefenseModification.IDENTIFIER)
	@Getter
	private static BaseDefenseModification instance = null;

	/**
	 * Stores the modification logger.
	 */
	@Getter
	private Logger logger = null;

	/**
	 * Stores the active modification proxy.
	 * Note: This has to be a static to make FML happy. Screw you FML! Statics suck in this context!
	 */
	@SidedProxy (serverSide = "rocks.spud.mc.basedefense.common.CommonModificationProxy", clientSide = "rocks.spud.mc.basedefense.client.ClientModificationProxy")
	private static CommonModificationProxy proxy = null;

	/**
	 * Defines the supported version range.
	 * @todo Remove snapshot version from supported version range in release version.
	 */
	public static final String SUPPORTED_VERSION_RANGE = "[1.0.0-SNAPSHOT],[1.0.0,1.1.0)";

	/**
	 * Returns the active modification proxy.
	 * @return The proxy.
	 */
	public CommonModificationProxy getProxy () {
		return proxy;
	}

	/**
	 * Handles the {@link cpw.mods.fml.common.event.FMLPreInitializationEvent} event.
	 * @param event The event.
	 */
	@Mod.EventHandler
	protected void onPreInitialize (FMLPreInitializationEvent event) {
		this.logger = event.getModLog ();

		this.getLogger ().info ("Pre Initialization");
		long startTime = System.currentTimeMillis ();

		this.getProxy ().preInitialize (event);

		this.getLogger ().info ("Pre Initialization End (" + (System.currentTimeMillis () - startTime) + "ms elapsed)");
	}

	/**
	 * Handles the {@link cpw.mods.fml.common.event.FMLInitializationEvent} event.
	 * @param event The event.
	 */
	@Mod.EventHandler
	protected void onInitialize (FMLInitializationEvent event) {
		this.getLogger ().info ("Initialization");
		long startTime = System.currentTimeMillis ();

		this.getProxy ().initialize (event);

		this.getLogger ().info ("Initialization End (" + (System.currentTimeMillis () - startTime) + "ms elapsed)");
	}

	/**
	 * Handles the {@link cpw.mods.fml.common.event.FMLPostInitializationEvent} event.
	 * @param event The event.
	 */
	@Mod.EventHandler
	protected void onPostInitialize (FMLPostInitializationEvent event) {
		this.getLogger ().info ("Post Initialization");
		long startTime = System.currentTimeMillis ();

		this.getProxy ().postInitialize (event);

		this.getLogger ().info ("Post Initialization End (" + (System.currentTimeMillis () - startTime) + "ms elapsed)");
	}
}
