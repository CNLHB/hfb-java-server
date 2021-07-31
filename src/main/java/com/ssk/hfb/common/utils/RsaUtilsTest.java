package com.ssk.hfb.common.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RsaUtilsTest {

    @Test
    void generateKey() throws Exception {
        RsaUtils.generateKey("public.pub","private.pri","aiwa",10);
    }
}