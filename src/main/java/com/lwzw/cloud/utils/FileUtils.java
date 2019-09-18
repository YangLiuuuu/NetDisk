package com.lwzw.cloud.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {

    /**
     * 压缩文件
     * @param srcFiles 需要压缩的文件列表
     * @param outputStream 压缩文件输出刘
     * @throws IOException
     */
    public static void toZip(List<File>srcFiles, OutputStream outputStream) throws IOException {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        int len;
        try {
            zos = new ZipOutputStream(outputStream);
            for (File srcFile:srcFiles){
                byte[] buf = new byte[2*1024];
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                FileInputStream in = new FileInputStream(srcFile);
                while ((len=in.read(buf))!=-1){
                    zos.write(buf,0,len);
                }
                zos.closeEntry();
                in.close();
            }
            long end = System.currentTimeMillis();
            System.out.println("文件压缩完成,耗时"+(end-start) + "ms");
        }catch (IOException e){
            throw new IOException("文件压缩出错");
        }finally {
            if (zos!=null){
                try {
                    zos.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
