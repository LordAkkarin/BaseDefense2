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
package basedefense.common.achievement;

import basedefense.common.achievement.BaseDefenseAchievementPage;
import lombok.NonNull;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

/**
 * Provides an abstract implementation of {@link Achievement}.
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
public abstract class AbstractAchievement extends Achievement {

        public AbstractAchievement (String p_i45300_1_, String p_i45300_2_, int p_i45300_3_, int p_i45300_4_, Item p_i45300_5_, Achievement p_i45300_6_) {
                super (p_i45300_1_, p_i45300_2_, p_i45300_3_, p_i45300_4_, p_i45300_5_, p_i45300_6_);
        }

        public AbstractAchievement (String p_i45301_1_, String p_i45301_2_, int p_i45301_3_, int p_i45301_4_, Block p_i45301_5_, Achievement p_i45301_6_) {
                super (p_i45301_1_, p_i45301_2_, p_i45301_3_, p_i45301_4_, p_i45301_5_, p_i45301_6_);
        }

        public AbstractAchievement (String p_i45302_1_, String p_i45302_2_, int p_i45302_3_, int p_i45302_4_, ItemStack p_i45302_5_, Achievement p_i45302_6_) {
                super (p_i45302_1_, p_i45302_2_, p_i45302_3_, p_i45302_4_, p_i45302_5_, p_i45302_6_);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Achievement registerStat () {
                super.registerStat ();
                BaseDefenseAchievementPage.PAGE.addAchievement (this);
                return this;
        }

        /**
         * Awards the achievement to a player.
         * @param player The player.
         */
        public void award (@NonNull EntityPlayer player) {
                player.addStat (this, 1);
        }
}
