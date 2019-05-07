package com.nikai.nio.chat;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * nio-chat-room com.nikai.nio.chat 客户端
 *
 * @author: nikai
 * @Description:
 * @Date: Create in 22:19 2019/5/7
 * @Modified By:
 */
public class NioClient {

    public void start() throws Exception {
//        链接服务器端
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 18000));
        System.out.println("客户端启动成功................");

//        向服务器发送数据
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            if (nextLine != null && nextLine.length() > 0) {
                socketChannel.write(Charset.forName("UTF-8").encode(nextLine));
            }
        }

//        接收服务器的响应

    }

    public static void main(String[] args) throws Exception {
        NioClient nioClient = new NioClient();
        nioClient.start();
    }

}
