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

package rocks.spud.mc.basedefense.common.block.entity.surveillance;

import appeng.api.config.AccessRestriction;
import appeng.api.config.PowerMultiplier;
import appeng.api.networking.GridFlags;
import appeng.api.util.AECableType;
import appeng.tile.grid.AENetworkPowerTile;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.InvOperation;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import rocks.spud.mc.basedefense.common.registration.annotation.BlockEntityDefinition;

/**
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
@BlockEntityDefinition (ControllerBlockEntity.IDENTIFIER)
public class ControllerBlockEntity extends AENetworkPowerTile {

	/**
	 * Defines the BE identifier.
	 */
	public static final String IDENTIFIER = "surveillance_controller";

	/**
	 * Stores the internal stub inventory.
	 */
	public static final AppEngInternalInventory INVENTORY = new AppEngInternalInventory (null, 0);

	/**
	 * Stores the internal stub inventory slots.
	 */
	public static final int[] SIDES = new int[0];

	/**
	 * Constructs a ControllerBlockEntity.
	 */
	public ControllerBlockEntity () {
		super ();

		this.internalMaxPower = PowerMultiplier.CONFIG.multiply (40);
		this.gridProxy.setFlags (GridFlags.REQUIRE_CHANNEL);
		this.internalPublicPowerStorage = true;
		this.internalPowerFlow = AccessRestriction.WRITE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AECableType getCableConnectionType (ForgeDirection dir) {
		return AECableType.SMART;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IInventory getInternalInventory () {
		return INVENTORY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onChangeInventory (IInventory iInventory, int i, InvOperation invOperation, ItemStack itemStack, ItemStack itemStack1) { }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int[] getAccessibleSlotsBySide (ForgeDirection forgeDirection) {
		return SIDES;
	}
}
