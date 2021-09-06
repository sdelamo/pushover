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

import io.micronaut.aop.InterceptorBean;
import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.runtime.context.scope.Refreshable;
import io.micronaut.scheduling.TaskExecutors;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@Refreshable
@InterceptorBean(PushoverMessage.class)
class PushoverMessageInterceptor implements MethodInterceptor<Object, Object> {
    private static final Logger LOG = LoggerFactory.getLogger(PushoverMessageInterceptor.class);
    private static final Class<PushoverMessage> ANNOTATION = PushoverMessage.class;
    private static final String MEMBER_APP_NAME = "appName";
    private static final String MEMBER_USER_NAME = "userName";
    private static final String MEMBER_TITLE = "title";
    private static final String MEMBER_URL = "url";
    private static final String MEMBER_URL_TITLE = "urlTitle";
    private static final String MEMBER_PRIORITY = "priority";
    private static final String MEMBER_SOUND = "sound";
    private static final String MEMBER_DEVICE = "device";
    private final Map<String, User> usersByName = new ConcurrentHashMap<>();
    private final Map<String, PushoverApplication> applicationsByName = new ConcurrentHashMap<>();
    private final Scheduler scheduler;

    PushoverMessageInterceptor(@Named(TaskExecutors.IO) ExecutorService executorService,
                               Collection<User> users,
                               Collection<PushoverApplication> applications) {
        this.scheduler = Schedulers.fromExecutorService(executorService);
        for (User user : users) {
            this.usersByName.put(user.getName(), user);
        }
        for (PushoverApplication application : applications) {
            this.applicationsByName.put(application.getName(), application);
        }
    }

    @Override
    public Object intercept(MethodInvocationContext<Object, Object> context) {
        context.stringValue(ANNOTATION).ifPresent(message -> sendNotification(context, message));
        return context.proceed();
    }

    void sendNotification(@NonNull MethodInvocationContext<Object, Object> context,
                          @NonNull String message) {
        Optional<String> appNameOptional = resolveAppName(context);
        Optional<String> userNameOptional = resolveUserName(context);
        if (appNameOptional.isPresent()) {
            String appName = appNameOptional.get();
            PushoverApplication application = applicationsByName.get(appName);
            if (application != null) {
                if (userNameOptional.isPresent()) {
                    String userName = userNameOptional.get();
                    User user = usersByName.get(userName);
                    if (user != null) {
                        Mono.from(application.send(user, createMessage(context, message)))
                                .subscribeOn(scheduler)
                                .subscribe(response -> {
                                    if (response.getStatus().equals(1)) {
                                        LOG.trace("your notification has been received and queued. Request {}", response.getRequest());
                                    }
                                });
                    } else {
                        if (LOG.isWarnEnabled()) {
                            LOG.warn("Pushover user not found by name qualifier: {}", userName);
                        }
                    }
                } else {
                    if (LOG.isWarnEnabled()) {
                        LOG.warn("Pushover user name not resolved");
                    }
                }
            } else {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("Pushover application not found by name qualifier: {}", appName);
                }
            }
        } else {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Pushover appName not resolved");
            }
        }
    }

    @NonNull
    private Message createMessage(@NonNull MethodInvocationContext<Object, Object> context,
                                  @NonNull String message) {
        Message.Builder builder = Message.builder(message);
        Optional<String> titleOptional = context.stringValue(ANNOTATION, MEMBER_TITLE);
        if (titleOptional.isPresent()) {
            builder = builder.title(titleOptional.get());
        }
        Optional<String> urlOptional = context.stringValue(ANNOTATION, MEMBER_URL);
        if (urlOptional.isPresent()) {
            Url.Builder urlBuilder = Url.builder(urlOptional.get());
            Optional<String> urlTitleOptional = context.stringValue(ANNOTATION, MEMBER_URL_TITLE);
            if (urlTitleOptional.isPresent()) {
                urlBuilder = urlBuilder.title(urlTitleOptional.get());
            }
            builder = builder.url(urlBuilder.build());
        }
        Optional<Priority> priorityOptional = context.getValue(ANNOTATION, MEMBER_PRIORITY, Priority.class);
        if (priorityOptional.isPresent()) {
            builder = builder.priority(priorityOptional.get());
        }
        Optional<Sound> soundOptional = context.getValue(ANNOTATION, MEMBER_SOUND, Sound.class);
        if (soundOptional.isPresent()) {
            builder = builder.sound(soundOptional.get());
        }
        Optional<String> deviceOptional = context.stringValue(ANNOTATION, MEMBER_DEVICE);
        if (deviceOptional.isPresent()) {
            builder = builder.device(deviceOptional.get());
        }
        return builder.build();
    }

    @NonNull
    private Optional<String> resolveAppName(@NonNull MethodInvocationContext<Object, Object> context) {
        Optional<String> appNameOptional = context.stringValue(ANNOTATION, MEMBER_APP_NAME);
        if (appNameOptional.isPresent()) {
            return appNameOptional;
        }
        LOG.trace("@PushoverMessage#appName not present");
        if (applicationsByName.size() == 1) {
            String result = applicationsByName.keySet().iterator().next();
            LOG.trace("There is only one PushoverApplication. Using PushoverApplication named {}", result);
            return Optional.of(result);
        }
        return Optional.empty();
    }

    @NonNull
    private Optional<String> resolveUserName(@NonNull MethodInvocationContext<Object, Object> context) {
        Optional<String> userNameOptional = context.stringValue(ANNOTATION, MEMBER_USER_NAME);
        if (userNameOptional.isPresent()) {
            return userNameOptional;
        }
        LOG.trace("@PushoverMessage#userName not present");
        if (usersByName.size() == 1) {
            String result = usersByName.keySet().iterator().next();
            LOG.trace("There is only one Pushover User. Using user named {}", result);
            return Optional.of(result);
        }
        return Optional.empty();
    }
}
