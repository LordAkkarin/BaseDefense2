/*
 * Copyright 2015 Johannes Donath <johannesd@torchmind.com>
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

package rocks.spud.mc.basedefense.api.registry.annotation.client;

import net.minecraft.item.Item;
import net.minecraftforge.client.IItemRenderer;
import rocks.spud.mc.basedefense.api.registry.ScannerPriorityType;
import rocks.spud.mc.basedefense.api.registry.annotation.common.RegistrationAnnotation;
import rocks.spud.mc.basedefense.api.registry.annotation.common.ScannerPriority;
import rocks.spud.mc.basedefense.api.registry.scanner.client.ItemRendererRegistryScanner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides an annotation for automated render type registrations.
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
@Retention (RetentionPolicy.RUNTIME)
@Target (ElementType.TYPE)
@RegistrationAnnotation (value = ItemRendererRegistryScanner.class, type = IItemRenderer.class)
@ScannerPriority (ScannerPriorityType.LOW)
public @interface ItemRendererType {

	/**
	 * Defines the item type.
	 * @return The item type.
	 */
	public Class<? extends Item> value ();
}