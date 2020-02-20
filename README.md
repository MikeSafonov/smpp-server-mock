# smpp-server-mock
[![codecov](https://codecov.io/gh/MikeSafonov/smpp-server-mock/branch/master/graph/badge.svg)](https://codecov.io/gh/MikeSafonov/smpp-server-mock)
[![Travis-CI](https://travis-ci.com/MikeSafonov/smpp-server-mock.svg?branch=master)](https://travis-ci.com/MikeSafonov/smpp-server-mock)
[![Conventional Commits](https://img.shields.io/badge/Conventional%20Commits-1.0.0-yellow.svg)](https://conventionalcommits.org)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=MikeSafonov_smpp-server-mock&metric=alert_status)](https://sonarcloud.io/dashboard?id=MikeSafonov_smpp-server-mock)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=MikeSafonov_smpp-server-mock&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=MikeSafonov_smpp-server-mock)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=MikeSafonov_smpp-server-mock&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=MikeSafonov_smpp-server-mock)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=MikeSafonov_smpp-server-mock&metric=security_rating)](https://sonarcloud.io/dashboard?id=MikeSafonov_smpp-server-mock)

[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=MikeSafonov_smpp-server-mock&metric=bugs)](https://sonarcloud.io/dashboard?id=MikeSafonov_smpp-server-mock)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=MikeSafonov_smpp-server-mock&metric=code_smells)](https://sonarcloud.io/dashboard?id=MikeSafonov_smpp-server-mock)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=MikeSafonov_smpp-server-mock&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=MikeSafonov_smpp-server-mock)

[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=MikeSafonov_smpp-server-mock&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=MikeSafonov_smpp-server-mock)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=MikeSafonov_smpp-server-mock&metric=ncloc)](https://sonarcloud.io/dashboard?id=MikeSafonov_smpp-server-mock)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=MikeSafonov_smpp-server-mock&metric=sqale_index)](https://sonarcloud.io/dashboard?id=MikeSafonov_smpp-server-mock)

This project helps to write integration test for applications uses SMPP connection. 

`smpp-server-mock` comes with:

- Mock SMPP server - [`MockSmppServer`](module-core/src/main/java/com/github/mikesafonov/smpp/server/MockSmppServer.java)
- Junit 5 extension - [`MockSmppExtension`](module-junit/src/main/java/com/github/mikesafonov/smpp/server/MockSmppExtension.java)
- Spring Boot Starter to start mock servers before application startup - 
[`MockSmppBootstrapConfiguration`](module-spring-boot/src/main/java/com/github/mikesafonov/smpp/server/MockSmppBootstrapConfiguration.java)
- AssertJ assertions for core SMPP server objects

## Core 
`smpp-server-mock-core` consist of two main classes: 
[`MockSmppServer`](module-core/src/main/java/com/github/mikesafonov/smpp/server/MockSmppServer.java) and
[`MockSmppServerHolder`](module-core/src/main/java/com/github/mikesafonov/smpp/server/MockSmppServerHolder.java).

`MockSmppServer` represents smpp server which listening connections, received request, produce response and 
keep requests in in-memory queue `QueueSmppSessionHandler`.

## Using JUnit 5 extension

Add `JUnit 5` test dependencies:

Maven:
```
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.6.0</version>
    <scope>test</scope>
</dependency>
```

Gradle:
```
dependencies{
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
}
```

Add extension dependency `com.github.mikesafonov:smpp-server-mock-junit`:

Maven: 
```
<dependency>
    <groupId>com.github.mikesafonov</groupId>
    <artifactId>smpp-server-mock-junit</artifactId>
    <version>${version}</version>
    <scope>test</scope>
</dependency>
```
Gradle:

```
dependencies{
    testImplementation("com.github.mikesafonov:smpp-server-mock-junit:${version}")
}
```

Test example:

```java
@ExtendWith(MockSmppExtension.class)
public class MyTest {
    @SmppServer(systemId = "customSystemId", password = "password")
    MockSmppServer mockSmppServer;
    
    @Test
    void shouldSendSms(){
        // create some class to send SMS and connect to mock server, example:
        MySmsSender sender = new MySmsSender("localhost", mockSmppServer.getPort(), 
                                "customSystemId", "password");
        sender.sendSms("message", "number");
        
        List<SubmitSm> messages = mockSmppServer.getSubmitSmMessages();
        SubmitSm message = messages.get(0);

        assertEquals("number", message.getDestAddress().getAddress());
    }       
}
```

## Using AssertJ assertions

`com.github.mikesafonov:smpp-server-mock-assertj` helps to write more intuitive assertions for SMPP objects.  

To start using AssertJ assertions you need to:

- add `assertj-core` dependency
- add `com.github.mikesafonov:smpp-server-mock-assertj` dependency

Maven: 
```
<dependency>
    <groupId>com.github.mikesafonov</groupId>
    <artifactId>smpp-server-mock-assertj</artifactId>
    <version>${version}</version>
    <scope>test</scope>
</dependency>
```
Gradle:

```
dependencies{
    testImplementation("com.github.mikesafonov:smpp-server-mock-assertj:${version}")
}
```
Assertions for MockSmppServer:
```
SmppAssertions.assertThat(mockSmppServer)
                .hasPort(2000)
                .hasName("myserver")
                .hasSystemId("systemId")
                .hasSingleMessage() // assert for single SubmitSm
                .hasSingleCancelMessage() // assert for single CancelSm
                .messages()// assert for list of SubmitSm
                .cancelMessages()// assert for list of CancelSm
                .requests();//assert for list of any request
```

Assertions for SubmitSm:
```
SmppAssertions.assertThat(mockSmppServer)
                    .hasSingleMessage()
                    .hasDest("number")
                    .hasText("message")
                    .hasEsmClass(esmClass)
                    .hasDeliveryReport()
                    .doesNotHaveDeliveryReport()
                    .hasSource("someSource");

or

SmppAssertions.assertThat(submitSm)
                    .hasDest("number")
                    .hasText("message")
                    .hasEsmClass(esmClass)
                    .hasDeliveryReport()
                    .doesNotHaveDeliveryReport()
                    .hasSource("someSource");

```


Assertions for CancelSm:
```
SmppAssertions.assertThat(mockSmppServer)
                .hasSingleCancelMessage()
                .hasDest("number")
                .hasId("messageId")
                .hasSource("someSource");

or

SmppAssertions.assertThat(cancelSm)
                .hasDest("number")
                .hasId("messageId")
                .hasSource("someSource");
```

Assertions for list of SubmitSm:
```
SmppAssertions.assertThat(mockSmppServer)
                .messages()
                .containsDest(destAddress)
                .containsText(text)
                .containsEsmClass(esmClass)
                .containsSource(sourceAddress);

or

SmppAssertions.assertThatSubmit(listOfSubmitSm)
                .containsDest(destAddress)
                .containsText(text)
                .containsEsmClass(esmClass)
                .containsSource(sourceAddress);

```

Assertions for list of CancelSm:
```
SmppAssertions.assertThat(mockSmppServer)
                .cancelMessages()
                .containsDest(destAddress)
                .containsId(messageId)
                .containsSource(sourceAddress);

or

SmppAssertions.assertThat(listOfCancelSm)
                .containsDest(destAddress)
                .containsId(messageId)
                .containsSource(sourceAddress);
```

Assertions for MockSmppServerHolder:
```
SmppAssertions.assertThat(holder)
                .allStarted() // verify that all MockSmppServer started
                .serverByName("first); // find MockSmppServer by name and return assertions
```

## Using Spring Boot Starter

Spring boot starter are used to bootstrap `MockSmppServer` servers using spring cloud bootstrap phase. 

Add `spring-cloud` and `spring-boot-starter-test` test dependencies:

Maven:
```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter</artifactId>
    <version>cloud-version</version>     
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <version>version</version>     
    <scope>test</scope>
</dependency>
```

Gradle:
```
dependencies{
    testImplementation("org.springframework.cloud:spring-cloud-starter:${cloud-version}")
    testImplementation("org.springframework.boot:spring-boot-starter-test:${version}")
}
```

Add extension dependency `com.github.mikesafonov:smpp-server-mock-boot`:

Maven: 
```
<dependency>
    <groupId>com.github.mikesafonov</groupId>
    <artifactId>smpp-server-mock-boot</artifactId>
    <version>${version}</version>
    <scope>test</scope>
</dependency>
```
Gradle:

```
dependencies{
    testImplementation("com.github.mikesafonov:smpp-server-mock-boot:${version}")
}
```

The starter consumes properties:

`smpp.mocks.<server name>.port` (optional, default random)

`smpp.mocks.<server name>.password`

`smpp.mocks.<server name>.systemId`

The starter produces properties:

`smpp.mocks.<server name>.port` 

`smpp.mocks.<server name>.password`

`smpp.mocks.<server name>.systemId`

#### Example

**application.properties**:
```properties
smpp.mocks.one.password=test
smpp.mocks.one.systemId=user
# some properties to smpp connection in your app
my.smpp.connection.host=localhost
my.smpp.connection.port=${smpp.mocks.one.port}
my.smpp.connection.password=${smpp.mocks.one.password}
my.smpp.connection.systemId=${smpp.mocks.one.systemId}
```

**test**:

```java
    @SpringBootTest
    class SingleServer extends BaseSmppTest {
        @Autowired
        protected MockSmppServerHolder holder;
        
        @Test
        void shouldRunExpectedSmppServer() {
            // test logic
            List<PduRequest> requests = 
                    holder.getByName("one").get().getRequests();
            // verify smpp requests
        }
    }
```

## Build

### Build from source

You can build application using following command:

    ./gradlew clean build -x signArchives
    
#### Requirements:

JDK >= 1.8

### Unit tests

You can run unit tests using following command:

    ./gradlew test
    
### Mutation tests

You can run mutation tests using following command:

    ./grdlew pitest

You will be able to find pitest report in `build/reports/pitest/` folder.

## Contributing

Feel free to contribute. 
New feature proposals and bug fixes should be submitted as GitHub pull requests. 
Fork the repository on GitHub, prepare your change on your forked copy, and submit a pull request.

**IMPORTANT!**
>Before contributing please read about [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0-beta.2/) / [Conventional Commits RU](https://www.conventionalcommits.org/ru/v1.0.0-beta.2/)
