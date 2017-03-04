package main.test;

import junit.framework.TestCase;
import main.BigBitSet;

import java.math.BigInteger;

public class BigBitSetTest extends TestCase {
    public void testPower() {
        BigBitSet bitSet;
        BigInteger base = new BigInteger("2");
        String expected, actual;
        for (int i = 0; i < 100; i++) {
            try {
                bitSet = new BigBitSet(new BigInteger("99999999999999999999999999999999"), false);
                bitSet.setBit(i);
                expected = base.pow(i).toString();
                actual = bitSet.toBigInteger().toString();
                assertTrue(expected.equals(actual));
            } catch (Exception e) {
                assertTrue(false);
            }
        }
    }

    public void testSetAndGet() {
        for (int i = 0; i <= 200; i += 50) {
            BigBitSet bitSet = new BigBitSet(new BigInteger("200"), true);
            bitSet.setBit(i);
            assertTrue(bitSet.getBit(i));
        }
    }

    public void testBinary() {
        BigBitSet bitSet;
        int expected, actual, oneCounter;
        String bitString;
        for (int i = 0; i < 100; i++) {
            oneCounter = 0;
            try {
                bitSet = new BigBitSet(new BigInteger("100"), true);
                bitSet.setBit(i);
                expected = i;
                bitString = bitSet.toString();
                actual = bitString.substring(bitString.lastIndexOf("1") + 1).length();
                for (int c = 0; c < bitString.length(); c++) {
                    if (bitString.charAt(c) == '1') {
                        oneCounter++;
                    }
                }
                assertTrue(expected == actual);
                assertTrue(oneCounter == 1);
            } catch (Exception e) {
                assertTrue(false);
            }
        }
    }

    public void testInitLength() {
        BigBitSet bitSet;
        for (int i = 200; i < 1000; i += 200) {
            bitSet = new BigBitSet(i);
            assertTrue(bitSet.getLongCount() == i);
        }
    }

    public void testInitMaxValue() {
        BigBitSet bitSet = new BigBitSet(new BigInteger("200"), false);
        assertTrue(bitSet.getLongCount() == 1);
        bitSet = new BigBitSet(new BigInteger("18446744073709551615"), false);
        assertTrue(bitSet.getLongCount() == 1);
        bitSet = new BigBitSet(new BigInteger("18446744073709551616"), false);
        assertTrue(bitSet.getLongCount() == 2);
        bitSet = new BigBitSet(new BigInteger("4562440617622195218641171605700291324893228507248559930579192517899275167208677386505912811317371399778642309573594407310688704721375437998252661319722214188251994674360264950082874192246603775"), false);
        assertTrue(bitSet.getLongCount() == 10);
        bitSet = new BigBitSet(new BigInteger("4562440617622195218641171605700291324893228507248559930579192517899275167208677386505912811317371399778642309573594407310688704721375437998252661319722214188251994674360264950082874192246603776"), false);
        assertTrue(bitSet.getLongCount() == 11);
    }

    public void testIncrement() {
        BigBitSet bitSet = new BigBitSet(new BigInteger("200"), true);
        BigInteger bigInteger = BigInteger.valueOf(Integer.MAX_VALUE - 5);
        bitSet.setDecimalValue(bigInteger);
        BigInteger one = new BigInteger("1");
        for (int i = 0; i < 1000; i++) {
            assertTrue(bitSet.toBigInteger().compareTo(bigInteger) == 0);
            bitSet.increment();
            bigInteger = bigInteger.add(one);
        }
    }

    public void testIncrementOverflow() {
        BigBitSet bitSet = new BigBitSet(2);
        BigInteger max = bitSet.getMaxValueDecimal();
        BigInteger zero = new BigInteger("0");
        bitSet.setDecimalValue(max);
        assertTrue(bitSet.toBigInteger().compareTo(max) == 0);
        bitSet.increment();
        assertTrue(bitSet.toBigInteger().compareTo(zero) == 0);
    }

    public void testDecrementUnderflow() {
        BigBitSet bitSet = new BigBitSet(2);
        BigInteger zero = new BigInteger("0");
        BigInteger max = bitSet.getMaxValueDecimal();
        assertTrue(bitSet.toBigInteger().compareTo(zero) == 0);
        bitSet.decrement();
        assertTrue(bitSet.toBigInteger().compareTo(max) == 0);
    }

    public void testCompareTo() {
        BigBitSet bitSet1 = new BigBitSet(4);
        BigBitSet bitSet2 = new BigBitSet(4);
        assertTrue(bitSet1.compareTo(bitSet2) == 0);
        assertTrue(bitSet2.compareTo(bitSet1) == 0);
        bitSet1.setBit(63);
        assertTrue(bitSet1.compareTo(bitSet2) == 1);
        assertTrue(bitSet2.compareTo(bitSet1) == -1);
        bitSet2.setBit(64);
        assertTrue(bitSet1.compareTo(bitSet2) == -1);
        assertTrue(bitSet2.compareTo(bitSet1) == 1);
    }

    public void testAdd() {
        BigInteger four = new BigInteger("4");
        BigBitSet bitSet1 = new BigBitSet(4);
        BigBitSet bitSet2 = new BigBitSet(4);
        bitSet1.setBit(1);
        bitSet2.setBit(1);
        bitSet1.add(bitSet2);
        assertTrue(bitSet1.toBigInteger().compareTo(four) == 0);
        //Test first long overflow
        BigInteger bigInteger = new BigInteger("18446744073709551614");
        bitSet1 = new BigBitSet(4);
        bitSet2 = new BigBitSet(4);
        bitSet2.setDecimalValue(new BigInteger("1024"));
        bitSet1.setDecimalValue(bigInteger);
        bitSet1.add(bitSet2);
        bigInteger = bigInteger.add(new BigInteger("1024"));
        assertTrue(bitSet1.toBigInteger().compareTo(bigInteger) == 0);
    }

    public void testAddLongOverflow() {
        BigBitSet bitSet1 = new BigBitSet(4);
        BigBitSet bitSet2 = new BigBitSet(4);
        BigInteger calc = new BigInteger("0");
        BigInteger great = BigInteger.valueOf(Long.MAX_VALUE - 10);
        bitSet2.setDecimalValue(great);
        for (long i = 0; i < 1000; i++){
            bitSet1.add(bitSet2);
            calc = calc.add(great);
            assertTrue(bitSet1.toBigInteger().compareTo(calc) == 0);
        }

    }

    public void testMultiply(){
//        BigInteger four = new BigInteger("4");
//        BigBitSet bitSet1 = new BigBitSet(4);
//        BigBitSet bitSet2 = new BigBitSet(4);
//        bitSet1.setBit(1);
//        bitSet2.setBit(1);
//        bitSet1.multiply(bitSet2);
//        assertTrue(bitSet1.toBigInteger().compareTo(four) == 0);
//        //Test first long overflow
//        BigInteger bigInteger = BigInteger.valueOf(Long.MAX_VALUE/2);
//        bitSet1 = new BigBitSet(4);
//        bitSet2 = new BigBitSet(4);
//        bitSet1.setDecimalValue(bigInteger);
//        bitSet2.setDecimalValue(new BigInteger("4"));
//        bitSet1.multiply(bitSet2);
//        bigInteger = bigInteger.multiply(new BigInteger("4"));
//        assertTrue(bitSet1.toBigInteger().compareTo(bigInteger) == 0);
    }

    public void testConsole() {
        for (int i = 0; i < 128; i++) {
            BigBitSet bitSet = new BigBitSet(new BigInteger("128"), true);
            bitSet.setBit(i);
            System.out.println(bitSet);
        }
    }
}