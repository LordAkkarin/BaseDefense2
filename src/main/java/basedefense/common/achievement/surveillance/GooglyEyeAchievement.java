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
package basedefense.common.achievement.surveillance;

import basedefense.common.achievement.AbstractAchievement;
import basedefense.common.achievement.BaseDefenseAchievementPage;
import basedefense.common.item.surveillance.GooglyGlassesItem;
import basedefense.util.RegistrationUtility;
import cpw.mods.fml.relauncher.ReflectionHelper;
import lombok.NonNull;
import lombok.SneakyThrows;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Provides an achievement for acquiring the {@link basedefense.common.item.surveillance.GooglyGlassesItem} item.
 *
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
public class GooglyEyeAchievement extends AbstractAchievement {
        public static final GooglyEyeAchievement ACHIEVEMENT = new GooglyEyeAchievement ();

        public GooglyEyeAchievement () {
                super (RegistrationUtility.getStatName ("surveillance", "googly_eye"), RegistrationUtility.getTranslation ("surveillance", "googly_eyes"), -2, -2, GooglyGlassesItem.ITEM, null);

                this.initIndependentStat ();
                this.setSpecial ();

                // Note: We intentionally place this here to hide the achievement
        }

        /**
         * {@inheritDoc}
         */
        @SuppressWarnings ("unchecked")
        @Override
        public void award (@NonNull EntityPlayer player) {
                if (player.worldObj.isRemote) {
                        BaseDefenseAchievementPage.PAGE.addAchievement (this);
                        AchievementList.achievementList.add (this);
                }

                super.award (player);
        }

        /**
         * {@inheritDoc}
         */
        @SuppressWarnings ("unchecked")
        @SneakyThrows
        @Override
        public Achievement registerStat () {
                StatList.allStats.add (this);

                Field oneShotStats = ReflectionHelper.findField (StatList.class, "oneShotStats");
                oneShotStats.setAccessible (true);
                Map map = ((Map) oneShotStats.get (null));
                map.put (this.statId, this);
                return this;
        }
}
