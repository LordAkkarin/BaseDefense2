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

package rocks.spud.mc.basedefense.api.registry.scanner.common;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.tileentity.TileEntity;
import rocks.spud.mc.basedefense.api.error.RegistryRegistrationException;
import rocks.spud.mc.basedefense.api.registry.IModificationRegistry;
import rocks.spud.mc.basedefense.api.registry.IRegistryScanner;
import rocks.spud.mc.basedefense.api.registry.annotation.common.BlockEntityType;

/**
 * Provides a registry scanner for block entities.
 *
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public class BlockEntityTypeRegistryScanner implements IRegistryScanner<BlockEntityType> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Side getSide () {
		return Side.SERVER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object scanType (IModificationRegistry registry, BlockEntityType annotation, Class<?> type) throws RegistryRegistrationException {
		Class<? extends TileEntity> entityType = type.asSubclass (TileEntity.class);
		GameRegistry.registerTileEntity (entityType, annotation.value ());

		return null;
	}
}
