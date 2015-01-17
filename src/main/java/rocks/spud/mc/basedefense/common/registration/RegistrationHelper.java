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

import appeng.api.AEApi;
import appeng.api.networking.IGridCache;
import com.google.common.base.Preconditions;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
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
import rocks.spud.mc.basedefense.common.registration.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.*;

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
	 * Stores a list of initialized and registered block types.
	 */
	private final Map<Class<? extends Block>, Block> blockMap = new HashMap<Class<? extends Block>, Block> ();

	/**
	 * Stores a list of initialized and registered block renderers.
	 */
	private final Map<Class<? extends ISimpleBlockRenderingHandler>, Integer> blockRendererMap = new HashMap<Class<? extends ISimpleBlockRenderingHandler>, Integer> ();

	/**
	 * Stores a list of initialized and registered item types.
	 */
	private final Map<Class<? extends Item>, Item> itemMap = new HashMap<Class<? extends Item>, Item> ();

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
	 * Returns a registered block.
	 * @param blockType The block type.
	 * @return The block.
	 */
	public Block getBlock (Class<? extends Block> blockType) {
		return this.blockMap.get (blockType);
	}

	/**
	 * Returns a registered renderer ID.
	 * @param rendererType The renderer type.
	 * @return The renderer ID.
	 */
	public int getBlockRendererID (Class<? extends ISimpleBlockRenderingHandler> rendererType) {
		return this.blockRendererMap.get (rendererType);
	}

	/**
	 * Returns a registered item.
	 * @param itemType The item type.
	 * @return The item.
	 */
	public Item getItem (Class<? extends Item> itemType) {
		return this.itemMap.get (itemType);
	}

	/**
	 * Checks whether a block type has been registered.
	 * @param blockType The block type.
	 * @return True if registered.
	 */
	public boolean hasBlock (Class<? extends Block> blockType) {
		return this.blockMap.containsKey (blockType);
	}

	/**
	 * Checks whether an item type has been registered.
	 * @param itemType The item type.
	 * @return True if registered.
	 */
	public boolean hasItem (Class<? extends Item> itemType) {
		return this.itemMap.containsKey (itemType);
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
		this.blockMap.put (blockType, block);
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
	 * Registers a block renderer.
	 * @param rendererTypes The renderer set.
	 */
	public void registerBlockRenderer (@NonNull Collection<Class<? extends ISimpleBlockRenderingHandler>> rendererTypes) {
		for (Class<? extends ISimpleBlockRenderingHandler> rendererType : rendererTypes) this.registerBlockRenderer (rendererType);
		getLogger ().info ("Registered %s block renderers.", rendererTypes.size ());
	}

	/**
	 * Registers a block renderer.
	 * @param rendererType The renderer type.
	 */
	public void registerBlockRenderer (@NonNull Class<? extends ISimpleBlockRenderingHandler> rendererType) {
		this.registerBlockRenderer (this.createInstance (rendererType));
	}

	/**
	 * Registers a block renderer.
	 * @param renderer The renderer.
	 */
	public void registerBlockRenderer (@NonNull ISimpleBlockRenderingHandler renderer) {
		Class<? extends ISimpleBlockRenderingHandler> rendererType = renderer.getClass ();
		Preconditions.checkArgument (rendererType.isAnnotationPresent (BlockRendererDefinition.class), "@BlockRendererDefinition annotation is not present for type %s", rendererType.getCanonicalName ());

		int renderID = RenderingRegistry.getNextAvailableRenderId ();
		RenderingRegistry.registerBlockHandler (renderID, renderer);
		this.blockRendererMap.put (rendererType, renderID);
	}

	/**
	 * Registers a set of grid caches.
	 * @param gridCacheTypes The grid cache type collection.
	 */
	public void registerGridCache (@NonNull Collection<Class<? extends IGridCache>> gridCacheTypes) {
		for (Class<? extends IGridCache> gridCacheType : gridCacheTypes) this.registerGridCache (gridCacheType);
		getLogger ().info ("Registered %s grid cache types.", gridCacheTypes.size ());
	}

	/**
	 * Registers a grid cache type.
	 * @param gridCacheType The grid cache type.
	 */
	public void registerGridCache (@NonNull Class<? extends IGridCache> gridCacheType) {
		Preconditions.checkArgument (gridCacheType.isAnnotationPresent (GridCacheDefinition.class));
		if (!StandardCriteria.INTEGRATION_AE2.isMet (this.getConfiguration ())) {
			getLogger ().info ("Skipping registration of grid cache %s: Missing AE2 installation", gridCacheType.getCanonicalName ());
			return;
		}
		if (!this.checkCriteria (gridCacheType)) return;
		AEApi.instance ().registries ().gridCache ().registerGridCache (gridCacheType.getAnnotation (GridCacheDefinition.class).value (), gridCacheType);
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
		this.itemMap.put (itemType, item);
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

		this.registerBlockRenderer (this.getAnnotatedTypes (BlockRendererDefinition.class, ISimpleBlockRenderingHandler.class));

		getLogger ().info ("Finished scanning in %sms", (System.currentTimeMillis () - startTime));
	}

	/**
	 * Scans the current class loader for annotated classes.
	 */
	protected void scanServerAnnotations () {
		getLogger ().info ("Scanning for server side (common) registrations ...");
		long startTime = System.currentTimeMillis ();

		this.registerBlock (this.getAnnotatedTypes (BlockDefinition.class, Block.class));
		this.registerItem (this.getAnnotatedTypes (ItemDefinition.class, Item.class));
		this.registerBlockEntity (this.getAnnotatedTypes (BlockEntityDefinition.class, TileEntity.class));
		this.registerGridCache (this.getAnnotatedTypes (GridCacheDefinition.class, IGridCache.class));

		getLogger ().info ("Finished scanning in %sms", (System.currentTimeMillis () - startTime));
	}
}
