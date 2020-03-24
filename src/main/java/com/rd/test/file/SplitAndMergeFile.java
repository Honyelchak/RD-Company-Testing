package com.rd.test.file;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

/**
 * @author Honyelchak
 * @create 2020-03-23 21:09
 * 文件操作：
 * 将一个100M文件分割成20份，再合并成新文件。
 * 对两个文件内容进行SHA1加密算法对比一致为正确。
 */
public class SplitAndMergeFile {
    // 源文件
    private File srcFile;
    // 源文件名称
    private String fileName;
    // 分割后的文件后缀
    private String fileSuffix;
    // 源文件总大小
    private Long totalSize;
    // 每一份的大小
    private Long blockSize;
    // 分割后小文件的存储路径
    private List<String> blockPath;
    // 存放小文件的目录
    private String saveDirectory;
    // 分割的份数
    private int copies;

    public SplitAndMergeFile(File srcFile, String fileSuffix) {
        String srcFilePath = srcFile.getAbsolutePath();
        this.fileName = srcFile.getName();
        this.fileSuffix = Optional.ofNullable(fileSuffix).orElse(srcFilePath.substring(srcFilePath.lastIndexOf('.')));
        this.srcFile = srcFile;
        this.copies = copies;
        this.totalSize = srcFile.length();
        this.blockPath = new ArrayList<>();
    }

    // 计算每一块文件的大小。
    private void calculateBlockSize() {
        this.blockSize = (long) Math.ceil(this.totalSize * 1.0 / this.copies);
        //this.blockSize = (long) (this.totalSize / this.copies);
        System.out.println("预计拆分后小文件的平均大小: " + this.blockSize);
    }

    // 校验文件是否合法
    public boolean checkIsLegal() {
        if (!srcFile.exists()) {
            System.out.println("srcFile文件不存在");
            return false;
        }
        if (srcFile.isDirectory()) {
            System.out.println("该文件为一个目录");
            return false;
        }
        return true;
    }

    /**
     * 文件分割
     *
     * @param copies        指定分割的份数
     * @param saveDirectory 指定小文件存储的目录i
     */
    public void split(int copies, String saveDirectory) {
        this.copies = copies;
        this.saveDirectory = saveDirectory;
        if (!checkIsLegal()) {
            return;
        }
        if (saveDirectory == null) saveDirectory = srcFile.getParent();

        System.out.println(saveDirectory);
        calculateBlockSize();

        for (int i = 1; i <= copies; i++) {
            //List容器里面增加每一块的路径
            this.blockPath.add(saveDirectory + "/" + this.fileName + ".part" + i);
        }
        long beginPointer = 0;//起始点
        long actualBlockSize = blockSize;//实际大小
        for (int i = 0; i < copies; i++) {
            if (i == copies - 1) {
                //最后一块
                actualBlockSize = this.totalSize - beginPointer;
            }
            this.saveFileFromIndex(i, beginPointer, actualBlockSize);
            System.out.println("split success part of " + i + ": " + this.blockPath.get(i) + " 长度为：" + new File(this.blockPath.get(i)).length());
            beginPointer += actualBlockSize;
        }

    }


    /**
     * 从源文件指定位置，生成小文件
     *
     * @param blockIndex      第一个小文件(用于获取小文件路径)
     * @param beginPointer    读取源文件的指针()
     * @param actualBlockSize 实际需要生成小文件的大小。
     */
    private void saveFileFromIndex(int blockIndex, long beginPointer, long actualBlockSize) {
        File dstFile = new File(this.blockPath.get(blockIndex));

        RandomAccessFile randomAccessFile = null;
        BufferedOutputStream bufferedOutputStream = null;

        try {
            randomAccessFile = new RandomAccessFile(this.srcFile, "r");
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(dstFile));
            randomAccessFile.seek(beginPointer);
            byte[] flush = new byte[1024 * 10];
            int len = 0;
            while ((len = randomAccessFile.read(flush)) != -1) {
                //写出
                if (actualBlockSize - len >= 0)//判断是否足够
                {
                    bufferedOutputStream.write(flush, 0, len);//写出
                    actualBlockSize -= len;//剩余量
                } else {
                    //读取每一块实际大小的最后一小部分   最后一次写出
                    bufferedOutputStream.write(flush, 0, (int) actualBlockSize);
                    break;//每个block最后一部分读取完之后，一定要break，否则就会继续读取
                }
            }
            bufferedOutputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedOutputStream.close();
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 合并文件
     *
     * @param destPath 指定合并后文件的存储位置
     */
    public void merge(String destPath) {
        File destFile = new File(destPath);
        SequenceInputStream sequenceInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        Vector<InputStream> inputStreams = new Vector<>();

        try {
            for (int i = 0; i < this.blockPath.size(); i++) {
                inputStreams.add(new BufferedInputStream(new FileInputStream(new File(blockPath.get(i)))));
            }

            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(destFile, true));
            sequenceInputStream = new SequenceInputStream(inputStreams.elements());

            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = sequenceInputStream.read(bytes)) != -1) {
                bufferedOutputStream.write(bytes, 0, len);
            }
            bufferedOutputStream.flush();
            System.out.println("merge success");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                sequenceInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        // 源文件路径
        String srcfile = "D:\\Users\\Desktop\\test\\logstash-5.6.8.zip.part1";
        // 合并后的文件路径
        String fileAfterMergePath = "D:\\Users\\Desktop\\test\\11.txt";

        /*try {
            // 生成一个100M大小的文件
            FilesUtil.create(new File(srcfile), 100*1024*1024);
            System.out.println("100M文件create成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        // 创建SplitAndMergeFile文件实例
        SplitAndMergeFile splitFile = new SplitAndMergeFile(new File(srcfile), "zip");
        // 分割文件
        splitFile.split(20, null);
        // 合并文件
        splitFile.merge(fileAfterMergePath);
        // 合并后的文件
        File file1 = new File(fileAfterMergePath);
        // 源文件
        File file2 = new File(srcfile);
        // 合并后的文件生成的SHA1字符串
        String fileSha1 = FilesUtil.getFileSha1(file1);
        // 源文件生成的SHA1字符串
        String fileSha2 = FilesUtil.getFileSha1(file2);

        System.out.println("拆分前文件SHA1:" + fileSha1);
        System.out.println("拆分后文件SHA1:" + fileSha2);
        // 对比是否一致。
        if (fileSha1.equals(fileSha2)) {
            System.out.println("SHA1一致");
        } else {
            System.out.println("SHA1不一致");
        }

    }


}
