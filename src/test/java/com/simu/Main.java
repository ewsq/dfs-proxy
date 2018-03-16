package com.simu;

import java.io.*;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author DengrongGuan
 * @create 2018-02-09
 **/
public class Main {
    public static void main(String[] args){
        try {
            FileOutputStream f = new FileOutputStream("/Users/dengrongguan/test.zip");
            ZipOutputStream zip = new ZipOutputStream(new BufferedOutputStream(f));
//            zip.putNextEntry(new ZipEntry("xml/"));
            zip.putNextEntry(new ZipEntry("xml/xml/"));
            zip.putNextEntry(new ZipEntry("xml/"));
            zip.putNextEntry(new ZipEntry("xml/adb/"));
            zip.putNextEntry(new ZipEntry("xml/adb/bsg"));
            zip.putNextEntry(new ZipEntry("xml/adb/bsg1"));
            zip.putNextEntry(new ZipEntry("xml/adb/bsg2"));
            zip.putNextEntry(new ZipEntry("xml/xml/test.txt"));
            InputStream inputStream = new FileInputStream(new File("/Users/dengrongguan/test.txt"));
            int len;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) > 0) {
                zip.write(buffer, 0, len);
            }
            inputStream.close();
            zip.closeEntry();
//            zip.putNextEntry(new ZipEntry("xml/xml/test.txt"));
//            inputStream = new FileInputStream(new File("/Users/dengrongguan/test.txt"));
//            while ((len = inputStream.read(buffer)) > 0) {
//                zip.write(buffer, 0, len);
//            }
//            inputStream.close();
//            zip.closeEntry();
//            zip.putNextEntry(new ZipEntry("test.txt"));
//            inputStream = new FileInputStream(new File("/Users/dengrongguan/test.txt"));
//            while ((len = inputStream.read(buffer)) > 0) {
//                zip.write(buffer, 0, len);
//            }
//            inputStream.close();
//            zip.closeEntry();
            zip.close();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
