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

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Introspected
public class Response {

    @NonNull
    @NotNull
    private Integer status;

    @NonNull
    @NotBlank
    private String request;

    public Response() {

    }

    @Creator
    public Response(@NonNull Integer status, @NonNull String request) {
        this.status = status;
        this.request = request;
    }

    @NonNull
    public Integer getStatus() {
        return status;
    }

    @NonNull
    public String getRequest() {
        return request;
    }

    public void setStatus(@NonNull Integer status) {
        this.status = status;
    }

    public void setRequest(@NonNull String request) {
        this.request = request;
    }

    @Override
    public String toString() {
        return "PushoverResponse{" +
                "status=" + status +
                ", request='" + request + '\'' +
                '}';
    }
}
