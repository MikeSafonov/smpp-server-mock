package com.github.mikesafonov.smpp.boot;

import com.github.mikesafonov.smpp.server.MockSmppServerHolder;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Mike Safonov
 */
public abstract class BaseSmppTest {
    @Autowired
    protected MockSmppServerHolder holder;
}
