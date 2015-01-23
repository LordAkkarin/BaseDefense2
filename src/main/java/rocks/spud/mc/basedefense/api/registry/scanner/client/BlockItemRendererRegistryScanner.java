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

import com.google.common.base.Preconditions;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import rocks.spud.mc.basedefense.api.error.RegistryRegistrationException;
import rocks.spud.mc.basedefense.api.registry.IModificationRegistry;
import rocks.spud.mc.basedefense.api.registry.IRegistryScanner;
import rocks.spud.mc.basedefense.api.registry.annotation.client.BlockItemRendererType;

/**
 * Provides a scanner for block item specific renderers.
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public class BlockItemRendererRegistryScanner implements IRegistryScanner<BlockItemRendererType> {

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
	public Object scanType (IModificationRegistry registry, BlockItemRendererType annotation, Class<?> type) throws RegistryRegistrationException {
		Block block = registry.getInstance (annotation.value ());
		Preconditions.checkNotNull (block, "block");

		Class<? extends IItemRenderer> rendererType = type.asSubclass (IItemRenderer.class);
		IItemRenderer renderer = registry.constructInstance (rendererType);

		MinecraftForgeClient.registerItemRenderer (Item.getItemFromBlock (block), renderer);

		return renderer;
	}
}
