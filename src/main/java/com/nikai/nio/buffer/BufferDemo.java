package com.nikai.nio.buffer;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * nio-chat-room com.nikai.nio.buffer
 *
 * @author: nikai
 * @Description:
 * @Date: Create in 11:20 2019/6/2
 * @Modified By:
 */
public class BufferDemo {

    public static void decode(String str) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(128);
        buffer.put(str.getBytes("UTF-8"));
        buffer.flip();

        //对获取utf8的编解码
        Charset utf8 = Charset.forName("UTF-8");
        //对buffer中的内容解码
        CharBuffer charBuffer = utf8.decode(buffer);

        /*array()返回的就是内部的数组引用，编码以后的有效长度是0-limit*/
        char[] chars = Arrays.copyOf(charBuffer.array(), charBuffer.limit());
        System.out.println(chars);
    }

    public static void encode(String str) throws Exception {
        CharBuffer charBuffer = CharBuffer.allocate(128);
        charBuffer.append(str);
        charBuffer.flip();

        Charset utf8 = Charset.forName("UTF-8");
        ByteBuffer byteBuffer = utf8.encode(charBuffer);
        /**/
        byte[] bytes = Arrays.copyOf(byteBuffer.array(), byteBuffer.limit());
        System.out.println(Arrays.toString(bytes));
    }

    public static void main(String[] args) throws Exception {
        BufferDemo.decode("我是谁");
        BufferDemo.encode("我是谁");
    }
}
