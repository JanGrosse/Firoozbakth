package main;

import java.math.BigInteger;

public class BigBitSet implements Comparable<BigBitSet> {
    //Every long will occupy 64 bits(8 bytes). The maximum array length is Integer.MAX_VALUE - 5 (=2147483642). So theoretically almost 17 gibibytes (=64*2147483642 bits) could be stored
    private long[] bitSetArray;
    //Will store the maxValue; If the decimal value is too big for BigInteger it stays null
    private BigInteger maxValueDecimal;
    //Calculate the maximum unsigned number long can hold: one time positive and plus one extra negative
    private BigInteger longMaxDecimal = BigInteger.valueOf(Long.MAX_VALUE).multiply(new BigInteger("2")).add(new BigInteger("1"));
    //Max Value of long plus one is the value of the new MSB
    private BigInteger MSBValueDecimal = BigInteger.valueOf(Long.MAX_VALUE).add(new BigInteger("1"));
    //Decimal base for every long
    private BigInteger baseDecimal = longMaxDecimal.add(new BigInteger("1"));
    //Get bit count of long. Plus one for the sign bit
    private int bitsPerLong = Long.bitCount(Long.MAX_VALUE) + 1;
    //count of long arrays
    private int longCount = 0;
    //count of bits
    private BigInteger bitCount;
    /*    Performance related    */
    //Save for speed up
    private BigInteger bitsPerLongBigInteger = BigInteger.valueOf(bitsPerLong);
    //Save for speed up
    private int maxBitIndexLong = bitsPerLong - 1;
    //Instantiate calculation variables, which are used for most arithmetic and logic operations
    private int akkuIntA;
    private int akkuIntB;
    private long akkuLongA;
    private long akkuLongB;
    private long[] akkuBitSet;

    public BigBitSet(BigInteger value, boolean bitsNotStorable) {
        if (bitsNotStorable) {
            int longCount;
            BigInteger sixtyFour = new BigInteger("64");
            BigInteger maxIntVal = BigInteger.valueOf(Integer.MAX_VALUE - 5);
            //maximum array length is Integer.MAX_VALUE - 5
            if (maxIntVal.multiply(sixtyFour).compareTo(value) != 1) {
                throw new Error("You can not store that many bits! Amount of bits must be smaller than 64*(2^32 - 5)\n Also consider max heap size!");
            }
            //Calculate needed longs
            longCount = value.divide(sixtyFour).intValue();
            if (value.mod(sixtyFour).intValue() > 0) longCount++;
            initArray(longCount);
        } else {
            if (value.signum() == -1) throw new Error("Max value is negative!");
            initArrayViaMaxValue(value);
        }
    }

    /**
     * Size of the BitSet in 8 bytes
     *
     * @param bytes8 Size of the BitSet in 8 bytes
     */
    public BigBitSet(int bytes8) {
        if (bytes8 <= 0) throw new Error("Invalid count!");
        initArray(bytes8);
    }

    public void setDecimalValue(BigInteger valueToSet) {
        byte[] bytes;
        int i, j;
        BigInteger mod;
        //Check size, if maxValueDecimal could be set
        if (maxValueDecimal != null && (valueToSet.compareTo(maxValueDecimal)) == 1)
            throw new Error("Value is to big!");
        //For every long in long array
        for (i = 0; i < longCount; i++) {
            mod = valueToSet.mod(baseDecimal);
            bytes = mod.toByteArray();
            //Set byte array
            for (j = 0; j < bytes.length; j++) {
                bitSetArray[i] = (bitSetArray[i] << 8) + (bytes[j] & 0xff);
            }
            valueToSet = valueToSet.divide(baseDecimal);
        }
    }

    private void initArrayViaMaxValue(BigInteger max) {
        BigInteger temp = max;
        BigInteger zero = new BigInteger("0");
        while (temp.compareTo(zero) != 0) {
            longCount++;
            temp = temp.divide(baseDecimal);
        }
        initArray(longCount);
    }

    private void initArray(int length) {
        longCount = length;
        bitCount = BigInteger.valueOf(length).multiply(bitsPerLongBigInteger);
        //Tries to calculate the max decimal value
        try {
            //BigInteger can only calculate with integer powers
            BigInteger maxInt = BigInteger.valueOf(Integer.MAX_VALUE);
            BigInteger one = BigInteger.valueOf(1);
            if (bitCount.compareTo(maxInt) == -1) {
                BigInteger two = new BigInteger("2");
                //Calculate the max decimal value; decrement one for the zero
                maxValueDecimal = two.pow(bitCount.intValue()).subtract(one);
            } else {
                maxValueDecimal = null;
            }
        } catch (Error e) {
            maxValueDecimal = null;
        }
        bitSetArray = new long[length];
    }

