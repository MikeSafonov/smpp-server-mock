package com.github.mikesafonov.junit.smpp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SmppExtension.class)
public class ExtensionTest {
    @SmppServer
    MockSmppServer mockSmppServer;

    @Test
    void run(){
    }
}
