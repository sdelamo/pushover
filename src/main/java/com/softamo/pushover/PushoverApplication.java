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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.naming.Named;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class PushoverApplication implements Named {
    private static final Logger LOG = LoggerFactory.getLogger(PushoverApplication.class);
    private final PushoverApplicationConfiguration applicationConfiguration;
    private final PushoverApi api;

    public PushoverApplication(PushoverApplicationConfiguration applicationConfiguration,
                               PushoverApi api) {
        this.applicationConfiguration = applicationConfiguration;
        this.api = api;
    }

    @Override
    @NonNull
    public String getName() {
        return applicationConfiguration.getName();
    }

    @NonNull
    Publisher<Response> send(@NonNull @NotNull User user,
                             @NonNull @NotNull @Valid Message message) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Sending notification to user {} from App {} with contents {}", user.getName(), applicationConfiguration.getName(), message.toString());
        }
        return api.sendMessage(applicationConfiguration.getToken(), user.getKey(), message);
    }
}
