package com.fastcache.MCache.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class IOUtils {

    private final static int BUFFER_SIZE = 0x400; // 1024


    public static byte[] readInputStream(InputStream inStream) throws IOException{
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int len = 0;
        while( (len = inStream.read(buffer)) !=-1 ){
            if (len!=0) {
                outSteam.write(buffer, 0, len);
            }
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }


    public static String inputStream2String(InputStream inStream) throws IOException{

        return new String(readInputStream(inStream), "UTF-8");
    }


    public static void copyStream(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        for (;;) {
            int count = is.read(bytes, 0, BUFFER_SIZE);
            if (count == -1)
                break;
            os.write(bytes, 0, count);
        }
    }


    public static void copyFile(File src, File dst) throws IOException {
        FileInputStream in = new FileInputStream(src);
        FileOutputStream out = new FileOutputStream(dst);
        FileChannel inChannel = in.getChannel();
        FileChannel outChannel = out.getChannel();

        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }

        in.close();
        out.close();
    }


    public static void writeToFile(InputStream in, File target) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(target));
        int count;
        byte data[] = new byte[BUFFER_SIZE];
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1) {
            bos.write(data, 0, count);
        }
        bos.close();
    }


    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void closeQuietly(Closeable... closeables) {

        if (Preconditions.isNotBlank(closeables)) {

            for(Closeable closeable:closeables) {
                closeQuietly(closeable);
            }
        }
    }

}
