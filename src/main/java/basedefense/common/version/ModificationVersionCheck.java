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
package basedefense.common.version;

import basedefense.BaseDefenseModification;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import java.io.InputStreamReader;
import java.util.Optional;

/**
 * Provides a simple utility class for retrieving version information.
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
public class ModificationVersionCheck {
        public static final String API_URL = "https://api.github.com";
        public static final String API_PATTERN = "/repos/%s/%s/releases/latest";
        public static final String USER_AGENT_PATTERN = "BaseDefense/%s (+https://github.com/LordAkkarin/BaseDefense2)";

        private final HttpClient httpClient;
        private Optional<String> latestVersion = null;

        public ModificationVersionCheck () {
                HttpClientBuilder builder = HttpClientBuilder.create ();
                builder.disableCookieManagement ();
                builder.disableRedirectHandling ();

                builder.setUserAgent (String.format (USER_AGENT_PATTERN, BaseDefenseModification.getInstance ().getVersion ()));
                builder.setDefaultHeaders (Lists.newArrayList (new BasicHeader ("Accept", "application/vnd.github.v3+json")));

                this.httpClient = builder.build ();
        }

        /**
         * Checks the modification version.
         * @return The version.
         */
        public Optional<String> check () {
                // We will only run this check once as this check may take a lot of time to process
                if (this.latestVersion != null) return this.latestVersion;

                InputStreamReader inputStreamReader = null;

                try {
                        HttpGet getRequest = new HttpGet (API_URL + String.format (API_PATTERN, "LordAkkarin", "BaseDefense2"));
                        HttpResponse response = this.httpClient.execute (getRequest);
                        Preconditions.checkState (response.getStatusLine ().getStatusCode () == 200, "Expected status code 200 but received %s", response.getStatusLine ());

                        HttpEntity entity = response.getEntity ();
                        inputStreamReader = new InputStreamReader (entity.getContent ());

                        Gson gson = new Gson ();
                        JsonObject object = gson.fromJson (inputStreamReader, JsonObject.class);
                        Preconditions.checkState (object.has ("tag_name"), "No valid version found.");

                        this.latestVersion = Optional.of (object.get ("tag_name").getAsString ());
                } catch (Exception ex) {
                        BaseDefenseModification.getInstance ().getLogger ().warn ("Unable to retrieve version information: " + ex.getMessage (), ex);
                        this.latestVersion = Optional.empty ();
                } finally {
                        IOUtils.closeQuietly (inputStreamReader);
                }

                return this.latestVersion;
        }
}
