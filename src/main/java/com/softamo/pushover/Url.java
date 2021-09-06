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
public class Url {
    @NonNull
    @NotBlank
    private final String url;

    @Nullable
    private String title;

    public Url(@NonNull String url) {
        this.url = url;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public static Builder builder(@NonNull String url) {
        return new Builder(url);
    }

    public static class Builder {

        private final Url url;

        Builder(@NonNull String url) {
            this.url = new Url(url);
        }

        @NonNull
        public Builder title(@Nullable String title) {
            url.setTitle(title);
            return this;
        }

        public Url build() {
            return url;
        }
    }
}
