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
package basedefense.api.surveillance;

/**
 * Represents a surveillance network controller.
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
public interface ISurveillanceNetworkController {

        /**
         * Retrieves the controller type.
         * @return The type.
         */
        ControllerType getControllerType ();

        /**
         * Retrieves the system state.
         * @return The state.
         */
        SystemState getSystemState ();

        /**
         * Updates the system state.
         * @param state The state.
         */
        void setSystemState (SystemState state);

        /**
         * Provides a list of valid controller types.
         */
        enum ControllerType {
                PRIMITIVE,
                FACE_RECOGNITION
        }

        /**
         * Provides a list of valid system states.
         */
        enum SystemState {
                ALERT,
                ARMED,
                DISABLED
        }
}
