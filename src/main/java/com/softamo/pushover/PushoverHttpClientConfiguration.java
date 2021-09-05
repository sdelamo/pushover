/*
 * Copyright 2017-2021 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.softamo.pushover;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.client.HttpClientConfiguration;
import io.micronaut.runtime.ApplicationConfiguration;

import javax.validation.constraints.NotBlank;
import java.time.Duration;

@ConfigurationProperties(PushoverHttpClientConfiguration.PREFIX)
public class PushoverHttpClientConfiguration extends HttpClientConfiguration {
    public static final String PREFIX = "pushover.http-client";
    public static final String HOST_LIVE = "https://api.pushover.net";

    private final PushoverHttpClientConfiguration.PushoverConnectionPoolConfiguration connectionPoolConfiguration;

    @NonNull
    @NotBlank
    private String url = HOST_LIVE;

    public PushoverHttpClientConfiguration(final ApplicationConfiguration applicationConfiguration,
                                  final PushoverHttpClientConfiguration.PushoverConnectionPoolConfiguration connectionPoolConfiguration) {
        super(applicationConfiguration);
        this.connectionPoolConfiguration = connectionPoolConfiguration;
    }

    @NotBlank
    @NonNull
    public String getUrl() {
        return this.url;
    }

    public void setUrl(@NonNull @NotBlank String url) {
        this.url = url;
    }

    @Override
    @NonNull
    public ConnectionPoolConfiguration getConnectionPoolConfiguration() {
        return this.connectionPoolConfiguration;
    }

    /**
     * {@link ConnectionPoolConfiguration} for {@link PushoverHttpClient}.
     */
    @ConfigurationProperties(ConnectionPoolConfiguration.PREFIX)
    public static class PushoverConnectionPoolConfiguration extends ConnectionPoolConfiguration {
    }

    /**
     * Extra {@link ConfigurationProperties} to set the values for the {@link io.micronaut.retry.annotation.Retryable} annotation on {@link PushoverHttpClient}.
     */
    public static class PushoverConnectionPoolRetryConfiguration {

        public static final String PREFIX = "retry";

        private static final Duration DEFAULT_DELAY = Duration.ofSeconds(5);
        private static final int DEFAULT_ATTEMPTS = 0;

        /**
         * The delay between retry attempts.
         */
        private Duration delay = DEFAULT_DELAY;

        /**
         * The maximum number of retry attempts.
         */
        private int attempts = DEFAULT_ATTEMPTS;

        public Duration getDelay() {
            return delay;
        }

        public void setDelay(Duration delay) {
            this.delay = delay;
        }

        public int getAttempts() {
            return attempts;
        }

        public void setAttempts(int attempts) {
            this.attempts = attempts;
        }
    }
}
