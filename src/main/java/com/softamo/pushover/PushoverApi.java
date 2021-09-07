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
import io.micronaut.core.annotation.Nullable;
import org.reactivestreams.Publisher;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

public interface PushoverApi {
    String MESSAGES = "/1/messages.json";
    @NonNull
    default Publisher<PushoverResponse> sendMessage(@NonNull @NotBlank String token,
                                                    @NonNull @NotBlank String user,
                                                    @NonNull @NotBlank @Valid Message message) {
        return sendMessage(token,
                user,
                message.getMessage(),
                message.getUrl(),
                message.getUrlTitle(),
                message.getTitle(),
                message.getPriority() != null ? message.getPriority().getValue() : null,
                message.getSound() != null ? message.getSound().toString() : null);
    }

    @NonNull
    Publisher<PushoverResponse> sendMessage(@NonNull @NotBlank String token,
                                            @NonNull @NotBlank String user,
                                            @NonNull @NotBlank String message,
                                            @Nullable String url,
                                            @SuppressWarnings("checkstyle:ParameterName") @Nullable String url_title,
                                            @Nullable String title,
                                            @Nullable Integer priority,
                                            @Nullable String sound);
}
