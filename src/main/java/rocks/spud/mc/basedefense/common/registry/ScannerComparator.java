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

package rocks.spud.mc.basedefense.common.registry;

import rocks.spud.mc.basedefense.api.registry.ScannerPriorityType;
import rocks.spud.mc.basedefense.api.registry.annotation.common.ScannerPriority;

import java.lang.annotation.Annotation;
import java.util.Comparator;

/**
 * Provides a comparator for scanners.
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public class ScannerComparator implements Comparator<Class<? extends Annotation>> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare (Class<? extends Annotation> s1, Class<? extends Annotation> s2) {
		ScannerPriorityType p1 = (s1.isAnnotationPresent (ScannerPriority.class) ? s1.getAnnotation (ScannerPriority.class).value () : ScannerPriorityType.NORMAL);
		ScannerPriorityType p2 = (s2.isAnnotationPresent (ScannerPriority.class) ? s2.getAnnotation (ScannerPriority.class).value () : ScannerPriorityType.NORMAL);

		return (Math.max (-1, Math.min (1, (p2.numeric - p1.numeric))));
	}
}
