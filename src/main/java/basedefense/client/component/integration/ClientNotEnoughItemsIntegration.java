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
package basedefense.client.component.integration;

import basedefense.common.component.ComponentManager;
import basedefense.common.component.integration.CommonNotEnoughItemsIntegration;
import basedefense.common.item.surveillance.GooglyGlassesItem;
import codechicken.nei.api.API;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Provides a client side implementation of {@link CommonNotEnoughItemsIntegration}.
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
public class ClientNotEnoughItemsIntegration extends CommonNotEnoughItemsIntegration implements IClientIntegration {

        public ClientNotEnoughItemsIntegration (ComponentManager componentManager) {
                super (componentManager);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void activate () {
                super.activate ();

                this.hideItems ();
        }

        /**
         * Hides items that do not belong in NEI ...
         */
        protected void hideItems () {
                API.hideItem (new ItemStack (GooglyGlassesItem.ITEM, OreDictionary.WILDCARD_VALUE));
        }
}
