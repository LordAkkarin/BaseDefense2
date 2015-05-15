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
package basedefense.client.renderer.item.surveillance;

import basedefense.common.item.surveillance.GooglyGlassesItem;
import com.google.common.collect.ImmutableMap;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

/**
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
public class GooglyEyeRenderer {
        private static final IModelCustom EYE_MODEL = AdvancedModelLoader.loadModel (new ResourceLocation ("basedefense2", "models/surveillance_googly_eye.obj"));

        /**
         * Handles {@link RenderLivingEvent}.
         *
         * @param event The event.
         */
        @SubscribeEvent
        public void onRenderLiving (RenderLivingEvent.Post event) {
                EntityLivingBase entity = event.entity;
                if (!GooglyGlassesItem.ITEM.isPlayerWearing ()) return;

                EntityConfiguration configuration = EntityConfiguration.valueOf (entity.getClass ());
                if (configuration == null) return;

                GL11.glPushMatrix ();
                {
                        GL11.glTranslated (event.x, event.y, event.z);
                        GL11.glTranslatef (0.0f, configuration.getRotationCenterY (), 0.0f);

                        GL11.glRotatef (entity.getRotationYawHead (), 0.0f, -1.0f, 0.0f);

                        GL11.glPushMatrix ();
                        {
                                GL11.glRotatef (entity.rotationPitch, 1.0f, 0.0f, 0.0f);
                                GL11.glTranslatef (0.0f, configuration.getEyeOffsetY (), configuration.getEyeOffsetZ ());

                                this.renderModel (configuration.getEyeOffsetX (), configuration.getEyeScale ());
                                this.renderModel (-configuration.getEyeOffsetX (), configuration.getEyeScale ());
                        }
                        GL11.glPopMatrix ();
                }
                GL11.glPopMatrix ();
        }

        /**
         * Renders the model at a certain distance from the center.
         *
         * @param distance The distance.
         * @param scale    The model scale.
         */
        private void renderModel (float distance, float scale) {
                GL11.glPushMatrix ();
                {
                        GL11.glTranslatef (distance, 0.0f, 0.0f);
                        GL11.glScalef (scale, scale, scale);

                        Minecraft.getMinecraft ().renderEngine.bindTexture (new ResourceLocation ("basedefense2", "textures/models/surveillance_googly_eye.png"));
                        EYE_MODEL.renderAll ();
                }
                GL11.glPopMatrix ();
        }

        /**
         * Provides a list of valid entity configurations.
         */
        public enum EntityConfiguration {
                CREEPER (EntityCreeper.class) {
                        @Override
                        public float getRotationCenterY () {
                                return 1.275f;
                        }

                        @Override
                        public float getEyeOffsetX () {
                                return 0.125f;
                        }

                        @Override
                        public float getEyeOffsetY () {
                                return 0.3f;
                        }

                        @Override
                        public float getEyeOffsetZ () {
                                return 0.25f;
                        }
                },
                GHAST (EntityGhast.class) {
                        @Override
                        public float getRotationCenterY () {
                                return 2.0f;
                        }

                        @Override
                        public float getEyeOffsetX () {
                                return 1.25f;
                        }

                        @Override
                        public float getEyeOffsetY () {
                                return 1.0f;
                        }

                        @Override
                        public float getEyeOffsetZ () {
                                return 2.5f;
                        }

                        @Override
                        public float getEyeScale () {
                                return 1;
                        }
                },
                SKELETON (EntitySkeleton.class) {
                        @Override
                        public float getRotationCenterY () {
                                return 1.520f;
                        }

                        @Override
                        public float getEyeOffsetX () {
                                return 0.125f;
                        }

                        @Override
                        public float getEyeOffsetY () {
                                return 0.25f;
                        }

                        @Override
                        public float getEyeOffsetZ () {
                                return 0.25f;
                        }
                },
                VILLAGER (EntityVillager.class) {
                        @Override
                        public float getRotationCenterY () {
                                return 1.45f;
                        }

                        @Override
                        public float getEyeOffsetX () {
                                return 0.125f;
                        }

                        @Override
                        public float getEyeOffsetY () {
                                return 0.225f;
                        }

                        @Override
                        public float getEyeOffsetZ () {
                                return 0.25f;
                        }
                },
                ZOMBIE (EntityZombie.class) {
                        @Override
                        public float getRotationCenterY () {
                                return 1.520f;
                        }

                        @Override
                        public float getEyeOffsetX () {
                                return 0.125f;
                        }

                        @Override
                        public float getEyeOffsetY () {
                                return 0.25f;
                        }

                        @Override
                        public float getEyeOffsetZ () {
                                return 0.25f;
                        }
                };
                private static final ImmutableMap<Class<? extends EntityLivingBase>, EntityConfiguration> entityMap;
                private final Class<? extends EntityLivingBase> entityType;

                static {
                        ImmutableMap.Builder<Class<? extends EntityLivingBase>, EntityConfiguration> mapBuilder = ImmutableMap.builder ();

                        for (EntityConfiguration configuration : values ()) {
                                mapBuilder.put (configuration.entityType, configuration);
                        }

                        entityMap = mapBuilder.build ();
                }

                EntityConfiguration (Class<? extends EntityLivingBase> entityType) {
                        this.entityType = entityType;
                }

                public abstract float getEyeOffsetX ();

                public abstract float getEyeOffsetY ();

                public abstract float getEyeOffsetZ ();

                public float getEyeScale () {
                        return 0.1f;
                }

                public abstract float getRotationCenterY ();

                public static EntityConfiguration valueOf (Class<? extends EntityLivingBase> entityType) {
                        return entityMap.get (entityType);
                }
        }
}
