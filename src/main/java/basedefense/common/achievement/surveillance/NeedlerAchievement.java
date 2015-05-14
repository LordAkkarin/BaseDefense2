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
import basedefense.common.item.surveillance.DNANeedleItem;
import basedefense.util.RegistrationUtility;

/**
 * Provides an achievement for acquiring the {@link basedefense.common.item.surveillance.DNANeedleItem} item.
 *
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
public class NeedlerAchievement extends AbstractAchievement {
        private static final String NAME = "surveillance_needler";
        private static final String TRANSLATION = "basedefense.surveillance.needler";
        public static final NeedlerAchievement ACHIEVEMENT = new NeedlerAchievement ();

        public NeedlerAchievement () {
                super (RegistrationUtility.getStatName ("surveillance", "needler"), RegistrationUtility.getTranslation ("surveillance", "needler"), 0, 0, DNANeedleItem.ITEM, null);
        }
}
