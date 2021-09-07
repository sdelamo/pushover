package com.softamo.pushover

import io.micronaut.aop.MethodInvocationContext
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
import spock.lang.Specification
import spock.util.concurrent.PollingConditions
import javax.validation.constraints.NotBlank

class PushoverMessageInterceptorSpec extends Specification {

    void "it is possible to send a push notification"() {
        given:
        int mockPort = SocketUtils.findAvailableTcpPort()
        EmbeddedServer mockServer = ApplicationContext.run(EmbeddedServer, [
                'spec.name': 'PushoverMessageInterceptorSpec.pushover',
                'micronaut.server.port': mockPort,
        ])
        ApplicationContext applicationContext = ApplicationContext.run([
                'pushover.applications.l3-37.token': 'T6xoNWc7zboEppeeM69tMZsCNkdRqU',
                'pushover.users.sdelamo.key': 's2HkfXVenEeMJ2MBwqDZrhAXpg7uzK',
                'pushover.http-client.url': "http://localhost:$mockPort"
        ])
        PushoverMessageInterceptor interceptor = applicationContext.getBean(PushoverMessageInterceptor)

        when:
        def context = Stub(MethodInvocationContext)
        context.stringValue(PushoverMessage) >> Optional.of('Hello World')
        interceptor.intercept(context)

        then:
        noExceptionThrown()
        new PollingConditions().eventually {
            assert 1 == numberOfMessages(mockServer.applicationContext)
        }
        messages(mockServer.applicationContext)[0] == 'Hello World'

        cleanup:
        applicationContext.close()
        mockServer.close()
    }

    private static int numberOfMessages(ApplicationContext applicationContext) {
        messages(applicationContext).size()
    }

    private static List<String> messages(ApplicationContext applicationContext) {
        applicationContext.getBean(MessagesController).messages
    }

    @Requires(property = 'spec.name', value = 'PushoverMessageInterceptorSpec.pushover')
    @Controller
    static class MessagesController {

        List<String> messages = []

        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        @Post("/1/messages.json")
        Publisher<PushoverResponse> sendMessage(@NonNull @NotBlank String token,
                                                @NonNull @NotBlank String user,
                                                @NonNull @NotBlank String message) {
            messages << message
            Publishers.just(new PushoverResponse(1, "aff2ff8c-5d98-4bf3-9424-0af69c9177ad"))
        }
    }
}
