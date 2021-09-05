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

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;

import javax.validation.constraints.NotBlank;

@Introspected
public class Message {
    @NonNull
    @NotBlank
    private final String message;

    /**
     * a title for your supplementary URL, otherwise just the URL is shown.
     */
    @Nullable
    private String urlTitle;

    /**
     * a supplementary URL to show with your message.
     */
    @Nullable
    private String url;

    /**
     * your message's title, otherwise your app's name is used.
     */
    @Nullable
    private String title;

    /**
     *  your user's device name to send the message directly to that device, rather than all of the user's devices (multiple devices may be separated by a comma).
     */
    @Nullable
    private String device;

    @Nullable
    private Priority priority;

    /**
     * the name of one of the sounds supported by device clients to override the user's default sound choice.
     */
    @Nullable
    private Sound sound;

    /**
     *  A Unix timestamp of your message's date and time to display to the user, rather than the time your message is received by our API.
     */
    @Nullable
    private Integer timestamp;

    public Message(@NonNull String message) {
        this.message = message;
    }

    @Nullable
    public Sound getSound() {
        return sound;
    }

    public void setSound(@Nullable Sound sound) {
        this.sound = sound;
    }

    @Nullable
    public Priority getPriority() {
        return priority;
    }

    public void setPriority(@Nullable Priority priority) {
        this.priority = priority;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    @Nullable
    public String getUrlTitle() {
        return urlTitle;
    }

    public void setUrlTitle(@Nullable String urlTitle) {
        this.urlTitle = urlTitle;
    }

    @Nullable
    public String getUrl() {
        return url;
    }

    public void setUrl(@Nullable String url) {
        this.url = url;
    }

    @Nullable
    public String getDevice() {
        return device;
    }

    public void setDevice(@Nullable String device) {
        this.device = device;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }
}

