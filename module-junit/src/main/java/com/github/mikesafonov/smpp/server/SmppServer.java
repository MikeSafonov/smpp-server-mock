package com.github.mikesafonov.smpp.server;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface SmppServer {
    String name() default "";

    String systemId() default MockSmppServer.DEFAULT_SYSTEM_ID;

    String password() default MockSmppServer.DEFAULT_PASSWORD;

    int port() default MockSmppServer.RANDOM_PORT;
}
