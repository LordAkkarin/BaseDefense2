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

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

/**
 * Provides a base definition for components.
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
public interface ICommonComponent {

        /**
         * Checks whether a component is activated.
         * @param event The event.
         * @param configuration The modification configuration.
         * @return True if activated.
         */
        boolean isActivated (FMLPreInitializationEvent event, Configuration configuration);

        /**
         * Registers a set of {@link net.minecraft.stats.Achievement} implementations.
         */
        void registerAchievements ();

        /**
         * Registers a set of {@link net.minecraft.block.Block} implementations.
         */
        void registerBlocks ();

        /**
         * Registers a set of {@link net.minecraft.tileentity.TileEntity} implementations.
         */
        void registerBlockEntities ();

        /**
         * Registers dungeon loot hooks.
         */
        void registerDungeonLoot ();

        /**
         * Registers a set of {@link net.minecraft.entity.Entity} implementations.
         */
        void registerEntities ();

        /**
         * Registers a set of {@link net.minecraft.item.Item} implementations.
         */
        void registerItems ();
}
