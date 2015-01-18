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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import rocks.spud.mc.basedefense.BaseDefenseModification;
import rocks.spud.mc.basedefense.api.registry.annotation.common.BlockEntityType;
import rocks.spud.mc.basedefense.api.registry.annotation.common.RegistrationCriteria;
import rocks.spud.mc.basedefense.api.registry.criteria.SurveillanceFeatureCriteria;
import rocks.spud.mc.basedefense.api.surveillance.network.cache.ISecurityGridCache;
import rocks.spud.mc.basedefense.api.surveillance.network.entity.ISecurityNetworkDetector;
import rocks.spud.mc.basedefense.api.surveillance.network.event.controller.SecurityControllerUpdateEvent;
import rocks.spud.mc.basedefense.common.network.cache.SecurityGridCache;

/**
 * Provides a block entity for the camera block.
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
@BlockEntityType (CameraBlockEntity.IDENTIFIER)
@RegistrationCriteria (SurveillanceFeatureCriteria.class)
public class CameraBlockEntity extends AENetworkPowerTile implements ISecurityNetworkDetector {

	/**
	 * Defines the block entity identifier.
	 */
	public static final String IDENTIFIER = "surveillance_camera";

	/**
	 * Defines the animation rotation max.
	 */
	public static final float ANIMATION_ROTATION_MAX = 45.0f;

	/**
	 * Defines the animation speed.
	 */
	public static final float ANIMATION_ROTATION_SPEED = 0.005f;

	/**
	 * Stores the internal stub inventory.
	 */
	public static final AppEngInternalInventory INVENTORY = new AppEngInternalInventory (null, 0);

	/**
	 * Stores the internal stub inventory slots.
	 */
	public static final int[] SIDES = new int[0];

	/**
	 * Stores the body rotation.
	 */
	@SideOnly (Side.CLIENT)
	private float bodyRotation = 0.0f;

	/**
	 * Stores the body rotation direction.
	 */
	@SideOnly (Side.CLIENT)
	private int bodyRotationDirection = 1;

	/**
	 * Constructs a new CameraBlockEntity.
	 */
	public CameraBlockEntity () {
		super ();

		this.internalMaxPower = PowerMultiplier.CONFIG.multiply (15);
		this.gridProxy.setFlags (GridFlags.REQUIRE_CHANNEL);
		this.internalPublicPowerStorage = true;
		this.internalPowerFlow = AccessRestriction.WRITE;
	}

	/**
	 * Returns the camera body rotation.
	 * @param increaseRotation Indicates whether the rotation shall be increased or not.
	 * @return The rotation.
	 */
	@SideOnly (Side.CLIENT)
	public float getBodyRotation (boolean increaseRotation) {
		if (increaseRotation) {
			this.bodyRotation = Math.min (1.0f, Math.max (-1.0f, (this.bodyRotation + (ANIMATION_ROTATION_SPEED * this.bodyRotationDirection))));
			if (this.bodyRotation <= -1.0f || this.bodyRotation >= 1.0f) this.bodyRotationDirection *= -1;
		}

		return (this.bodyRotation * ANIMATION_ROTATION_MAX);
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
		int meta = 0;

		try {
			SecurityGridCache cache = this.getProxy ().getGrid ().getCache (ISecurityGridCache.class);

			switch (cache.getControllerState ()) {
				case OFFLINE:
					meta = 0;
					break;
				case ONLINE:
					meta = 1;
					break;
				case CONFLICT:
					meta = 0;
					break;
			}

			if (!this.getProxy ().isPowered () || this.getProxy ().getPath ().getControllerState () != ControllerState.CONTROLLER_ONLINE)
				meta = 0;
		} catch (GridAccessException ex) {
			BaseDefenseModification.getInstance ().getLogger ().warn ("Could not access grid from block entity %s.", this.getLocation ().toString ());
			meta = 0;
		}

		meta = (meta << 0x2);
		meta |= (this.blockMetadata & 0x3);
		System.out.println (Integer.toBinaryString (meta));

		this.worldObj.setBlockMetadataWithNotify (this.xCoord, this.yCoord, this.zCoord, meta, 2);
	}
}
