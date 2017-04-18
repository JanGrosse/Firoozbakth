package main.DataStructures;

public class ArrayPair<A, B> {
    private A[] firstArray;
    private B[] secondArray;

    public ArrayPair(A[] firstArray, B[] secondArray) {
        this.firstArray = firstArray;
        this.secondArray = secondArray;
    }

    public A[] getFirstArray() {
        return firstArray;
    }

    public B[] getSecondArray() {
        return secondArray;
    }
}
