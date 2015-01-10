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

package rocks.spud.mc.basedefense.common.registration;

import com.google.common.base.Preconditions;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import rocks.spud.mc.basedefense.common.registration.annotation.BlockDefinition;
import rocks.spud.mc.basedefense.common.registration.annotation.BlockEntityDefinition;
import rocks.spud.mc.basedefense.common.registration.annotation.Criteria;
import rocks.spud.mc.basedefense.common.registration.annotation.ItemDefinition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Provides a helper for registering game elements with annotations.
 *
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
public class RegistrationHelper {

	/**
	 * Stores an internal logger instance.
	 */
	@Getter (AccessLevel.PROTECTED)
	private static final Logger logger = LogManager.getFormatterLogger (RegistrationHelper.class);

	/**
	 * Stores the active modification configuration.
	 */
	@Getter (AccessLevel.PROTECTED)
	private final net.minecraftforge.common.config.Configuration configuration;

	/**
	 * Stores the Reflections instance.
	 */
	@Getter (AccessLevel.PROTECTED)
	private final Reflections reflections;

	/**
	 * Constructs a new RegistrationHelper.
	 */
	public RegistrationHelper (net.minecraftforge.common.config.Configuration configuration) {
		this (configuration, RegistrationHelper.class.getClassLoader ());
	}

	/**
	 * Constructs a new ReflectionHelper.
	 *
	 * @param classLoader The class loader to setup for scanning.
	 */
	public RegistrationHelper (net.minecraftforge.common.config.Configuration configuration, ClassLoader classLoader) {
		this.configuration = configuration;
		this.reflections = new Reflections (this.buildConfiguration (classLoader));
	}

	/**
	 * Builds the reflection configuration.
	 *
	 * @param classLoader The parent class loader.
	 * @return The configuration.
	 */
	protected Configuration buildConfiguration (ClassLoader classLoader) {
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder ();

		configurationBuilder.setClassLoaders (new ClassLoader[] { classLoader });

		return ConfigurationBuilder.build ();
	}

	/**
	 * Checks the criteria attached to an object.
	 * @param object The object.
	 * @return True if all criteria is met.
	 */
	protected boolean checkCriteria (Object object) {
		return this.checkCriteria (object.getClass ());
	}

	/**
	 * Checks the criteria attached to a class.
	 * @param type The type.
	 * @return True if all criteria is met.
	 */
	protected boolean checkCriteria (Class<?> type) {
		if (!type.isAnnotationPresent (Criteria.class)) return true;
		Criteria criteria = type.getAnnotation (Criteria.class);

		for (StandardCriteria current : criteria.value ()) {
			if (!current.isMet (this.configuration)) return false;
		}

		return true;
	}

	/**
	 * Constructs a new instance of the specified class.
	 *
	 * @param type The type.
	 * @param <T> The type.
	 * @return The instance.
	 */
	protected <T> T createInstance (Class<T> type) {
		try {
			Constructor<T> constructor = type.getDeclaredConstructor ();
			constructor.setAccessible (true);
			return constructor.newInstance ();
		} catch (NoSuchMethodException ex) {
			throw new IllegalArgumentException (String.format ("Type %s does not have standard no-argument constructor: %s", type.getCanonicalName (), ex.getMessage ()), ex);
		} catch (Exception ex) {
			throw new IllegalStateException (String.format ("Could not initialize instance for type %s: %s", type.getCanonicalName (), ex.getMessage ()), ex);
		}
	}

	/**
	 * Returns a collection of types annotated with the specified annotation.
	 *
	 * @param annotationType The annotation type.
	 * @param type The type.
	 * @param <T> The type.
	 * @return The collection.
	 */
	protected <T> Collection<Class<? extends T>> getAnnotatedTypes (Class<? extends Annotation> annotationType, Class<T> type) {
		List<Class<? extends T>> typeList = new ArrayList<Class<? extends T>> ();
		Set<Class<?>> rawTypeList = this.getReflections ().getTypesAnnotatedWith (annotationType);

		for (Class<?> rawType : rawTypeList) {
			if (!type.isAssignableFrom (rawType))
				throw new RuntimeException (String.format ("Type %s has annotation @%s but does not extend %s", rawType.getCanonicalName (), annotationType.getSimpleName (), type.getCanonicalName ()));
			typeList.add (((Class<? extends T>) rawType));
		}

		return typeList;
	}

