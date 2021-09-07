package com.softamo.pushover

import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Requires
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.io.socket.SocketUtils
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Produces
import io.micronaut.inject.qualifiers.Qualifiers
import io.micronaut.runtime.server.EmbeddedServer
import reactor.core.publisher.Mono
import spock.lang.Specification
import javax.validation.constraints.NotBlank

class InvalidApplicationTokenSpec  extends Specification {

    void "it is possible to send a push notification"() {
        given:
        int mockPort = SocketUtils.findAvailableTcpPort()
        EmbeddedServer mockServer = ApplicationContext.run(EmbeddedServer, [
                'spec.name': 'InvalidApplicationTokenSpec.pushover',
                'micronaut.server.port': mockPort,
        ])
        ApplicationContext applicationContext = ApplicationContext.run([
                'pushover.applications.l3-37.token': 'T6xoNWc7zboEppeeM69tMZsCNkdRqU',
                'pushover.users.sdelamo.key': 's2HkfXVenEeMJ2MBwqDZrhAXpg7uzK',
                'pushover.http-client.url': "http://localhost:$mockPort"
        ])

        PushoverUser user = applicationContext.getBean(PushoverUser, Qualifiers.byName("sdelamo"))
        PushoverApplication application = applicationContext.getBean(PushoverApplication, Qualifiers.byName("l3-37"))

        when:
        Message message = Message.builder("Hello World").build();
        Optional<PushoverResponse> resultOptional = Mono.from(application.send(user, message)).blockOptional()

        then:
        noExceptionThrown()
        resultOptional
        resultOptional.isPresent()

        when:
        PushoverResponse result = resultOptional.get()

        then:
        result
        result.status == 0
        result.errors == ['application token is invalid']

        cleanup:
        applicationContext.close()
        mockServer.close()
    }

    @Requires(property = 'spec.name', value = 'InvalidApplicationTokenSpec.pushover')
    @Controller
    static class MessagesController {

        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        @Post("/1/messages.json")
        HttpResponse<Map> sendMessage(@NonNull @NotBlank String token,
                                      @NonNull @NotBlank String user,
                                      @NonNull @NotBlank String message) {
            HttpResponse.badRequest([
                    "token":"invalid",
                    "errors":["application token is invalid"],
                    "status":0,
                    "request":"9e9673cd-5bdd-43df-9df5-0293bba04db0"
            ])
        }
    }
}
