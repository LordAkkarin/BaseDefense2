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

package rocks.spud.mc.basedefense.api.registry.scanner.common;

import cpw.mods.fml.relauncher.Side;
import rocks.spud.mc.basedefense.api.error.RegistryRegistrationException;
import rocks.spud.mc.basedefense.api.registry.IModificationRegistry;
import rocks.spud.mc.basedefense.api.registry.IRegistryScanner;
import rocks.spud.mc.basedefense.api.registry.annotation.common.RegistrationAnnotation;

import java.lang.annotation.Annotation;

/**
 * Provides a scanner for annotation scanners.
 *
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public class RegistryScannerTypeScanner implements IRegistryScanner<RegistrationAnnotation> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Side getSide () {
		return Side.SERVER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object scanType (IModificationRegistry registry, RegistrationAnnotation annotation, Class<?> type) throws RegistryRegistrationException {
		Class<? extends Annotation> annotationType = type.asSubclass (Annotation.class);
		Class<? extends IRegistryScanner> scannerType = annotation.value ();

		IRegistryScanner scanner = registry.constructInstance (scannerType);
		registry.registerScanner (annotationType, annotation.type (), scanner);

		return null;
	}
}