    /* Bit Setter and Getter */

    /**
     * @param index
     * @internal akkuLongA for shift
     */
    public void setBit(int index) {
        //Save unnecessary shift operations via modulo
        akkuLongA = index & (bitsPerLong - 1);
        //Shift mask and set bit
        bitSetArray[index / bitsPerLong] = bitSetArray[index / bitsPerLong] | (1L << akkuLongA);
    }

    /**
     * @param index
     * @internal akkuLongA for shift
     * @internal akkuIntA for array index
     */
    public void setBit(long index) {
        //Save unnecessary shift operations via modulo
        akkuLongA = index & (bitsPerLong - 1);
        //Calculate index
        akkuIntA = (int) (index / bitsPerLong);
        //Shift mask and set bit
        bitSetArray[akkuIntA] = bitSetArray[akkuIntA] | (1L << akkuLongA);
    }

    /**
     * @param index Bit position starting with 0
     * @internal akkuLongA for shift
     * @internal akkuIntA for array index
     */
    public void setBit(BigInteger index) {
        //Save unnecessary shift operations via modulo
        akkuLongA = index.mod(bitsPerLongBigInteger).intValue();
        //Big integer index
        akkuIntA = index.divide(bitsPerLongBigInteger).intValue();
        //Shift mask and set bit
        bitSetArray[akkuIntA] = bitSetArray[akkuIntA] | (1L << akkuLongA);
    }

    /**
     * @param index Bit position starting with 0
     * @internal akkuLongA for shift
     */
    public boolean getBit(int index) {
        //Save unnecessary shift operations via modulo
        akkuLongA = index & (bitsPerLong - 1);
        return ((bitSetArray[index / bitsPerLong] >> akkuLongA) & 1) == 1;
    }

    /**
     * @param index Bit position starting with 0
     * @internal akkuLongA for shift
     * @internal akkuIntA for array index
     */
    public boolean getBit(long index) {
        //Save unnecessary shift operations via modulo
        akkuLongA = index & (bitsPerLong - 1);
        //Calculate index
        akkuIntA = (int) (index / bitsPerLong);
        return ((bitSetArray[akkuIntA] >> akkuLongA) & 1) == 1;
    }

    /**
     * @param index Bit position starting with 0
     * @internal akkuLongA for shift
     * @internal akkuIntA for array index
     */
    public boolean getBit(BigInteger index) {
        //Save unnecessary shift operations via modulo
        akkuLongA = index.mod(bitsPerLongBigInteger).intValue();
        //Big integer index
        akkuIntA = index.divide(bitsPerLongBigInteger).intValue();
        //Shift mask and set bit
        return ((bitSetArray[akkuIntA] >> akkuLongA) & 1) == 1;
    }

    /*    logical operations    */

    /**
     * @param otherSet The BigBitSet
     * @internal akkuIntA as counting index
     * @internal akkuIntB as max index
     * @internal akkuBitSet to store the other BitSet arrays
     */
    public void binaryAnd(BigBitSet otherSet) {
        akkuBitSet = otherSet.bitSetArray;
        akkuIntB = longCount;
        if (otherSet.getLongCount() < akkuIntB) akkuIntB = otherSet.getLongCount();
        for (akkuIntA = 0; akkuIntA < akkuIntB; akkuIntA++) {
            bitSetArray[akkuIntA] &= akkuBitSet[akkuIntA];
        }
    }

    /**
     * @param otherSet The BigBitSet
     * @internal akkuIntA as counting index
     * @internal akkuIntB as max index
     * @internal akkuBitSet to store the other BitSet arrays
     */
    public void binaryOr(BigBitSet otherSet) {
        akkuBitSet = otherSet.bitSetArray;
        akkuIntB = longCount;
        if (otherSet.getLongCount() < akkuIntB) akkuIntB = otherSet.getLongCount();
        for (akkuIntA = 0; akkuIntA < akkuIntB; akkuIntA++) {
            bitSetArray[akkuIntA] |= akkuBitSet[akkuIntA];
        }
    }

    /*    Arithmetic operations    */

    /**
     * @internal akkIntA as counter index
     */
    public void increment() {
        akkuIntA = -1;
        //Increment the longs until there is no more overflow, or the end is reached
        while (++akkuIntA < longCount && (bitSetArray[akkuIntA]++ == -1)) ;
    }

    /**
     * Starts incrementing at a special long
     *
     * @internal akkIntA as counter index
     */
    public void increment(int index) {
        akkuIntA = index;
        //Increment the longs until there is no more overflow, or the end is reached
        while (++akkuIntA < longCount && (bitSetArray[akkuIntA]++ == -1)) ;
    }

