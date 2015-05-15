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
import basedefense.util.TranslationUtility;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * Provides an item for storing player and living entity DNA samples.
 *
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
public class VialItem extends Item {
        public static final VialItem ITEM = new VialItem ();
        public static final String NAME = RegistrationUtility.getName ("surveillance", "vial");

        private IIcon icon_empty;
        private IIcon icon_player;
        private IIcon icon_entity;

        private VialItem () {
                super ();

                this.setUnlocalizedName (TranslationUtility.getComponentTranslation ("surveillance", "vial"));
                this.setCreativeTab (SurveillanceCreativeTab.TAB);

                this.setMaxStackSize (1);
                this.setMaxDamage (0);
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

                NBTTagCompound compound = p_77624_1_.getTagCompound ();
                if (compound == null) return;

                switch (p_77624_1_.getItemDamage ()) {
                        case 1:
                                if (!compound.hasKey ("playerName")) return;
                                p_77624_3_.add (String.format (LanguageRegistry.instance ().getStringLocalization ("basedefense.gui.surveillance.vial.player"), compound.getString ("playerName")));
                                break;
                        case 2:
                                p_77624_3_.add (LanguageRegistry.instance ().getStringLocalization ("basedefense.gui.surveillance.vial.entity"));
                                break;
                }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public IIcon getIconFromDamage (int p_77617_1_) {
                switch (p_77617_1_) {
                        case 1:
                                return this.icon_player;
                        case 2:
                                return this.icon_entity;
                        default:
                                return this.icon_empty;
                }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public IIcon getIconIndex (ItemStack p_77650_1_) {
                return this.getIconFromDamage (p_77650_1_.getItemDamage ());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void registerIcons (IIconRegister p_94581_1_) {
                this.icon_empty = p_94581_1_.registerIcon ("basedefense2:surveillance_vial_empty");
                this.icon_player = p_94581_1_.registerIcon ("basedefense2:surveillance_vial_dna_player");
                this.icon_entity = p_94581_1_.registerIcon ("basedefense2:surveillance_vial_dna_mob");
        }
}
