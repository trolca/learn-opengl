package me.trolca.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class Utils {

    public static String bytesToString(byte[] bytes){
        StringBuilder stringBuilder = new StringBuilder();

        for(byte b : bytes){
            stringBuilder.append((char) b);
        }

        return stringBuilder.toString();
    }

    public static  <T> void addValuesToArray(T[] array, T[] itemsToAdd, int from){
        if((array.length-from) < itemsToAdd.length)
            throw new IllegalArgumentException("The items to add array is longer than the space available in the core array!");

        System.arraycopy(itemsToAdd, 0, array, from, itemsToAdd.length);
    }

    public static <T> boolean contains(T[] array, T value){

        for(T arrValue : array){

            if(value.equals(arrValue))
                return true;

        }

        return false;
    }

    public static <T> boolean addNextFree(T[] array, T value){

        for(int i=0; i < array.length; i++){

            if(array[i] == null){
                array[i] = value;
                return true;
            }

        }

        return false;

    }

    public static String getTextFromInputStream(InputStream in) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();

        int data = in.read();
        while (data != -1) {
            stringBuilder.append((char) data);
            data = in.read();

        }
        in.close();

        return stringBuilder.toString();
    }
}
