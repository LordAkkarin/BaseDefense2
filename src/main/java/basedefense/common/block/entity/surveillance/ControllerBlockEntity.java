/*
 * Copyright 2015 Johannes Donath <johannesd@torchmind.com>
 * and other copyright owners as documented in the project's IP log.
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
package basedefense.common.block.entity.surveillance;

import appeng.api.config.AccessRestriction;
import appeng.api.config.PowerMultiplier;
import appeng.api.networking.GridFlags;
import appeng.api.networking.events.MENetworkControllerChange;
import appeng.api.networking.events.MENetworkEventSubscribe;
import appeng.api.networking.events.MENetworkPowerStatusChange;
import appeng.me.GridAccessException;
import appeng.tile.TileEvent;
import appeng.tile.events.TileEventType;
import appeng.tile.grid.AENetworkPowerTile;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.tile.inventory.InvOperation;
import basedefense.BaseDefenseModification;
import basedefense.api.surveillance.ISurveillanceGridCache;
import basedefense.api.surveillance.ISurveillanceGridCache.ControllerState;
import basedefense.api.surveillance.ISurveillanceNetworkController;
import basedefense.api.surveillance.event.SurveillanceControllerChange;
import basedefense.api.surveillance.event.SurveillanceSystemStateChange;
import basedefense.common.block.surveillance.ControllerBlock;
import basedefense.util.RegistrationUtility;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Provides a {@link net.minecraft.tileentity.TileEntity} implementation for {@link basedefense.common.block.surveillance.ControllerBlock}.
 *
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
public class ControllerBlockEntity extends AENetworkPowerTile implements ISurveillanceNetworkController {
        public static final AppEngInternalInventory INVENTORY = new AppEngInternalInventory (null, 0);
        public static final String NAME = RegistrationUtility.getName ("surveillance", "controller");
        public static final int[] SIDES = new int[0];

        private SystemState systemState = SystemState.ARMED;
        private long alertTimeout = 0;

        public ControllerBlockEntity () {
                super ();

                this.internalMaxPower = PowerMultiplier.CONFIG.multiply (40);
                this.gridProxy.setFlags (GridFlags.REQUIRE_CHANNEL);
                this.internalPublicPowerStorage = true;
                this.internalPowerFlow = AccessRestriction.WRITE;
        }

        /**
         * Reads the NBT data.
         *
         * @param compound The tag compound.
         */
        @TileEvent (TileEventType.WORLD_NBT_READ)
        protected void Controller_readFromNBT (NBTTagCompound compound) {
                this.systemState = SystemState.valueOf (compound.getString ("systemState"));
                this.alertTimeout = compound.getLong ("alertTimeout");
        }

        /**
         * Writes the NBT data.
         *
         * @param compound The tag compound.
         */
        @TileEvent (TileEventType.WORLD_NBT_WRITE)
        protected void Controller_writeToNBT (NBTTagCompound compound) {
                compound.setString ("systemState", this.getSystemState ().name ());
                compound.setLong ("alertTimeout", this.alertTimeout);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int[] getAccessibleSlotsBySide (ForgeDirection forgeDirection) {
                return SIDES;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ControllerType getControllerType () {
                return ((this.worldObj.getBlockMetadata (this.xCoord, this.yCoord, this.zCoord) & 0x4) == 0x4 ? ControllerType.FACE_RECOGNITION : ControllerType.PRIMITIVE);
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
        public SystemState getSystemState () {
                return this.systemState;
        }

        @Override
        public void setSystemState (SystemState state) {
                if (this.systemState != state) {
                        this.systemState = state;

                        try {
                                this.getProxy ().getGrid ().postEvent (new SurveillanceSystemStateChange (this, state));
                        } catch (GridAccessException ex) {
                                BaseDefenseModification.getInstance ().getLogger ().error ("Could not access grid: " + ex.getMessage (), ex);
                        }
                }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onChangeInventory (IInventory iInventory, int i, InvOperation invOperation, ItemStack itemStack, ItemStack itemStack1) { }

        /**
         * Handles the {@link MENetworkControllerChange} event.
         *
         * @param event The event.
         */
        @MENetworkEventSubscribe
        public void onControllerChange (MENetworkControllerChange event) {
                this.updateMetadata ();
        }

        /**
         * Handles the {@link MENetworkPowerStatusChange} event.
         *
         * @param event The event.
         */
        @MENetworkEventSubscribe
        public void onPowerStatusChange (MENetworkPowerStatusChange event) {
                this.updateMetadata ();
        }

        /**
         * handles the {@link SurveillanceControllerChange} event.
         *
         * @param event The event.
         */
        @MENetworkEventSubscribe
        public void onSurveillanceControllerChange (SurveillanceControllerChange event) {
                this.updateMetadata ();
        }

        /**
         * Handles block entity updates.
         */
        @TileEvent (TileEventType.TICK)
        protected void update () {
                this.alertTimeout = Math.max (0, (this.alertTimeout - 1));
                if (this.getSystemState () == SystemState.ALERT && this.alertTimeout == 0)
                        this.setSystemState (SystemState.ARMED);
        }

        /**
         * Updates the block metadata.
         */
        protected void updateMetadata () {
                int metadata = this.worldObj.getBlockMetadata (this.xCoord, this.yCoord, this.zCoord);
                ControllerState oldState = ControllerBlock.BLOCK.getState (metadata);
                ControllerState state;

                try {
                        if (!this.getProxy ().isPowered ())
                                state = ControllerState.OFFLINE;
                        else if (this.getProxy ().getPath ().getControllerState () != appeng.api.networking.pathing.ControllerState.CONTROLLER_ONLINE)
                                state = ControllerState.OFFLINE;
                        else {
                                ISurveillanceGridCache cache = this.getProxy ().getGrid ().getCache (ISurveillanceGridCache.class);
                                state = cache.getControllerState ();
                        }
                } catch (GridAccessException ex) {
                        BaseDefenseModification.getInstance ().getLogger ().error ("Cannot access grid: " + ex.getMessage (), ex);
                        state = ControllerState.OFFLINE;
                }

                if (state != oldState) {
                        metadata = ControllerBlock.BLOCK.buildState (state, metadata);
                        this.worldObj.setBlockMetadataWithNotify (this.xCoord, this.yCoord, this.zCoord, metadata, 2);
                }
        }
}