	/**
	 * Registers a set of blocks.
	 *
	 * @param blockTypes The block type collection.
	 */
	public void registerBlock (@NonNull Collection<Class<? extends Block>> blockTypes) {
		for (Class<? extends Block> blockType : blockTypes) this.registerBlock (blockType);
		getLogger ().info ("Registered %s block types.", blockTypes.size ());
	}

	/**
	 * Registers a new block.
	 *
	 * @param blockType The block type.
	 */
	public void registerBlock (@NonNull Class<? extends Block> blockType) {
		this.registerBlock (this.createInstance (blockType));
	}

	/**
	 * Registers a block type.
	 *
	 * @param block The block.
	 */
	public void registerBlock (@NonNull Block block) {
		Class<? extends Block> blockType = block.getClass ();
		Preconditions.checkArgument (blockType.isAnnotationPresent (BlockDefinition.class), "No @BlockDefinition annotation present for Block type %s.", blockType.getCanonicalName ());
		if (!this.checkCriteria (block)) return;
		GameRegistry.registerBlock (block, blockType.getAnnotation (BlockDefinition.class).value ());
	}

	/**
	 * Registers a set of block entities.
	 *
	 * @param blockEntityTypes The entity type collection.
	 */
	public void registerBlockEntity (@NonNull Collection<Class<? extends TileEntity>> blockEntityTypes) {
		for (Class<? extends TileEntity> entityType : blockEntityTypes) this.registerBlockEntity (entityType);
		getLogger ().info ("Registered %s block entity types.", blockEntityTypes.size ());
	}

	/**
	 * Registers a block entity.
	 *
	 * @param entityType The block entity type.
	 */
	public void registerBlockEntity (@NonNull Class<? extends TileEntity> entityType) {
		Preconditions.checkArgument (entityType.isAnnotationPresent (BlockEntityDefinition.class));
		if (!this.checkCriteria (entityType)) return;
		GameRegistry.registerTileEntity (entityType, entityType.getAnnotation (BlockEntityDefinition.class).value ());
	}

	/**
	 * Registers a set of items.
	 *
	 * @param itemTypes The item type collection.
	 */
	public void registerItem (@NonNull Collection<Class<? extends Item>> itemTypes) {
		for (Class<? extends Item> itemType : itemTypes) this.registerItem (itemType);
		getLogger ().info ("Registered %s item types.", itemTypes.size ());
	}

	/**
	 * Registers an item.
	 *
	 * @param itemType The item type.
	 */
	public void registerItem (@NonNull Class<? extends Item> itemType) {
		this.registerItem (this.createInstance (itemType));
	}

	/**
	 * Registers an item.
	 *
	 * @param item The item.
	 */
	public void registerItem (@NonNull Item item) {
		Class<? extends Item> itemType = item.getClass ();
		Preconditions.checkArgument (itemType.isAnnotationPresent (ItemDefinition.class));
		if (!this.checkCriteria (item)) return;
		GameRegistry.registerItem (item, item.getClass ().getAnnotation (ItemDefinition.class).value ());
	}

	/**
	 * Scans the current class loader for annotated classes.
	 *
	 * @param side The active side.
	 */
	public void scanAnnotations (Side side) {
		if (side == Side.CLIENT)
			this.scanClientAnnotations ();
		else
			this.scanServerAnnotations ();
	}

	/**
	 * Scans the current class loader for annotated classes.
	 */
	protected void scanClientAnnotations () {
		getLogger ().info ("Scanning for client side registrations ...");
		long startTime = System.currentTimeMillis ();

		// TODO

		getLogger ().info ("Finished scanning in %sms", (System.currentTimeMillis () - startTime));
	}

	/**
	 * Scans the current class loader for annotated classes.
	 */
	protected void scanServerAnnotations () {
		getLogger ().info ("Scanning for server side (common) registrations ...");
		long startTime = System.currentTimeMillis ();

		this.registerBlock (this.getAnnotatedTypes (BlockDefinition.class, Block.class));
		this.registerBlockEntity (this.getAnnotatedTypes (BlockEntityDefinition.class, TileEntity.class));
		this.registerItem (this.getAnnotatedTypes (ItemDefinition.class, Item.class));

		getLogger ().info ("Finished scanning in %sms", (System.currentTimeMillis () - startTime));
	}
}
