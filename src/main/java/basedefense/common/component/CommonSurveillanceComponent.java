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
package basedefense.common.component;

import basedefense.common.achievement.surveillance.GooglyEyeAchievement;
import basedefense.common.achievement.surveillance.NeedlerAchievement;
import basedefense.common.item.surveillance.DNANeedleItem;
import basedefense.common.item.surveillance.GooglyGlassesItem;
import basedefense.common.item.surveillance.VialItem;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.config.Configuration;

/**
 * Provides an {@link ICommonComponent} implementation for surveillance related gameplay elements.
 *
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
public abstract class CommonSurveillanceComponent extends AbstractCommonComponent {

        public CommonSurveillanceComponent (ComponentManager componentManager) {
                super (componentManager);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isActivated (FMLPreInitializationEvent event, Configuration configuration) {
                return configuration.getBoolean ("surveillance", "feature", true, "Enables surveillance features (cameras, security bots, etc).") && Loader.isModLoaded ("appliedenergistics2");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void registerAchievements () {
                GooglyEyeAchievement.ACHIEVEMENT.registerStat ();
                NeedlerAchievement.ACHIEVEMENT.registerStat ();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void registerBlockEntities () {

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void registerBlocks () {

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void registerDungeonLoot () {
                ChestGenHooks.addItem (ChestGenHooks.BONUS_CHEST, new WeightedRandomChestContent (new ItemStack (GooglyGlassesItem.ITEM, 1), 1, 1, 7));
                ChestGenHooks.addItem (ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent (new ItemStack (GooglyGlassesItem.ITEM, 1), 1, 2, 10));
                ChestGenHooks.addItem (ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent (new ItemStack (GooglyGlassesItem.ITEM, 1), 1, 1, 5));
                ChestGenHooks.addItem (ChestGenHooks.PYRAMID_DESERT_CHEST, new WeightedRandomChestContent (new ItemStack (GooglyGlassesItem.ITEM, 1), 1, 1, 5));
                ChestGenHooks.addItem (ChestGenHooks.PYRAMID_JUNGLE_CHEST, new WeightedRandomChestContent (new ItemStack (GooglyGlassesItem.ITEM, 1), 1, 1, 5));
                ChestGenHooks.addItem (ChestGenHooks.STRONGHOLD_LIBRARY, new WeightedRandomChestContent (new ItemStack (GooglyGlassesItem.ITEM, 1), 1, 2, 10));
                ChestGenHooks.addItem (ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent (new ItemStack (GooglyGlassesItem.ITEM, 1), 1, 1, 2));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void registerEntities () {

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void registerItems () {
                GameRegistry.registerItem (VialItem.ITEM, VialItem.NAME);
                GameRegistry.registerItem (DNANeedleItem.ITEM, DNANeedleItem.NAME);
                GameRegistry.registerItem (GooglyGlassesItem.ITEM, GooglyGlassesItem.NAME);
        }
}
