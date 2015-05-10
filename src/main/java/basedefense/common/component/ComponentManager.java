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
package basedefense.common.component;

import basedefense.client.component.IClientComponent;
import basedefense.server.component.IServerComponent;
import com.google.common.collect.ImmutableMap;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.minecraftforge.common.config.Configuration;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Provides a simple manager for {@link ICommonComponent} instances.
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
@RequiredArgsConstructor
public class ComponentManager {
        private ImmutableMap<Class<? extends ICommonComponent>, ICommonComponent> activeComponents = null;
        private final Set<Class<? extends IClientComponent>> clientComponents = new HashSet<> ();
        private final Set<Class<? extends IServerComponent>> serverComponents = new HashSet<> ();

        /**
         * Activates all supported and enabled components.
         * @param event The event.
         */
        @SuppressWarnings ("unchecked")
        public void activateComponents (@NonNull final FMLPreInitializationEvent event) {
                ImmutableMap.Builder<Class<? extends ICommonComponent>, ICommonComponent> componentBuilder = ImmutableMap.builder ();
                Configuration configuration = new Configuration (event.getSuggestedConfigurationFile ());

                Stream<ICommonComponent> componentStream = this.initializeComponents (((Stream) (event.getSide () == Side.CLIENT ? this.clientComponents : this.serverComponents).stream ()));

                componentStream
                        .filter (c -> c.isActivated (event, configuration))
                                .forEach (c -> {
                                        componentBuilder.put (c.getClass ().getSuperclass ().asSubclass (ICommonComponent.class), c);
                                        event.getModLog ().info ("Loaded " + c.getClass ().getCanonicalName () + " component.");
                                });

                this.activeComponents = componentBuilder.build ();
                configuration.save ();
        }

        /**
         * Initializes a component class.
         * @param component The type.
         * @return The instance.
         */
        @SneakyThrows
        private ICommonComponent initializeComponent (Class<? extends ICommonComponent> component) {
                Constructor<? extends ICommonComponent> constructor = component.getDeclaredConstructor (ComponentManager.class);
                return constructor.newInstance (this);
        }

        /**
         * Initializes a set of components.
         * @param classStream The class stream.
         * @return The instance set.
         */
        private Stream<ICommonComponent> initializeComponents (Stream<Class<? extends ICommonComponent>> classStream) {
                Stream.Builder<ICommonComponent> streamBuilder = Stream.builder ();
                classStream.forEach (c -> streamBuilder.add (this.initializeComponent (c)));
                return streamBuilder.build ();
        }

        /**
         * Registers a component.
         * @param serverComponent The server-side component.
         * @param clientComponent The client-side component.
         */
        public void registerComponent (@NonNull Class<? extends IServerComponent> serverComponent, @NonNull Class<? extends IClientComponent> clientComponent) {
                this.serverComponents.add (serverComponent);
                this.clientComponents.add (clientComponent);
        }

        /**
         * Executes a method on all active components.
         * @param consumer The consumer.
         */
        public void run (@NonNull Consumer<ICommonComponent> consumer) {
                this.activeComponents.values ().stream ().forEach (consumer);
        }
}
