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

package rocks.spud.mc.basedefense.api.registry.scanner.client;

import cpw.mods.fml.relauncher.Side;
import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.item.Item;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rocks.spud.mc.basedefense.api.error.RegistryRegistrationException;
import rocks.spud.mc.basedefense.api.registry.IModificationRegistry;
import rocks.spud.mc.basedefense.api.registry.IRegistryScanner;
import rocks.spud.mc.basedefense.api.registry.annotation.client.ItemRendererType;

/**
 * Provides a scanner for automatically registering item render types.
 *
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public class ItemRendererRegistryScanner implements IRegistryScanner<ItemRendererType> {

	/**
	 * Stores an internal logger.
	 */
	@Getter (AccessLevel.PROTECTED)
	private static final Logger logger = LogManager.getFormatterLogger (ItemRendererRegistryScanner.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Side getSide () {
		return Side.CLIENT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object scanType (IModificationRegistry registry, ItemRendererType annotation, Class<?> type) throws RegistryRegistrationException {
		Item item = registry.getInstance (annotation.value ());
		if (item == null) {
			getLogger ().warn ("Could not find item of type %s: Skipping registration of renderer type %s", annotation.value ().getCanonicalName (), type.getCanonicalName ());
			return null;
		}

		Class<? extends IItemRenderer> rendererType = type.asSubclass (IItemRenderer.class);
		IItemRenderer renderer = registry.constructInstance (rendererType);

		MinecraftForgeClient.registerItemRenderer (item, renderer);
		return renderer;
	}
}
