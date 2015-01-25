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

package rocks.spud.mc.basedefense.common.item.block.surveillance;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import rocks.spud.mc.basedefense.common.block.entity.surveillance.SecurityDoorControllerBlockEntity;

/**
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public class SecurityDoorControllerBlockItem extends ItemBlock {

	/**
	 * Constructs a new SecurityDoorControllerBlockItem.
	 *
	 * @param p_i45328_1_ The parent block.
	 */
	public SecurityDoorControllerBlockItem (Block p_i45328_1_) {
		super (p_i45328_1_);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean placeBlockAt (ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
		ForgeDirection direction = ForgeDirection.getOrientation (side);
		metadata = SecurityDoorControllerBlockEntity.buildMetadata (false, direction);

		return super.placeBlockAt (stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
	}
}