    /**
     * @internal akkIntA as counter index
     */
    public void decrement() {
        akkuIntA = longCount;
        //Decrement the longs until there is no more overflow, or the end is reached
        while (--akkuIntA >= 0 && (bitSetArray[akkuIntA]-- == 0)) ;
    }

    /**
     * Starts decrementing at special index
     *
     * @internal akkIntA as counter index
     */
    public void decrement(int index) {
        akkuIntA = index;
        //Decrement the longs until there is no more overflow, or the end is reached
        while (--akkuIntA >= 0 && (bitSetArray[akkuIntA]-- == 0)) ;
    }

    /**
     * @param otherSet the bits to add
     * @internal akkuBitSet to store the other bitset
     * @internal akkuIntB to define max index
     * @internal akkuIntA as index
     * @internal akkuLongA as temporary storage to check for overflows
     */
    public void add(BigBitSet otherSet) {
        akkuBitSet = otherSet.bitSetArray;
        akkuIntB = longCount;
        if (otherSet.getLongCount() < akkuIntB) akkuIntB = otherSet.getLongCount();
        for (akkuIntA = 0; akkuIntA < akkuIntB; akkuIntA++) {
            akkuLongA = bitSetArray[akkuIntA] + akkuBitSet[akkuIntA];
            bitSetArray[akkuIntA] = akkuLongA;
            //TODO check for overflow
            if (((bitSetArray[akkuIntA] >>> maxBitIndexLong) + (akkuBitSet[akkuIntA] >>> maxBitIndexLong)) == 1) {
                bitSetArray[akkuIntA] = akkuLongA;
                increment(akkuIntA + 1);
            } else bitSetArray[akkuIntA] = akkuLongA;
        }
    }

    /*    Conversions     */

    public BigInteger toBigInteger() {
        BigInteger sum = new BigInteger("0");
        BigInteger multiplier;
        long temp;
        for (int i = 0; i < longCount; i++) {
            multiplier = baseDecimal.pow(i);
            temp = bitSetArray[i];
            //Check if sign bit is set
            if (temp < 0) {
                //Unset sign bit
                temp = temp ^ (1L << (bitsPerLong - 1));
                //Add all except MSB
                sum = sum.add(BigInteger.valueOf(temp).multiply(multiplier));
                //Add value of MSB
                sum = sum.add(MSBValueDecimal.multiply(multiplier));
            }
            //Else get the integer value of the long, multiply it with its power and add it up
            else sum = sum.add(BigInteger.valueOf(temp).multiply(multiplier));
        }
        return sum;
    }

    @Override
    public String toString() {
        int i, j;
        int indexMax = longCount - 1;
        String[] out = new String[longCount];
        String temp;
        for (i = indexMax; i >= 0; i--) {
            temp = Long.toBinaryString(bitSetArray[i]);
            for (j = temp.length(); j < bitsPerLong; j++) temp = "0" + temp;
            out[indexMax - i] = temp;
        }
        return String.join("", out);
    }

    /**
     * @param o BigBitSet to compare to
     * @return The result
     * @internal akkuIntA as coungting index
     * @internal akkuBitSet to store the other BitSet arrays
     */
    @Override
    public int compareTo(BigBitSet o) {
        akkuBitSet = o.bitSetArray;
        akkuIntA = longCount;
        //If one is smaller only the lower bits are checked
        if (o.getLongCount() < akkuIntA) akkuIntA = o.getLongCount();
        //Decrements the index until the longs are not identical
        while (--akkuIntA >= 0 && akkuBitSet[akkuIntA] == bitSetArray[akkuIntA]) ;
        if (akkuIntA == -1) return 0;
        akkuLongA = akkuBitSet[akkuIntA];
        akkuLongB = bitSetArray[akkuIntA];
        return (akkuLongA < akkuLongB) ^ (akkuLongA < 0) ^ (akkuLongB < 0) ? 1 : -1;
    }

    /*
    GETTER
    */

    public long[] getBitSetArray() {
        return bitSetArray;
    }

    public BigInteger getMaxValueDecimal() {
        return maxValueDecimal;
    }

    public BigInteger getLongMaxDecimal() {
        return longMaxDecimal;
    }

    public BigInteger getMSBValueDecimal() {
        return MSBValueDecimal;
    }

    public BigInteger getBaseDecimal() {
        return baseDecimal;
    }

    public int getBitsPerLong() {
        return bitsPerLong;
    }

    public BigInteger getBitsPerLongBigInteger() {
        return bitsPerLongBigInteger;
    }

    public int getLongCount() {
        return longCount;
    }

    public BigInteger getBitCount() {
        return bitCount;
    }
}
