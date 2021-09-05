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

import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import org.reactivestreams.Publisher;
import javax.validation.constraints.NotBlank;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ManualPushoverHttpClient implements PushoverApi {

    public static final String KEY_TOKEN = "token";
    public static final String KEY_USER = "user";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_URL = "url";
    public static final String KEY_URL_TITLE = "url_title";
    public static final String KEY_TITLE = "title";
    public static final String KEY_PRIORITY = "priority";
    public static final String KEY_SOUND = "sound";
    private final HttpClient httpClient;

    public ManualPushoverHttpClient(String url) {
        try {
            this.httpClient = HttpClient.create(new URL(url));
        } catch (MalformedURLException e) {
            throw new ConfigurationException("malformed url :" + url);
        }
    }

    public ManualPushoverHttpClient() {
        this(PushoverHttpClientConfiguration.HOST_LIVE);
    }

    @NonNull
    private Publisher<Response> sendMessage(Map<String, Object> body) {
        return httpClient.retrieve(HttpRequest.POST(MESSAGES, body)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON), Response.class);
    }

    @Override
    @NonNull
    public Publisher<Response> sendMessage(@NonNull @NotBlank String token,
                                           @NonNull @NotBlank String user,
                                           @NonNull @NotBlank String message,
                                           @Nullable String url,
                                           @SuppressWarnings("checkstyle:ParameterName") @Nullable String url_title,
                                           @Nullable String title,
                                           @Nullable Integer priority,
                                           @Nullable String sound) {
        Map<String, Object> body = new HashMap<>();
        body.put(KEY_TOKEN, token);
        body.put(KEY_USER, user);
        body.put(KEY_MESSAGE, message);
        if (url != null) {
            body.put(KEY_URL, url);
        }
        if (url_title != null) {
            body.put(KEY_URL_TITLE, url_title);
        }
        if (title != null) {
            body.put(KEY_TITLE, title);
        }
        if (priority != null) {
            body.put(KEY_PRIORITY, priority);
        }
        if (sound != null) {
            body.put(KEY_SOUND, sound);
        }
        return sendMessage(body);
    }
}
