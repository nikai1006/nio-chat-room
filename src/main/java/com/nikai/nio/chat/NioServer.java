package com.nikai.nio.chat;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
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
        socketChannel.bind(new InetSocketAddress(180000));

//        4、设置chanel为非阻塞模式
        socketChannel.configureBlocking(false);

//        5、将channel注册到selector上，监听连接事件
        socketChannel.register(register, SelectionKey.OP_ACCEPT);

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
                //如果是可读事件
//                do something
            }
        }
//        7、根据就绪状态，调用对方方法处理业务逻辑

//        8、
    }

    public static void main(String[] args) {
        NioServer nioServer = new NioServer();
    }
}
