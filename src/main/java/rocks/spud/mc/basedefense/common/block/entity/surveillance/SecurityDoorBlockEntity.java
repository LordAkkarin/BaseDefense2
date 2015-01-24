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

package rocks.spud.mc.basedefense.common.block.entity.surveillance;

import net.minecraft.tileentity.TileEntity;
import rocks.spud.mc.basedefense.api.registry.annotation.common.BlockEntityType;
import rocks.spud.mc.basedefense.api.registry.annotation.common.RegistrationCriteria;
import rocks.spud.mc.basedefense.api.registry.criteria.SurveillanceFeatureCriteria;

/**
 * Provides a dummy block entity for security doors.
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
@BlockEntityType (SecurityDoorBlockEntity.IDENTIFIER)
@RegistrationCriteria (SurveillanceFeatureCriteria.class)
public class SecurityDoorBlockEntity extends TileEntity {

	/**
	 * Defines the block entity identifier.
	 */
	public static final String IDENTIFIER = "surveillance_security_door";
}
