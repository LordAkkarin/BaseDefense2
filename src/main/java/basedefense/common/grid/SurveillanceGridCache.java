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
package basedefense.common.grid;

import appeng.api.networking.IGrid;
import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IGridStorage;
import basedefense.api.surveillance.ISurveillanceGridCache;
import basedefense.api.surveillance.ISurveillanceNetworkController;
import basedefense.api.surveillance.ISurveillanceNetworkController.ControllerType;
import basedefense.api.surveillance.event.SurveillanceControllerChange;
import com.google.common.collect.Iterables;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Provides an implementation of {@link ISurveillanceGridCache}.
 *
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
@RequiredArgsConstructor
public class SurveillanceGridCache implements ISurveillanceGridCache {
        private final IGrid grid;
        private final Set<ISurveillanceNetworkController> controllerSet = new HashSet<ISurveillanceNetworkController> ();
        private ControllerState state = ControllerState.OFFLINE;
        private boolean updatePending = false;

        /**
         * {@inheritDoc}
         */
        @Override
        public void addNode (IGridNode iGridNode, IGridHost iGridHost) {
                if (!(iGridHost instanceof ISurveillanceNetworkController)) return;
                this.controllerSet.add (((ISurveillanceNetworkController) iGridHost));

                this.updatePending = true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ISurveillanceNetworkController getController () {
                return Iterables.getFirst (controllerSet, null);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ControllerState getControllerState () {
                return this.state;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onJoin (IGridStorage iGridStorage) { }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onSplit (IGridStorage iGridStorage) { }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onUpdateTick () {
                if (!this.updatePending) return;
                ControllerState oldState = this.getControllerState ();

                if (controllerSet.size () > 1)
                        this.state = ControllerState.CONFLICT;
                else if (controllerSet.size () == 0)
                        this.state = ControllerState.OFFLINE;
                else {
                        ISurveillanceNetworkController controller = this.getController ();

                        this.state = (controller.getControllerType () == ControllerType.FACE_RECOGNITION ? ControllerState.ACTIVE_FACE_RECOGNITION : ControllerState.ACTIVE_PRIMITIVE);
                }

                if (this.state != oldState)
                        this.grid.postEvent (new SurveillanceControllerChange (this.getController (), this.state));
                this.updatePending = false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void populateGridStorage (IGridStorage iGridStorage) { }

        /**
         * {@inheritDoc}
         */
        @Override
        public void removeNode (IGridNode iGridNode, IGridHost iGridHost) {
                if (!(iGridHost instanceof ISurveillanceNetworkController)) return;
                this.controllerSet.remove (iGridHost);

                this.updatePending = true;
        }
}
