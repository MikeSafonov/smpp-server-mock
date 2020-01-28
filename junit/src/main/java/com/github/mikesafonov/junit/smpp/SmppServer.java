package com.github.mikesafonov.junit.smpp;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface SmppServer {
    String systemId() default MockSmppServer.DEFAULT_SYSTEM_ID;

    String password() default MockSmppServer.DEFAULT_PASSWORD;

    int port() default MockSmppServer.DEFAULT_PORT;
}
