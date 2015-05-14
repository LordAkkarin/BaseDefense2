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
package basedefense.common.creative;

import basedefense.util.RegistrationUtility;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

/**
 * Provides a creative tab for surveillance blocks and items.
 *
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
public class SurveillanceCreativeTab extends CreativeTabs {
        public static final SurveillanceCreativeTab TAB = new SurveillanceCreativeTab ();

        protected SurveillanceCreativeTab () {
                super (RegistrationUtility.getTranslation (null, "surveillance"));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Item getTabIconItem () {
                return Items.apple; // TODO: FIX MEH!
        }
}
