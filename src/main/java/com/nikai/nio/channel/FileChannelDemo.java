package com.nikai.nio.channel;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * nio-chat-room com.nikai.nio.channel
 *
 * @author: nikai
 * @Description:
 * @Date: Create in 11:36 2019/6/2
 * @Modified By:
 */
public class FileChannelDemo {

    public static void main(String[] args) {
        try {
            File file = new File("./README1.md");
            if (!file.exists()) {
                file.createNewFile();
            }

            /*根据文件输出流创建与这个文件相关的通道*/
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            FileChannel fileChannel = fileOutputStream.getChannel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(64);
            byteBuffer.put("我是尼凯 \n".getBytes("UTF-8"));
            fileChannel.write(byteBuffer);
            byteBuffer.flip();
            byteBuffer.clear();

            byteBuffer.put("你好，尼凯 \n".getBytes("UTF-8"));
            fileChannel.write(byteBuffer);
            byteBuffer.flip();
            byteBuffer.clear();

            fileOutputStream.close();
            fileChannel.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Path path = Paths.get("./README1.md");
            FileChannel fileChannel = FileChannel.open(path);

            ByteBuffer byteBuffer = ByteBuffer.allocate((int) fileChannel.size() + 1);
            Charset utf8 = Charset.forName("UTF-8");
            /*阻塞模式，读完才返回*/
            fileChannel.read(byteBuffer);

            byteBuffer.flip();
            CharBuffer charBuffer = utf8.decode(byteBuffer);
            System.out.println(charBuffer.toString());
            byteBuffer.clear();

            fileChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
