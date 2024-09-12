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
