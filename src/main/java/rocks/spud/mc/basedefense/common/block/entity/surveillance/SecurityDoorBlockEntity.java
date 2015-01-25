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

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import rocks.spud.mc.basedefense.api.registry.annotation.common.BlockEntityType;
import rocks.spud.mc.basedefense.api.registry.annotation.common.RegistrationCriteria;
import rocks.spud.mc.basedefense.api.registry.criteria.SurveillanceFeatureCriteria;
import rocks.spud.mc.basedefense.common.block.surveillance.SecurityDoorBlock;
import rocks.spud.mc.basedefense.common.block.surveillance.SecurityDoorControllerBlock;

import java.util.Random;

/**
 * Provides a dummy block entity for security doors.
 *
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
@BlockEntityType (SecurityDoorBlockEntity.IDENTIFIER)
@RegistrationCriteria (SurveillanceFeatureCriteria.class)
public class SecurityDoorBlockEntity extends TileEntity {

	/**
	 * Defines the block entity identifier.
	 */
	public static final String IDENTIFIER = "surveillance_security_door";

	/**
	 * Stores the decay timer.
	 */
	private int decayTimer = 20;

	/**
	 * Constructs a new SecurityDoorBlockEntity.
	 */
	public SecurityDoorBlockEntity () {
		super ();

		this.decayTimer = (new Random ()).nextInt (120);
		this.decayTimer = Math.max (20, this.decayTimer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateEntity () {
		super.updateEntity ();

		// skip client side execution
		if (this.worldObj.isRemote) return;

		// update timer
		this.decayTimer = Math.max (0, (this.decayTimer - 1));
		if (decayTimer > 0) return;

		int metadata = this.worldObj.getBlockMetadata (this.xCoord, this.yCoord, this.zCoord);
		Block block = null;
		int blockMetadata = 0;

		// handle rotation around Y axis
		if (metadata <= 4) {
			if ((metadata & 0x1) != 0x1) {
				block = this.worldObj.getBlock (this.xCoord, (this.yCoord - 1), this.zCoord);
				metadata = this.worldObj.getBlockMetadata (this.xCoord, (this.yCoord - 1), this.zCoord);
			} else {
				block = this.worldObj.getBlock (this.xCoord, (this.yCoord + 1), this.zCoord);
				metadata = this.worldObj.getBlockMetadata (this.xCoord, (this.yCoord + 1), this.zCoord);
			}
		} else {
			ForgeDirection direction = ForgeDirection.getOrientation ((metadata - 2));

			int x = this.xCoord;
			int z = this.zCoord;

			switch (direction) {
				case NORTH:
					z--;
					break;
				case SOUTH:
					z++;
					break;
				case WEST:
					x--;
					break;
				case EAST:
					x++;
					break;
			}

			block = this.worldObj.getBlock (x, this.yCoord, z);
			blockMetadata = this.worldObj.getBlockMetadata (x, this.yCoord, z);
		}

		// decay
		if (block == null || (!(block instanceof SecurityDoorBlock) && !(block instanceof SecurityDoorControllerBlock))) {
			this.worldObj.setBlockToAir (this.xCoord, this.yCoord, this.zCoord);
			return;
		}

		if (block instanceof SecurityDoorControllerBlock && (metadata & 0x1) != 0x1) {
			this.worldObj.setBlockToAir (this.xCoord, this.yCoord, this.zCoord);
			return;
		}

		this.decayTimer = 20;
	}
}
