package com.softamo.pushover

import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Requires
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.async.publisher.Publishers
import io.micronaut.core.io.socket.SocketUtils
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Produces
import io.micronaut.runtime.server.EmbeddedServer
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import spock.lang.Specification

import javax.validation.constraints.NotBlank

class ManualPushoverApplicationSpec extends Specification {

    void "it is possible to send a push notification"() {
        given:
        int mockPort = SocketUtils.findAvailableTcpPort()
        EmbeddedServer mockServer = ApplicationContext.run(EmbeddedServer, [
                'spec.name': 'ManualPushoverApplicationSpec.pushover',
                'micronaut.server.port': mockPort,
        ])

        PushoverUser user = new PushoverUser() {
            @Override
            String getKey() {
                's2HkfXVenEeMJ2MBwqDZrhAXpg7uzK'
            }

            @Override
            String getName() {
                'sdelamo'
            }
        }
        PushoverApplication application = new PushoverApplication(new PushoverApplicationConfiguration() {
            @Override
            String getToken() {
                'T6xoNWc7zboEppeeM69tMZsCNkdRqU'
            }

            @Override
            String getName() {
                'l3-37'
            }
        },new ManualPushoverHttpClient("http://localhost:$mockPort"))

        when:
        Message message = Message.builder("Hello World").build()
        PushoverResponse result = Mono.from(application.send(user, message)).block()

        then:
        noExceptionThrown()
        result.status == 1

        cleanup:
        mockServer.close()
    }

    @Requires(property = 'spec.name', value = 'ManualPushoverApplicationSpec.pushover')
    @Controller
    static class MessagesController {

        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        @Post("/1/messages.json")
        PushoverResponse sendMessage(@NonNull @NotBlank String token,
                                        @NonNull @NotBlank String user,
                                        @NonNull @NotBlank String message) {
            new PushoverResponse(1, "aff2ff8c-5d98-4bf3-9424-0af69c9177ad")
        }
    }
}
