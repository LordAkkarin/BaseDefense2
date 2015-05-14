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
package basedefense.client.renderer.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

/**
 * Provides a base implementation of {@link IItemRenderer}.
 *
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
public abstract class AbstractItemRenderer implements IItemRenderer {

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean handleRenderType (ItemStack item, ItemRenderType type) {
                return true;
        }

        /**
         * Renders the entity version of an item.
         *
         * @param itemStack The item stack.
         * @param data      Additional data.
         */
        protected abstract void renderEntity (ItemStack itemStack, Object... data);

        /**
         * Renders the equipped version of an item.
         *
         * @param type      The render type.
         * @param itemStack The item stack.
         * @param data      Additional data.
         */
        protected abstract void renderEquipped (ItemRenderType type, ItemStack itemStack, Object... data);

        /**
         * Renders the inventory version of an item.
         *
         * @param itemStack The item stack.
         * @param data      Additional data.
         */
        protected abstract void renderInventory (ItemStack itemStack, Object... data);

        /**
         * {@inheritDoc}
         */
        @Override
        public void renderItem (ItemRenderType type, ItemStack item, Object... data) {
                GL11.glPushMatrix ();
                {
                        switch (type) {
                                case ENTITY:
                                        this.renderEntity (item, data);
                                        break;
                                case INVENTORY:
                                        this.renderInventory (item, data);
                                        break;
                                case EQUIPPED:
                                        this.setupEquippedCommon (item, data);
                                        this.setupEquipped (item, data);
                                        this.renderEquipped (type, item, data);
                                        break;
                                case EQUIPPED_FIRST_PERSON:
                                        this.setupEquippedCommon (item, data);
                                        this.setupEquippedFirstPerson (item, data);
                                        this.renderEquipped (type, item, data);
                                        break;
                        }
                }
                GL11.glPopMatrix ();
        }

        /**
         * Prepares the matrix for equipped renders.
         *
         * @param itemStack The item stack.
         * @param data      Additional data.
         */
        protected void setupEquipped (ItemStack itemStack, Object... data) { }

        /**
         * Prepares the matrix for equpped render types.
         *
         * @param itemStack The item stack.
         * @param data      Additional data.
         */
        protected void setupEquippedCommon (ItemStack itemStack, Object... data) { }

        /**
         * Prepares the matrix for equipped (first person) renders.
         *
         * @param itemStack The item stack.
         * @param data      Additional data.
         */
        protected void setupEquippedFirstPerson (ItemStack itemStack, Object... data) { }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean shouldUseRenderHelper (ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
                return true;
        }
}
