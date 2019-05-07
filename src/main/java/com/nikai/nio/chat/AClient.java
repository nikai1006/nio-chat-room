package com.nikai.nio.chat;

/**
 * nio-chat-room com.nikai.nio.chat
 *
 * @author: nikai
 * @Description:
 * @Date: Create in 0:08 2019/5/8
 * @Modified By:
 */
public class AClient {

    public static void main(String[] args) throws Exception{
        NioClient nioClient = new NioClient();
        nioClient.start(AClient.class.getSimpleName());
    }

}
