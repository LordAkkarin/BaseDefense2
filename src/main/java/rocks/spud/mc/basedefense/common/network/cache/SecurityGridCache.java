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

package rocks.spud.mc.basedefense.common.network.cache;

import appeng.api.networking.IGrid;
import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IGridStorage;
import com.google.common.collect.Iterables;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import rocks.spud.mc.basedefense.api.registry.annotation.common.GridCacheType;
import rocks.spud.mc.basedefense.api.registry.annotation.common.RegistrationCriteria;
import rocks.spud.mc.basedefense.api.registry.criteria.SurveillanceFeatureCriteria;
import rocks.spud.mc.basedefense.api.surveillance.network.cache.ISecurityGridCache;
import rocks.spud.mc.basedefense.api.surveillance.network.cache.SecurityControllerState;
import rocks.spud.mc.basedefense.api.surveillance.network.entity.ISecurityNetworkController;
import rocks.spud.mc.basedefense.api.surveillance.network.event.controller.SecurityControllerUpdateEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * Provides a grid cache for the surveillance network.
 * @author {@literal Johannes Donath <johannesd@torchmind.com>}
 */
@RequiredArgsConstructor
@GridCacheType (ISecurityGridCache.class)
@RegistrationCriteria (SurveillanceFeatureCriteria.class)
public class SecurityGridCache implements ISecurityGridCache {

	/**
	 * Indicates whether a calculation is pending for the next tick.
	 */
	@Getter
	private boolean calculationPending = false;

	/**
	 * Stores a list of known controllers within the network.
	 */
	@Getter (AccessLevel.PROTECTED)
	private final Set<ISecurityNetworkController> controllerSet = new HashSet ();

	/**
	 * Defines the current controller state within the network.
	 */
	@Getter
	private SecurityControllerState controllerState = SecurityControllerState.OFFLINE;

	/**
	 * Stores the parent grid.
	 */
	@Getter (AccessLevel.PROTECTED)
	private final IGrid grid;

	/**
	 * Re-Calculates the network state.
	 */
	protected void calculate () {
		this.calculationPending = false;
		SecurityControllerState controllerState = this.controllerState;

		if (this.getControllerSet ().size () == 0)
			this.controllerState = SecurityControllerState.OFFLINE;
		else if (this.getControllerSet ().size () == 1)
			this.controllerState = SecurityControllerState.ONLINE;
		else
			this.controllerState = SecurityControllerState.CONFLICT;

		if (controllerState != this.getControllerState ()) this.getGrid ().postEvent (new SecurityControllerUpdateEvent (this.getController (), this.getControllerState ()));
	}

	/**
	 * {@inheritDoc}
	 */
	public ISecurityNetworkController getController () {
		if (this.controllerState != SecurityControllerState.ONLINE) return null;
		return Iterables.getFirst (this.getControllerSet (), null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onUpdateTick () {
		if (this.calculationPending) this.calculate ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeNode (IGridNode iGridNode, IGridHost iGridHost) {
		if (!(iGridHost instanceof ISecurityNetworkController)) return;

		this.getControllerSet ().remove (iGridHost);
		this.calculationPending = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addNode (IGridNode iGridNode, IGridHost iGridHost) {
		if (!(iGridHost instanceof ISecurityNetworkController)) return;

		this.getControllerSet ().add (((ISecurityNetworkController) iGridHost));
		this.calculationPending = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSplit (IGridStorage iGridStorage) { }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onJoin (IGridStorage iGridStorage) { }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void populateGridStorage (IGridStorage iGridStorage) { }
}
