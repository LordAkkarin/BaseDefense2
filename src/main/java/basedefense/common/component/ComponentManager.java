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

import basedefense.BaseDefenseModification;
import basedefense.client.component.IClientComponent;
import basedefense.client.component.integration.IClientIntegration;
import basedefense.common.component.integration.ICommonIntegration;
import basedefense.server.component.IServerComponent;
import basedefense.server.component.integration.IServerIntegration;
import com.google.common.collect.ImmutableMap;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
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
 *
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
@RequiredArgsConstructor
public class ComponentManager {
        private ImmutableMap<Class<? extends ICommonComponent>, ICommonComponent> activeComponents = null;
        private ImmutableMap<Class<? extends ICommonIntegration>, ICommonIntegration> activeIntegrations = null;
        private Configuration configuration = null;
        private final Set<Class<? extends IClientComponent>> clientComponents = new HashSet<> ();
        private final Set<Class<? extends IServerComponent>> serverComponents = new HashSet<> ();
        private final Set<Class<? extends IClientIntegration>> clientIntegrations = new HashSet<> ();
        private final Set<Class<? extends IServerIntegration>> serverIntegrations = new HashSet<> ();

        /**
         * Activates all supported and enabled components.
         *
         * @param event The event.
         */
        @SuppressWarnings ("unchecked")
        public void activateComponents (@NonNull final FMLPreInitializationEvent event) {
                ImmutableMap.Builder<Class<? extends ICommonComponent>, ICommonComponent> componentBuilder = ImmutableMap.builder ();
                this.configuration = new Configuration (event.getSuggestedConfigurationFile ());

                Stream<ICommonComponent> componentStream = this.initializeComponents (((Stream) (event.getSide () == Side.CLIENT ? this.clientComponents : this.serverComponents).stream ()));

                componentStream
                        .filter (c -> c.isActivated (event, this.configuration))
                        .forEach (c -> {
                                componentBuilder.put (c.getClass ().getSuperclass ().asSubclass (ICommonComponent.class), c);
                                event.getModLog ().info ("Loaded " + c.getClass ().getCanonicalName () + " component.");
                        });

                this.activeComponents = componentBuilder.build ();
        }

        /**
         * Activates all supported and enabled integrations.
         *
         * @param event The event.
         */
        @SuppressWarnings ("unchecked")
        public void activateIntegrations (@NonNull final FMLPostInitializationEvent event) {
                ImmutableMap.Builder<Class<? extends ICommonIntegration>, ICommonIntegration> integrationBuilder = ImmutableMap.builder ();

                Stream<ICommonIntegration> integrationStream = this.initializeIntegrations (((Stream) (event.getSide () == Side.CLIENT ? this.clientIntegrations : this.serverIntegrations).stream ()));

                integrationStream
                        .filter (i -> i.isAvailable (event, this.configuration))
                        .forEach (i -> {
                                i.activate ();

                                integrationBuilder.put (i.getClass ().getSuperclass ().asSubclass (ICommonIntegration.class), i);
                                BaseDefenseModification.getInstance ().getLogger ().info ("Loaded " + i.getClass ().getCanonicalName () + " integration.");
                        });

                this.activeIntegrations = integrationBuilder.build ();
                this.configuration.save ();
        }

        /**
         * Initializes a component class.
         *
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
         *
         * @param classStream The class stream.
         * @return The instance set.
         */
        private Stream<ICommonComponent> initializeComponents (Stream<Class<? extends ICommonComponent>> classStream) {
                Stream.Builder<ICommonComponent> streamBuilder = Stream.builder ();
                classStream.forEach (c -> streamBuilder.add (this.initializeComponent (c)));
                return streamBuilder.build ();
        }

        /**
         * Initializes an integration class.
         *
         * @param integration The type.
         * @return The instance.
         */
        @SneakyThrows
        private ICommonIntegration initializeIntegration (Class<? extends ICommonIntegration> integration) {
                Constructor<? extends ICommonIntegration> constructor = integration.getDeclaredConstructor (ComponentManager.class);
                return constructor.newInstance (this);
        }

        /**
         * Initializes a set of integrations.
         *
         * @param classStream The class stream.
         * @return The instance set.
         */
        private Stream<ICommonIntegration> initializeIntegrations (Stream<Class<? extends ICommonIntegration>> classStream) {
                Stream.Builder<ICommonIntegration> streamBuilder = Stream.builder ();
                classStream.forEach (c -> streamBuilder.add (this.initializeIntegration (c)));
                return streamBuilder.build ();
        }

        /**
         * Registers a component.
         *
         * @param serverComponent The server-side component.
         * @param clientComponent The client-side component.
         */
        public void registerComponent (@NonNull Class<? extends IServerComponent> serverComponent, @NonNull Class<? extends IClientComponent> clientComponent) {
                this.serverComponents.add (serverComponent);
                this.clientComponents.add (clientComponent);
        }

        /**
         * Registers a client integration.
         *
         * @param serverIntegration The server-side integration.
         * @param clientIntegration The client-side integration.
         */
        public void registerIntegration (@NonNull Class<? extends IServerIntegration> serverIntegration, @NonNull Class<? extends IClientIntegration> clientIntegration) {
                this.serverIntegrations.add (serverIntegration);
                this.clientIntegrations.add (clientIntegration);
        }

        /**
         * Executes a method on all active components.
         *
         * @param consumer The consumer.
         */
        public void run (@NonNull Consumer<ICommonComponent> consumer) {
                this.activeComponents.values ().stream ().forEach (consumer);
        }
}
