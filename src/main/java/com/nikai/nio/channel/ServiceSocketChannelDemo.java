package com.nikai.nio.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * nio-chat-room com.nikai.nio.channel
 *
 * @author: nikai
 * @Description:
 * @Date: Create in 12:27 2019/6/2
 * @Modified By:
 */
public class ServiceSocketChannelDemo {

    public static class TCPEchoServer implements Runnable {

        /*服务器地址*/
        private InetSocketAddress inetSocketAddress;

        public TCPEchoServer(int port) {
            this.inetSocketAddress = new InetSocketAddress(port);
        }

        @Override
        public void run() {
            Charset utf8 = Charset.forName("UTF-8");
            ServerSocketChannel ssc = null;
            Selector selector = null;

            Random rnd = new Random();
            try {

                /*创建选择器*/
                selector = Selector.open();

                /*创建通道*/
                ssc = ServerSocketChannel.open();
                ssc.configureBlocking(false);

                /*设置服务器监听端口，设置最大连接缓冲数为100*/
                ssc.bind(inetSocketAddress, 100);

                /*服务器通道只能对tcp链接事件感兴趣*/
                ssc.register(selector, SelectionKey.OP_ACCEPT);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            System.out.println("Server start with address:" + inetSocketAddress.getHostName());

            try {
                while (!Thread.currentThread().isInterrupted()) {
                    int n = selector.select();
                    if (n == 0) {
                        return;
                    }

                    Set<SelectionKey> keySet = selector.keys();
                    Iterator<SelectionKey> keyIterator = keySet.iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        keyIterator.remove();
                        try {
                            if (key.isAcceptable()) {
                                SocketChannel sc = ssc.accept();
                                sc.configureBlocking(false);

                                sc.register(selector, SelectionKey.OP_READ);
                                System.out.println("accept from " + sc.getRemoteAddress());


                            }

                            if (key.isAcceptable()) {
//                                (Buffers)key.attachment();
                                SocketChannel socketChannel = (SocketChannel) key.channel();
                                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                socketChannel.read(byteBuffer);
                                byteBuffer.flip();

                                CharBuffer charBuffer = utf8.decode(byteBuffer);
                                System.out.println(charBuffer.array());



                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
