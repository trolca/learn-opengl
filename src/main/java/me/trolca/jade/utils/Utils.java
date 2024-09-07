package me.trolca.jade.utils;

import java.nio.ByteBuffer;

public class Utils {

    public static String bytesToString(byte[] bytes){
        StringBuilder stringBuilder = new StringBuilder();

        for(byte b : bytes){
            stringBuilder.append((char) b);
        }

        return stringBuilder.toString();
    }
}
