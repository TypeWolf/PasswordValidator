package com.zipse;

import com.cthiebaud.passwordvalidator.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;

class DemoValidatorMainTest {

    @Test
    void testValidate_EmptyPassword() {
        DemoValidatorMain validator = new DemoValidatorMain();

        ValidationResult result = validator.validate("");

        assertFalse(result.isValid());
        assertEquals("Password cannot be empty.", result.Message());
    }

    @Test
    void testValidate_ValidPassword() {
        DemoValidatorMain validator = new DemoValidatorMain();

        ValidationResult result = validator.validate("securePassword");

        assertTrue(result.isValid());
        assertEquals("Password is valid.", result.Message());
    }

    @Test
    void testValidate_ShortPassword() {
        DemoValidatorMain validator = new DemoValidatorMain();

        ValidationResult result = validator.validate("123");

        assertFalse(result.isValid());
        assertEquals("Password must be at least 6 characters long.", result.Message());
    }
}
