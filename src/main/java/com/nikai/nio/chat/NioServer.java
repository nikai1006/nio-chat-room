package com.nikai.nio.chat;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * nio-chat-room com.nikai.nio.chat
 *
 * @author: nikai
 * @Description:
 * @Date: Create in 22:19 2019/5/7
 * @Modified By:
 */
public class NioServer {

    public void start() throws Exception {
        //1 创建Selector
        Selector register = Selector.open();

//        2、通过ServerSocketChannel创建channel通道
        ServerSocketChannel socketChannel = ServerSocketChannel.open();

//        3、为channel通道绑定监听端口
        socketChannel.bind(new InetSocketAddress(18000));

//        4、设置chanel为非阻塞模式
        socketChannel.configureBlocking(false);

//        5、将channel注册到selector上，监听连接事件
        socketChannel.register(register, SelectionKey.OP_ACCEPT);
        System.out.println("服务器启动成功");

//        6、循环等待新接入的连接
        while (true) {
            //获取可用的channel数量
            int readyChannels = register.select();

            if (readyChannels == 0) {
                continue;
            }
            Set<SelectionKey> selectionKeys = register.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
//                实例
                SelectionKey selectionKey = iterator.next();

                iterator.remove();
//                根据相应的状态来调用相应的处理逻辑

                //如果是接入事件
//                do something
                if (selectionKey.isAcceptable()) {
                    acceptHandler(socketChannel, register);
                }

                //如果是可读事件
//                do something
                if (selectionKey.isReadable()) {
                    readHandle(selectionKey, register);
                }
            }
        }
//        7、根据就绪状态，调用对方方法处理业务逻辑

//        8、
    }

    /**
     *
     */
    private void acceptHandler(ServerSocketChannel serverSocketChannel, Selector selector) throws Exception {

        //如果是接入事件，创建socketChannel
        SocketChannel socketChannel = serverSocketChannel.accept();

//        将socketChannel设置为非阻塞工作模式
        socketChannel.configureBlocking(false);

//        将channel注册到selector上，监听可读事件
        socketChannel.register(selector, SelectionKey.OP_READ);

//        回复客户端提示信息
        socketChannel.write(Charset.forName("UTF-8").encode("你与聊天室其他人都不是朋友关系，请注意饮食安全"));

    }

    private void readHandle(SelectionKey selectionKey, Selector selector) throws Exception {
//        要从selectionKey中获取到已经就绪的channel
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

//        创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//    循环读取客户端请求信息
        String request = "";
        while (socketChannel.read(byteBuffer) > 0) {
//            切换buffer为读模式
            byteBuffer.flip();

//            读取buffer中的内容
            request += Charset.forName("UTF-8").decode(byteBuffer);
        }
        //    将channel再次注册到selector上，监听他的可读事件
        socketChannel.register(selector, SelectionKey.OP_READ);

//        将客户端发送的请求信息广播给其他客户端
        if (request.length() > 0) {
            //广播给其他客户端
            System.out.println("::" + request);
        }
    }

    public static void main(String[] args) throws Exception {
        NioServer nioServer = new NioServer();
        nioServer.start();
    }


}
