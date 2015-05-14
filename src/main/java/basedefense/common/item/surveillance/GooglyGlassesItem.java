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
package basedefense.common.item.surveillance;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

/**
 * Provides a pair of glasses ... ...
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
public class GooglyGlassesItem extends ItemArmor {
        private static final ArmorMaterial MATERIAL = EnumHelper.addArmorMaterial ("googly", 33, new int[]{ 1, 3, 2, 1 }, 0);
        public static final GooglyGlassesItem ITEM = new GooglyGlassesItem ();
        public static final String NAME = "surveillance_googly_glasses";
        public static final String TRANSLATION = "surveillance.googly_glasses";

        protected GooglyGlassesItem () {
                super (MATERIAL, 4, 0);

                this.setUnlocalizedName (TRANSLATION);
                this.setTextureName ("basedefense2:googly_eye_glasses");
                this.setMaxStackSize (1);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getArmorTexture (ItemStack stack, Entity entity, int slot, String type) {
                return "basedefense2:textures/items/googly_eye_glasses_layer1.png";
        }

        /**
         * Checks whether the local player is wearing the glasses.
         * @return True if wearing.
         */
        @SideOnly (Side.CLIENT)
        public boolean isPlayerWearing () {
                ItemStack itemStack = FMLClientHandler.instance ().getClientPlayerEntity ().inventory.armorItemInSlot (3);
                return (itemStack != null && itemStack.getItem () == this);
        }
}
