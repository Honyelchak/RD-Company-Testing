package com.rd.test.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Honyelchak
 * @create 2020-03-23 22:26
 */
public class FilesUtil {

    /**
     * 生成文件的SHA1
     *
     * @param file 需要计算的File
     * @return
     */
    public static String getFileSha1(File file) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[1024 * 1024 * 10];

            int len = 0;
            while ((len = in.read(buffer)) > 0) {
                digest.update(buffer, 0, len);
            }
            String sha1 = new BigInteger(1, digest.digest()).toString(16);
            int length = 40 - sha1.length();
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    sha1 = "0" + sha1;
                }
            }
            return sha1;
        } catch (IOException e) {
            System.out.println(e);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        return null;
    }


    /**
     * 创建一个指定大小的文件
     *
     * @param file   File对象(指定路径)
     * @param length 文件大小
     * @throws IOException
     */
    public static void createFileOfSpecifiedSize(File file, long length) throws IOException {
        RandomAccessFile r = null;
        try {
            r = new RandomAccessFile(file, "rw");
            r.setLength(length);
        } finally {
            if (r != null) {
                try {
                    r.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
