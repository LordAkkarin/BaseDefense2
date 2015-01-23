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

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import rocks.spud.mc.basedefense.api.error.RegistryException;
import rocks.spud.mc.basedefense.api.error.RegistryInitializationException;
import rocks.spud.mc.basedefense.api.error.RegistryRegistrationException;
import rocks.spud.mc.basedefense.api.registry.IModificationRegistry;
import rocks.spud.mc.basedefense.api.registry.IRegistrationCriteria;
import rocks.spud.mc.basedefense.api.registry.IRegistryScanner;
import rocks.spud.mc.basedefense.api.registry.annotation.common.RegistrationAnnotation;
import rocks.spud.mc.basedefense.api.registry.annotation.common.RegistrationCriteria;
import rocks.spud.mc.basedefense.api.registry.scanner.common.RegistryScannerTypeScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Provides a modification registry.
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public class ModificationRegistry implements IModificationRegistry {

	/**
	 * Stores an internal logger.
	 */
	@Getter (AccessLevel.PROTECTED)
	private static final Logger logger = LogManager.getFormatterLogger (ModificationRegistry.class);

	/**
	 * Stores the active configuration instance.
	 */
	@Getter (AccessLevel.PROTECTED)
	private final Configuration configuration;

	/**
	 * Stores a cache for registration criteria.
	 */
	private Map<Class<? extends IRegistrationCriteria>, Boolean> criteriaCache = new HashMap<Class<? extends IRegistrationCriteria>, Boolean> ();

	/**
	 * Stores a cache for constructed objects.
	 */
	private Map<Class<?>, Object> objectCache = new HashMap<Class<?>, Object> ();

	/**
	 * Stores a cache of registered renderer identifiers.
	 */
	private Map<Class<?>, Integer> rendererIdentifierCache = new HashMap<Class<?>, Integer> ();

	/**
	 * Stores the registry scanner map.
	 */
	private Map<Class<? extends Annotation>, IRegistryScanner> scannerRegistry = new LinkedHashMap<Class<? extends Annotation>, IRegistryScanner> ();

	/**
	 * Stores the filter map.
	 */
	private Map<Class<? extends Annotation>, Class<?>> filterRegistry = new HashMap<Class<? extends Annotation>, Class<?>> ();

	/**
	 * Constructs a new ModificationRegistry.
	 * @param configuration The active configuration.
	 */
	public ModificationRegistry (Configuration configuration) {
		this.configuration = configuration;

		this.registerDefaultScanners ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean checkCriteria (@NonNull Class<? extends IRegistrationCriteria> criteriaType) {
		try {
			if (!this.criteriaCache.containsKey (criteriaType)) this.criteriaCache.put (criteriaType, this.constructInstance (criteriaType).isMet (this.getConfiguration ()));
			return this.criteriaCache.get (criteriaType);
		} catch (RegistryException ex) {
			getLogger ().info ("Could not verify criteria of type " + criteriaType.getCanonicalName () + ": " + ex.getMessage ());
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T constructInstance (@NonNull Class<T> type) throws RegistryInitializationException {
		try {
			Constructor<T> constructor = type.getDeclaredConstructor ();
			constructor.setAccessible (true);
			return constructor.newInstance ();
		} catch (Exception ex) {
			throw new RegistryInitializationException (String.format ("Could not initialize type %s: %s", type.getCanonicalName (), ex.getMessage ()), ex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getRendererIdentifier (@NonNull Class<?> rendererType) {
		if (!this.rendererIdentifierCache.containsKey (rendererType)) this.rendererIdentifierCache.put (rendererType, RenderingRegistry.getNextAvailableRenderId ());
		return this.rendererIdentifierCache.get (rendererType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T getInstance (@NonNull Class<T> type) {
		return ((T) this.objectCache.get (type));
	}

	/**
	 * Registers all default scanners.
	 */
	protected void registerDefaultScanners () {
		this.registerScanner (RegistrationAnnotation.class, Annotation.class, new RegistryScannerTypeScanner ());
		this.scanPackage ("rocks.spud.mc.basedefense.api.registry.annotation", RegistrationAnnotation.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerScanner (@NonNull Class<? extends Annotation> annotationType, Class<?> filter, @NonNull IRegistryScanner scanner) {
		if (this.scannerRegistry.containsKey (annotationType)) getLogger ().warn ("Scanner for type %s has already been registered. Skipping registration for %s", annotationType.getCanonicalName (), scanner.getClass ().getCanonicalName ());
		this.scannerRegistry.put (annotationType, scanner);
		if (filter != null) this.filterRegistry.put (annotationType, filter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scanPackage (String packageName) {
		for (Map.Entry<Class<? extends Annotation>, IRegistryScanner> scannerEntry : this.scannerRegistry.entrySet ()) this.scanPackage (packageName, scannerEntry.getKey ());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scanPackage (@NonNull String packageName, @NonNull Class<? extends Annotation> annotationType) {
		Reflections reflections = new Reflections (
			(new ConfigurationBuilder ())
				.addUrls (ClasspathHelper.forPackage (packageName))
		);

		// cache parameters
		Set<Class<?>> typeSet = reflections.getTypesAnnotatedWith (annotationType);
		IRegistryScanner scanner = this.scannerRegistry.get (annotationType);
		Class<?> typeFilter = this.filterRegistry.get (annotationType);

		// apply scanner
		for (Class<?> type : typeSet) {
			// check whether filters match
			if (typeFilter != null && !typeFilter.isAssignableFrom (type)) {
				getLogger ().warn ("Type %s does not match filter %s for annotation %s. Skipping registration.", type.getCanonicalName (), typeFilter.getCanonicalName (), annotationType.getCanonicalName ());
				continue;
			}

			// check whether criteria matches
			if (type.isAnnotationPresent (RegistrationCriteria.class)) {
				boolean result = true;

				for (Class<? extends IRegistrationCriteria> criteriaType : type.getAnnotation (RegistrationCriteria.class).value ()) {
					if (!this.checkCriteria (criteriaType)) {
						result = false;
						break;
					}
				}

				if (!result) continue;
			}

			// execute scanner
			try {
				if (scanner.getSide () == Side.SERVER || FMLCommonHandler.instance ().getEffectiveSide () == Side.CLIENT) scanner.scanType (this, type.getAnnotation (annotationType), type);
				getLogger ().info ("Registered type %s via annotation %s (scanner: %s).", type.getCanonicalName (), annotationType.getCanonicalName (), scanner.getClass ().getCanonicalName ());
			} catch (RegistryRegistrationException ex) {
				getLogger ().warn (String.format ("Could not register type %s: %s", type.getCanonicalName (), ex.getMessage ()), ex);
			}
		}
	}
}
