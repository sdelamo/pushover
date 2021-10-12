package com.softamo.pushover

import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Requires
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.async.annotation.SingleResult
import io.micronaut.core.async.publisher.Publishers
import io.micronaut.core.io.socket.SocketUtils
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Produces
import io.micronaut.inject.qualifiers.Qualifiers
import io.micronaut.runtime.server.EmbeddedServer
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import spock.lang.Specification
import javax.validation.constraints.NotBlank

class PushoverHttpClientSpec extends Specification {

    void "it is possible to send a push notification"() {
        given:
        int mockPort = SocketUtils.findAvailableTcpPort()
        EmbeddedServer mockServer = ApplicationContext.run(EmbeddedServer, [
                'spec.name': 'PushoverHttpClientSpec.pushover',
                'micronaut.server.port': mockPort,
        ])
        ApplicationContext applicationContext = ApplicationContext.run([
                'pushover.applications.l3-37.token': 'T6xoNWc7zboEppeeM69tMZsCNkdRqU',
                'pushover.users.sdelamo.key': 's2HkfXVenEeMJ2MBwqDZrhAXpg7uzK',
                'pushover.http-client.url': "http://localhost:$mockPort"
        ])

        PushoverUser user = applicationContext.getBean(PushoverUser, Qualifiers.byName("sdelamo"))
        PushoverApplicationConfiguration applicationConfiguration = applicationContext.getBean(PushoverApplicationConfiguration, Qualifiers.byName("l3-37"))
        PushoverHttpClient httpClient = applicationContext.getBean(PushoverHttpClient)
        when:
        Message message = Message.builder("Hello World").build()
        PushoverResponse result = Mono.from(httpClient.sendMessage(applicationConfiguration.getToken(), user.getKey(), message)).block()

        then:
        noExceptionThrown()
        result.status == 1

        cleanup:
        applicationContext.close()
        mockServer.close()
    }

    @Requires(property = 'spec.name', value = 'PushoverHttpClientSpec.pushover')
    @Controller
    static class MessagesController {

        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        @Post("/1/messages.json")
        @SingleResult
        Publisher<PushoverResponse> sendMessage(@NonNull @NotBlank String token,
                                                @NonNull @NotBlank String user,
                                                @NonNull @NotBlank String message) {
            Publishers.just(new PushoverResponse(1, "aff2ff8c-5d98-4bf3-9424-0af69c9177ad"))
        }
    }
}
