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

import basedefense.common.creative.SurveillanceCreativeTab;
import basedefense.util.RegistrationUtility;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.NonNull;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

/**
 * Provides an item for acquiring DNA vials from players and living entities.
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
public class DNANeedleItem extends Item {
        public static final DNANeedleItem ITEM = new DNANeedleItem ();
        public static final String NAME = RegistrationUtility.getName ("surveillance", "dna_needle");

        protected DNANeedleItem () {
                super ();

                this.setUnlocalizedName (RegistrationUtility.getTranslation ("surveillance", "dna_needle"));
                this.setTextureName ("basedefense2:dna_needle");
                this.setCreativeTab (SurveillanceCreativeTab.TAB);
                this.setHasSubtypes (true);

                this.setMaxStackSize (1);
                this.setMaxDamage (8);
                this.setNoRepair ();
        }

        /**
         * {@inheritDoc}
         */
        @SideOnly (Side.CLIENT)
        @SuppressWarnings ("unchecked")
        @Override
        public void addInformation (ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
                super.addInformation (p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
                VialType type = getVialType (p_77624_1_);

                NBTTagCompound compound = p_77624_1_.getTagCompound ();

                switch (type) {
                        case NONE: break;
                        case EMPTY:
                                p_77624_3_.add (LanguageRegistry.instance ().getStringLocalization ("basedefense.gui.surveillance.dna_needle.empty"));
                                break;
                        case PLAYER:
                                p_77624_3_.add (String.format (LanguageRegistry.instance ().getStringLocalization ("basedefense.gui.surveillance.dna_needle.player"), compound.getString ("playerName")));
                                break;
                        case ENTITY:
                                p_77624_3_.add (LanguageRegistry.instance ().getStringLocalization ("basedefense.gui.surveillance.dna_needle.entity")); // TODO: Find a reliable way of displaying an entity name
                                break;
                }
        }

        /**
         * Retrieves the type of vial attached to the item.
         * @param itemStack The item stack.
         * @return The vial type.
         */
        public static VialType getVialType (@NonNull ItemStack itemStack) {
                NBTTagCompound compound = itemStack.getTagCompound ();
                if (compound == null) return VialType.NONE;
                if (!compound.hasKey ("vial") || !compound.getBoolean ("vial")) return VialType.NONE;

                if (compound.hasKey ("playerID")) return VialType.PLAYER;
                if (compound.hasKey ("entityID")) return VialType.ENTITY;
                return VialType.EMPTY;
        }

        /**
         * Checks whether a needle item has an empty vial attached to it.
         * @param itemStack The item stack.
         * @return True if an empty vial is present.
         */
        public static boolean isVialEmpty (@NonNull ItemStack itemStack) {
                NBTTagCompound compound = itemStack.getTagCompound ();
                return compound != null && !(compound.hasKey ("playerID") || compound.hasKey ("entityID"));
        }

        /**
         * Checks whether a needle item has a vial attached to it.
         * @param itemStack The item stack.
         * @return True if a vial is present.
         */
        public static boolean hasVial (@NonNull ItemStack itemStack) {
                NBTTagCompound compound = itemStack.getTagCompound ();
                return compound != null && compound.hasKey ("vial") && compound.getBoolean ("vial");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hitEntity (ItemStack p_77644_1_, EntityLivingBase p_77644_2_, EntityLivingBase p_77644_3_) {
                if (!isVialEmpty (p_77644_1_)) return false;
                if (!hasVial (p_77644_1_)) return false;

                NBTTagCompound compound = p_77644_1_.getTagCompound ();

                if (p_77644_2_ instanceof EntityPlayer) {
                        compound.setString ("playerID", p_77644_2_.getPersistentID ().toString ());
                        compound.setString ("playerName", ((EntityPlayer) p_77644_2_).getDisplayName ());
                } else
                        compound.setString ("entityID", p_77644_2_.getPersistentID ().toString ());

                if (!(p_77644_3_ instanceof EntityPlayer) || !((EntityPlayer) p_77644_3_).capabilities.isCreativeMode)
                        p_77644_1_.damageItem (1, p_77644_2_);

                return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ItemStack onItemRightClick (ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
                NBTTagCompound sourceCompound = p_77659_1_.getTagCompound ();
                if (sourceCompound == null) sourceCompound = new NBTTagCompound ();

                if (hasVial (p_77659_1_)) {
                        VialType vialType = getVialType (p_77659_1_);
                        ItemStack stack = new ItemStack (VialItem.ITEM, 1, (vialType == VialType.EMPTY ? 0 : (vialType == VialType.PLAYER ? 1 : 2)));

                        if (vialType != VialType.EMPTY) {
                                NBTTagCompound compound = stack.getTagCompound ();
                                if (compound == null) compound = new NBTTagCompound ();

                                switch (vialType) {
                                        case PLAYER:
                                                compound.setString ("playerID", sourceCompound.getString ("playerID"));
                                                compound.setString ("playerName", sourceCompound.getString ("playerName"));
                                                break;
                                        case ENTITY:
                                                compound.setString ("entityID", sourceCompound.getString ("entityID"));
                                                break;
                                }

                                stack.setTagCompound (compound);
                        }

                        sourceCompound.setBoolean ("vial", false);
                        sourceCompound.removeTag ("playerID");
                        sourceCompound.removeTag ("playerName");
                        sourceCompound.removeTag ("entityID");

                        p_77659_3_.inventory.addItemStackToInventory (stack);
                } else {
                        ItemStack vialStack = new ItemStack (VialItem.ITEM, 1, 0);
                        if (!p_77659_3_.inventory.hasItemStack (vialStack)) return p_77659_1_;

                        for (int i = 0; i < InventoryPlayer.getHotbarSize (); i++) {
                                ItemStack stack = p_77659_3_.inventory.mainInventory[i];
                                if (stack == null || stack.getItem () != vialStack.getItem () || stack.getItemDamage () != vialStack.getItemDamage ()) continue;
                                stack.stackSize--;

                                if (stack.stackSize == 0) p_77659_3_.inventory.mainInventory[i] = null;
                                break;
                        }

                        sourceCompound.setBoolean ("vial", true);
                }

                p_77659_1_.setTagCompound (sourceCompound);
                return p_77659_1_;
        }

        /**
         * {@inheritDoc}
         */
        @SuppressWarnings ("unchecked")
        @Override
        public void getSubItems (Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_) {
                super.getSubItems (p_150895_1_, p_150895_2_, p_150895_3_);

                ItemStack itemStack = new ItemStack (p_150895_1_, 1, 0);
                NBTTagCompound compound = itemStack.getTagCompound ();
                if (compound == null) compound = new NBTTagCompound ();
                compound.setBoolean ("vial", true);
                itemStack.setTagCompound (compound);

                p_150895_3_.add (itemStack);
        }

        /**
         * Provides a list of valid vial types.
         */
        public enum VialType {
                NONE,
                EMPTY,
                PLAYER,
                ENTITY
        }
}
