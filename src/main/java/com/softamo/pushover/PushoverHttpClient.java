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
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Retryable;
import org.reactivestreams.Publisher;

import javax.validation.constraints.NotBlank;

@Client(
        value = "${" + PushoverHttpClientConfiguration.PREFIX + ".url:`" + PushoverHttpClientConfiguration.HOST_LIVE + "`}",
        configuration = PushoverHttpClientConfiguration.class,
        errorType = PushoverResponse.class
)
@Retryable(
        attempts = "${" + PushoverHttpClientConfiguration.PREFIX + ".retry.attempts:0}",
        delay = "${" + PushoverHttpClientConfiguration.PREFIX + ".retry.delay:5s}")
public interface PushoverHttpClient extends PushoverApi {

    @Override
    @Produces(MediaType.APPLICATION_FORM_URLENCODED)
    @Consumes(MediaType.APPLICATION_JSON)
    @Post(MESSAGES)
    @NonNull
    @SingleResult
    Publisher<PushoverResponse> sendMessage(@NonNull @NotBlank String token,
                                            @NonNull @NotBlank String user,
                                            @NonNull @NotBlank String message,
                                            @Nullable String url,
                                            @SuppressWarnings("checkstyle:ParameterName") @Nullable String url_title,
                                            @Nullable String title,
                                            @Nullable Integer priority,
                                            @Nullable String sound);
}
