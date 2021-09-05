**[Micronaut](https://micronaut.io) Java library to consume the [Pushover API](https://pushover.net/api)**

## Pushover API Micronaut Java Library

- [Code Repository](https://github.com/sdelamo/pushover)
- [Releases](https://github.com/sdelamo/pushover/releases)

This project is a Java library to consume the [Pushover API](https://pushover.net/api). It is built with the [Micronaut](https://micronaut.io) Framework and you can use it in a Micronaut app or as a standalone library.

## Dependency snippet

To use it with https://gradle.org[Gradle]:

`implementation 'com.softamo:pushover:XXXX'`

To use it with https://maven.apache.org[Maven]:

```xml
<dependency>
    <groupId>com.softamo</groupId>
    <artifactId>pushover</artifactId>
    <version>xxx</version>
    <type>pom</type>
</dependency>
```

## Usage

If you want to use the library in Micronaut application, register applications and users via configuration: 

```yaml
pushover:
    applications:
        l3-37:
            token: 'T6xoNWc7zboEppeeM69tMZsCNkdRqU'
    users:
        sdelamo:
            key: 's2HkfXVenEeMJ2MBwqDZrhAXpg7uzK',
```

Then you can send messages from an application to a user:

```java
User user = applicationContext.getBean(User, Qualifiers.byName("sdelamo"))
PushoverApplication application = applicationContext.getBean(PushoverApplication, Qualifiers.byName("l3-37"))
Message message = new Message("Hello World");
application.send(user, message);
```

You can use the library without a Micronaut Application Context. In that case, you can do:

```java
String token = 'T6xoNWc7zboEppeeM69tMZsCNkdRqU'
String user = 's2HkfXVenEeMJ2MBwqDZrhAXpg7uzK'
PushoverApplication application = new PushoverApplication(() -> token, new ManualPushoverHttpClient())
Message message = new Message("Hello World")
Response result = Mono.from(application.send(() -> user, message)).block()
```

## Build

This library uses https://gradle.org[Gradle].

It uses the plugins:

- [Gradle Build Scan Plugins](https://plugins.gradle.org/plugin/com.gradle.build-scan)
  
## Release instructions

### snapshot:

- Make sure version ends with `-SNAPSHOT`
- `./gradlew publish`

### release:

- bump up version
- Tag it. E.g. v1.0.0
- `./gradlew publishToSonatype closeSonatypeStagingRepository`

Go to `https://s01.oss.sonatype.org/#stagingRepositories` and release repository.
