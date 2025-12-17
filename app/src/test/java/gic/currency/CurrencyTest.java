package gic.currency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import gic.currrency.Currency;

public class CurrencyTest {
    @Test
    public void currencyToString() {
        var test = new Currency(10000);
        assertEquals("$100.00", test.toString());

        var test1 = new Currency(100000);
        assertEquals("$1,000.00", test1.toString());

        var test2 = new Currency(1000000);
        assertEquals("$10,000.00", test2.toString());

        var test3 = new Currency(10000000);
        assertEquals("$100,000.00", test3.toString());

        var test4 = new Currency(100000000);
        assertEquals("$1,000,000.00", test4.toString());

        var test5 = new Currency(256367478);
        assertEquals("$2,563,674.78", test5.toString());

        var test6 = new Currency(34512389);
        assertEquals("$345,123.89", test6.toString());
    }

    @Test
    public void currencyFromString() {
        var test = new Currency("$100.00");
        assertEquals(10000, test.getAmountCents());

        var test1 = new Currency("$1,000.00");
        assertEquals(100000, test1.getAmountCents());

        var test2 = new Currency("$10,000.00");
        assertEquals(1000000, test2.getAmountCents());

        var test3 = new Currency("$100,000.00");
        assertEquals(10000000, test3.getAmountCents());

        var test4 = new Currency("$1,000,000.00");
        assertEquals(100000000, test4.getAmountCents());

        var test5 = new Currency("$2,563,674.78");
        assertEquals(256367478, test5.getAmountCents());

        var test6 = new Currency("$345,123.89");
        assertEquals(34512389, test6.getAmountCents());

        try {
            new Currency("100.00");
            fail("100.00 should have failed");
        } catch (IllegalArgumentException ex) {
            // Passed
        }

        try {
            new Currency("$1,00.00");
            fail("$1,00.00 should have failed");
        } catch (IllegalArgumentException ex) {
            // Passed
        }

        try {
            new Currency("$10,00.00");
            fail("$10,00.00 should have failed");
        } catch (IllegalArgumentException ex) {
            // Passed
        }

        try {
            new Currency("$10,000");
            fail("$10,000 should have failed");
        } catch (IllegalArgumentException ex) {
            // Passed
        }

        try {
            new Currency("$10,000.0");
            fail("$10,000.0 should have failed");
        } catch (IllegalArgumentException ex) {
            // Passed
        }

        try {
            new Currency("$10,000.000");
            fail("$10,000.000 should have failed");
        } catch (IllegalArgumentException ex) {
            // Passed
        }
    }

    @Test
    public void currencyAdd() {
        var test = new Currency(10000);
        test.add(10000);
        assertEquals(20000, test.getAmountCents());

        try {
            var test1 = new Currency(10000);
            test1.add(-20000);
            fail("Negative currencies should not be allowed");
        } catch (ArithmeticException ex) {
            // Passed
        }

        try {
            var test2 = new Currency(Long.MAX_VALUE - 200);
            test2.add(220);
            fail("The add method should not allow the currency to overflow.");
        } catch (ArithmeticException ex) {
            // Passed
        }

        try {
            var test3 = new Currency(Long.MIN_VALUE + 200);
            test3.add(-220);
            fail("The add method should not allow the currency to underflow.");
        } catch (ArithmeticException ex) {
            // Passed
        }
    }
}
