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

import com.fasterxml.jackson.annotation.JsonValue;

public enum Sound {
    PUSHOVER("pushover"),
    BIKE("bike"),
    BUGLE("bugle"),
    CASH_REGISTER("cashregister"),
    CLASSICAL("classical"),
    COSMIC("cosmic"),
    FALLING("falling"),
    GAMELAN("gamelan"),
    INCOMING("incoming"),
    INTERMISSION("intermission"),
    MAGIC("magic"),
    MECHANICAL("mechanical"),
    PIANOBAR("pianobar"),
    SIREN("siren"),
    SPACEALARM("spacealarm"),
    TUGBOAT("tugboat"),
    ALIEN("alien"),
    CLIMB("climb"),
    PERSISTENT("persistent"),
    echo("ECHO"),
    UPDOWN("updown"),
    VIBRATE("vibrate"),
    NONE("none");

    private final String value;
    Sound(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
