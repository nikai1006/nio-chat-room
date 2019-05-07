package com.nikai.nio.chat;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * nio-chat-room com.nikai.nio.chat
 *
 * @author: nikai
 * @Description:
 * @Date: Create in 23:14 2019/5/7
 * @Modified By:
 */
public class NioClientHandler implements Runnable {

    private Selector selector;

    public NioClientHandler(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            while (true) {
                //获取可用的channel数量
                int readyChannels = selector.select();

                if (readyChannels == 0) {
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    //                实例
                    SelectionKey selectionKey = iterator.next();

                    iterator.remove();
                    //                根据相应的状态来调用相应的处理逻辑

                    //如果是可读事件
                    //                do something
                    if (selectionKey.isReadable()) {
                        readHandle(selectionKey, selector);
                    }
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void readHandle(SelectionKey selectionKey, Selector selector) throws Exception {
//        要从selectionKey中获取到已经就绪的channel
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

//        创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//    循环读取服务器端请求信息
        String response = "";
        while (socketChannel.read(byteBuffer) > 0) {
//            切换buffer为读模式
            byteBuffer.flip();

//            读取buffer中的内容
            response += Charset.forName("UTF-8").decode(byteBuffer);
        }
        //    将channel再次注册到selector上，监听他的可读事件
        socketChannel.register(selector, SelectionKey.OP_READ);

//        将服务器发送的请求信息打印出来
        if (response.length() > 0) {
            //广播给其他客户端
            System.out.println(response);
        }
    }
}
