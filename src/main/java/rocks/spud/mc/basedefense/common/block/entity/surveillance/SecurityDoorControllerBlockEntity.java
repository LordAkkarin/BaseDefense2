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
import appeng.api.networking.events.MENetworkControllerChange;
import appeng.api.networking.events.MENetworkEventSubscribe;
import appeng.api.networking.events.MENetworkPowerStatusChange;
import appeng.api.networking.pathing.ControllerState;
import appeng.api.util.AECableType;
import appeng.me.GridAccessException;
import appeng.tile.grid.AENetworkPowerTile;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.InvOperation;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import rocks.spud.mc.basedefense.BaseDefenseModification;
import rocks.spud.mc.basedefense.api.registry.annotation.common.BlockEntityType;
import rocks.spud.mc.basedefense.api.registry.annotation.common.RegistrationCriteria;
import rocks.spud.mc.basedefense.api.registry.criteria.SurveillanceFeatureCriteria;
import rocks.spud.mc.basedefense.api.surveillance.network.cache.ISecurityGridCache;
import rocks.spud.mc.basedefense.api.surveillance.network.event.controller.SecurityControllerUpdateEvent;
import rocks.spud.mc.basedefense.common.network.cache.SecurityGridCache;

import java.util.Arrays;

/**
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
@BlockEntityType (SecurityDoorControllerBlockEntity.IDENTIFIER)
@RegistrationCriteria (SurveillanceFeatureCriteria.class)
public class SecurityDoorControllerBlockEntity extends AENetworkPowerTile {

	/**
	 * Defines the BE identifier.
	 */
	public static final String IDENTIFIER = "surveillance_security_door_controller";

	/**
	 * Stores the internal stub inventory.
	 */
	public static final AppEngInternalInventory INVENTORY = new AppEngInternalInventory (null, 0);

	/**
	 * Stores the internal stub inventory slots.
	 */
	public static final int[] SIDES = new int[0];

	/**
	 * Constructs a new SecurityDoorControllerBlockEntity.
	 */
	public SecurityDoorControllerBlockEntity () {
		super ();

		this.internalMaxPower = PowerMultiplier.CONFIG.multiply (10);
		this.gridProxy.setFlags (GridFlags.REQUIRE_CHANNEL);
		this.internalPublicPowerStorage = true;
		this.internalPowerFlow = AccessRestriction.WRITE;
	}

	/**
	 * Builds the block metadata based on the block state.
	 * @param active Indicates whether the camera is active.
	 * @param direction Defines the output direction.
	 * @return The metadata.
	 */
	public static int buildMetadata (boolean active, ForgeDirection direction) {
		return ((active ? 0x1 : 0x0) | (Arrays.binarySearch (ForgeDirection.VALID_DIRECTIONS, direction) << 1));
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

	/**
	 * Returns the camera orientation.
	 * @return The orientation.
	 */
	public ForgeDirection getRotation () {
		int metadata = this.worldObj.getBlockMetadata (this.xCoord, this.yCoord, this.zCoord);
		metadata >>= 0x1;
		return ForgeDirection.getOrientation (metadata);
	}

	/**
	 * Checks whether a camera is active.
	 * @return True if active.
	 */
	public boolean isActive () {
		int metadata = this.worldObj.getBlockMetadata (this.xCoord, this.yCoord, this.zCoord);
		return ((metadata & 0x1) == 0x1);
	}

	/**
	 * Handles controller updates.
	 * @param event The event.
	 */
	@MENetworkEventSubscribe
	public void onControllerChange (MENetworkControllerChange event) {
		this.updateMetadata ();
	}

	/**
	 * Handles security controller updates.
	 * @param event The event.
	 */
	@MENetworkEventSubscribe
	public void onControllerChange (SecurityControllerUpdateEvent event) {
		this.updateMetadata ();
	}

	/**
	 * Handles power status updates.
	 * @param event The event.
	 */
	@MENetworkEventSubscribe
	public void onPowerStatusChange (MENetworkPowerStatusChange event) {
		this.updateMetadata ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onReady () {
		super.onReady ();

		this.updateMetadata ();
	}

	/**
	 * Updates the block metadata.
	 */
	public void updateMetadata () {
		boolean active = false;

		try {
			SecurityGridCache cache = this.getProxy ().getGrid ().getCache (ISecurityGridCache.class);

			switch (cache.getControllerState ()) {
				case OFFLINE:
				case CONFLICT: break;
				case ONLINE:
					active = true;
					break;
			}

			if (!this.getProxy ().isPowered () || this.getProxy ().getPath ().getControllerState () != ControllerState.CONTROLLER_ONLINE)
				active = false;
		} catch (GridAccessException ex) {
			BaseDefenseModification.getInstance ().getLogger ().warn ("Could not access grid from block entity %s.", this.getLocation ().toString ());
			active = false;
		}

		this.worldObj.setBlockMetadataWithNotify (this.xCoord, this.yCoord, this.zCoord, buildMetadata (active, this.getRotation ()), 2);
	}
}
