package com.ancestor.utils;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.ancestor.utils.StringUtils.*;
import static org.junit.Assert.*;

public class StringUtilsTest {

    @Test
    public void testToCamelCase() {
        assertNull(StringUtils.toCamelCase(null));
    }

    @Test
    public void testToCapitalizeCamelCase() {
        assertNull(StringUtils.toCapitalizeCamelCase(null));
        assertEquals("HelloWorld", StringUtils.toCapitalizeCamelCase("hello_world"));
    }

    @Test
    public void testToUnderScoreCase() {
        assertNull(StringUtils.toUnderScoreCase(null));
        assertEquals("hello_world", StringUtils.toUnderScoreCase("helloWorld"));
        assertEquals("\u0000\u0000", StringUtils.toUnderScoreCase("\u0000\u0000"));
        assertEquals("\u0000_a", StringUtils.toUnderScoreCase("\u0000A"));
    }

    @Test
    public void testGetWeekDay() {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E");
        assertEquals(simpleDateformat.format(new Date()), StringUtils.getWeekDay());
    }

    @Test
    public void testGetIP() {
        assertEquals("127.0.0.1", StringUtils.getIp(new MockHttpServletRequest()));
    }
}
