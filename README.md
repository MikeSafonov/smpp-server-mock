# smpp-server-mock

This project helps to write integration test for applications uses SMPP connection. 

`smpp-server-mock` comes with:

- Mock SMPP server - [`MockSmppServer`](module-core/src/main/java/com/github/mikesafonov/smpp/server/MockSmppServer.java)
- Junit 5 extension - [`MockSmppExtension`](module-junit/src/main/java/com/github/mikesafonov/smpp/server/MockSmppExtension.java)
- Spring Boot Starter to start mock servers before application startup - 
[`MockSmppBootstrapConfiguration`](module-spring-boot/src/main/java/com/github/mikesafonov/smpp/server/MockSmppBootstrapConfiguration.java)

## Using JUnit 5 extension

TODO

## Using Spring Boot Starter

TODO


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
