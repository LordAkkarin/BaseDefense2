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
package basedefense.util;

import lombok.NonNull;

/**
 * Provides a utility for retrieving translations.
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
public class TranslationUtility {
        private TranslationUtility () { }

        /**
         * Retrieves a component translation name.
         *
         * @param category The category.
         * @param name     The name.
         * @return The translation name.
         */
        public static String getComponentTranslation (String category, @NonNull String name) {
                if (category == null) return "basedefense." + name;
                return "basedefense." + category + "." + name;
        }

        /**
         * Retrieves a message translation name.
         * @param category The category.
         * @param component The component.
         * @param translationName The translation.
         * @return The translation name.
         */
        public static String getMessageTranslation (String category, @NonNull String component, @NonNull String translationName) {
                if (category == null) return "basedefense." + component + "." + translationName;
                return "basedefense." + category + "." + component + "." + translationName;
        }
}
