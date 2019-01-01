package com.test.io.bio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class IoTest {

    public void copy(String src, String dest) throws  FileNotFoundException{
        File file = new File(src);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isFile()) {
                        copyFile(files[i], new File(dest + File.separator + files[i].getName()));
                    }
                    if (files[i].isDirectory()) {
                        copyDir(files[i].getAbsolutePath(), dest + File.separator + files[i].getName());
                    }
                }
                copyDir(src,dest);
            }
            if (file.isFile()) {
                //copy文件
                copyFile(file,new File(dest));
            }
        } else {
            throw new FileNotFoundException("文件找不到");
        }
    }

    public void copyFile(File src, File dest) {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(src);
            fos = new FileOutputStream(dest);
            bis = new BufferedInputStream(fis);
            bos = new BufferedOutputStream(fos);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            ;
            bis.close();
            bos.close();
        } catch (IOException e) {

        }
    }

    public void copyDir(String src, String des) {
        File targetFile = new File(des);
        if (!targetFile.exists()) {
            targetFile.mkdir();
        }
        File[] files = new File(src).listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                copyFile(files[i], new File(targetFile.getAbsolutePath() + File.separator + files[i].getName()));
            }
            if (files[i].isDirectory()) {
                String newSource = src + File.separator + files[i].getName();
                String newTarget = des + File.separator + files[i].getName();
                copyDir(newSource, newTarget);
            }
        }
    }

    public void copyByChannel(File src, File dest) {
        FileChannel sourceChannel = null;
        FileChannel targetChannel = null;
        try {
            sourceChannel = new FileInputStream(src).getChannel();
            targetChannel = new FileOutputStream(dest).getChannel();
            for (long count = sourceChannel.size(); count > 0; ) {
                long transfer = sourceChannel.transferTo(sourceChannel.position(), count, targetChannel);
                count -= transfer;
            }
            sourceChannel.close();
            targetChannel.close();
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) throws FileNotFoundException{
        String src = "F:\\程序猿参考资料\\学习\\java基础\\Java视频教程（孙鑫）\\孙鑫Java无难事07\\Code3";
        String des = "F:\\程序猿参考资料\\学习\\java基础\\Java视频教程（孙鑫）\\孙鑫Java无难事07\\Code2";
        IoTest IO = new IoTest();
        IO.copy(src, des);
    }

}
