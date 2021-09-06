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

import io.micronaut.aop.Around;
import io.micronaut.context.annotation.AliasFor;
import io.micronaut.core.bind.annotation.Bindable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Around
@Documented
public @interface PushoverMessage {
    /**
     * @return The message to send.
     */
    @AliasFor(annotation = Bindable.class, member = "value")
    @AliasFor(member = "message")
    String value() default "";

    /**
     * @return The application's name.
     */
    @AliasFor(annotation = Bindable.class, member = "appName")
    String appName() default "";

    /**
     * @return The user's name.
     */
    @AliasFor(annotation = Bindable.class, member = "userName")
    String userName() default "";

    /**
     * @return A supplementary URL to show with your message.
     */
    @AliasFor(annotation = Bindable.class, member = "url")
    String url() default "";

    /**
     * @return A title for your supplementary URL, otherwise just the URL is shown.
     */
    @AliasFor(annotation = Bindable.class, member = "urlTitle")
    String urlTitle() default "";

    /**
     * @return Your message's title, otherwise your app's name is used.
     */
    @AliasFor(annotation = Bindable.class, member = "title")
    String title() default "";

    /**
     * @return Your message's priority.
     */
    @AliasFor(annotation = Bindable.class, member = "priority")
    Priority priority() default Priority.NORMAL;

    /**
     * @return Your message's sound.
     */
    @AliasFor(annotation = Bindable.class, member = "sound")
    Sound sound() default Sound.PUSHOVER;

    /**
     * @return User's device name to send the message directly to that device, rather than all of the user's devices (multiple devices may be separated by a comma).
     */
    @AliasFor(annotation = Bindable.class, member = "device")
    String device() default "";
}
