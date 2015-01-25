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
import appeng.tile.events.AETileEventHandler;
import appeng.tile.grid.AENetworkPowerTile;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.InvOperation;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import rocks.spud.mc.basedefense.BaseDefenseModification;
import rocks.spud.mc.basedefense.api.registry.annotation.common.BlockEntityType;
import rocks.spud.mc.basedefense.api.registry.annotation.common.RegistrationCriteria;
import rocks.spud.mc.basedefense.api.registry.criteria.SurveillanceFeatureCriteria;
import rocks.spud.mc.basedefense.api.surveillance.network.cache.ISecurityGridCache;
import rocks.spud.mc.basedefense.api.surveillance.network.event.communication.SecurityDoorRequestEvent;
import rocks.spud.mc.basedefense.api.surveillance.network.event.controller.SecurityControllerUpdateEvent;
import rocks.spud.mc.basedefense.common.block.surveillance.SecurityDoorBlock;
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
	 * Stores the active cooldown period.
	 */
	@Getter
	@Setter
	private int activeCooldown = 0;

	/**
	 * Stores the entity state.
	 */
	@Getter
	private boolean state = true;

	/**
	 * Defines the door cooldown.
	 */
	@Getter
	@Setter
	private int cooldown = 300;

	/**
	 * Defines the door rotation.
	 */
	@Getter
	@Setter
	private int doorRotation = 0;

	/**
	 * Defines the door extension amount.
	 */
	@Getter
	private int extensionCount = 2;

	/**
	 * Constructs a new SecurityDoorControllerBlockEntity.
	 */
	public SecurityDoorControllerBlockEntity () {
		super ();

		this.internalMaxPower = PowerMultiplier.CONFIG.multiply (10);
		this.gridProxy.setFlags (GridFlags.REQUIRE_CHANNEL);
		this.internalPublicPowerStorage = true;
		this.internalPowerFlow = AccessRestriction.WRITE;

		this.addNewHandler (new AETileEventHandler () {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void Tick () {
				super.Tick ();

				// skip ticks on the client side
				if (worldObj.isRemote) return;

				// handle automatic door closing
				if (activeCooldown < 0) return;
				activeCooldown = Math.max (0, (activeCooldown - 1));
				if (activeCooldown == 0) closeDoor ();
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void writeToNBT (NBTTagCompound data) {
				super.writeToNBT (data);

				data.setInteger ("activeCooldown", activeCooldown);
				data.setBoolean ("state", state);
				data.setInteger ("cooldown", cooldown);
				data.setInteger ("doorRotation", doorRotation);
				data.setInteger ("extensionCount", extensionCount);
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void readFromNBT (NBTTagCompound data) {
				super.readFromNBT (data);

				activeCooldown = data.getInteger ("activeCooldown");
				state = data.getBoolean ("state");
				cooldown = data.getInteger ("cooldown");
				doorRotation = data.getInteger ("doorRotation");
				extensionCount = data.getInteger ("extensionCount");
			}
		});
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
	 * Handles door requests.
	 * @param event The event.
	 */
	@MENetworkEventSubscribe
	public void onDoorRequest (SecurityDoorRequestEvent event) {
		this.activeCooldown = (event.isCooldownEnabled () ? this.cooldown : -1);

		if (event.isState ())
			this.openDoor ();
		else
			this.closeDoor ();
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
	 * Opens the door.
	 */
	public void openDoor () {
		if (this.state) return;

		// update blocks
		for (int i = 1; i <= this.extensionCount; i++) {
			if (!this.setBlockRelative (i, BaseDefenseModification.getInstance ().getProxy ().getRegistry ().getInstance (SecurityDoorBlock.class), this.doorRotation)) break;
		}
		this.state = true;

		// update BE
		this.worldObj.markTileEntityChunkModified (this.xCoord, this.yCoord, this.zCoord, this);
		this.worldObj.markBlockForUpdate (this.xCoord, this.yCoord, this.zCoord);
	}

	/**
	 * Closes the door.
	 */
	public void closeDoor () {
		if (!this.state) return;

		// update blocks
		for (int i = 1; i <= this.extensionCount; i++) {
			if (!this.setBlockRelative (i, Blocks.air, 0)) break;
		}
		this.state = false;

		// update BE
		this.worldObj.markTileEntityChunkModified (this.xCoord, this.yCoord, this.zCoord, this);
		this.worldObj.markBlockForUpdate (this.xCoord, this.yCoord, this.zCoord);
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
	 * Sets a block relative to the BE position.
	 * @param distance The distance.
	 * @param blockType The block type.
	 * @param metadata The block metadata.
	 */
	protected boolean setBlockRelative (int distance, Block blockType, int metadata) {
		int x = this.xCoord;
		int y = this.yCoord;
		int z = this.zCoord;

		switch (this.getRotation ()) {
			case DOWN:
				y -= distance;
				break;
			case UP:
				y += distance;
				break;
			case NORTH:
				z -= distance;
				break;
			case SOUTH:
				z += distance;
				break;
			case WEST:
				x -= distance;
				break;
			case EAST:
				x += distance;
				break;
		}

		if (this.worldObj.isAirBlock (x, y, z) && !(this.worldObj.getBlock (x, y, z) instanceof SecurityDoorBlock)) return false;
		this.worldObj.setBlock (x, y, z, blockType, metadata, 2);
		return true;
	}

	/**
	 * Sets the extension count.
	 * @param extensionCount The extension count.
	 */
	public void setExtensionCount (int extensionCount) {
		state = this.isState ();
		if (state) this.closeDoor ();
		this.extensionCount = extensionCount;
		if (state) this.openDoor ();
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
