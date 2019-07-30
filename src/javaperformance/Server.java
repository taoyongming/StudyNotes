package javaperformance;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//Socket 启动服务
public class Server {

    private static int DEFAULT_PORT = 12345;
    private static ServerSocket server;

    public static void start() throws IOException {
        start(DEFAULT_PORT);
    }

    public static void start(int port) throws IOException {
        if (server != null) {
            return;
        }

        try {
            // 启动服务
            server = new ServerSocket(port);
            // 通过无线循环监听客户端连接
            while (true) {

                Socket socket = server.accept();
                // 当有新的客户端接入时，会执行下面的代码
                long start = System.currentTimeMillis();
                new Thread(new ServerHandler(socket)).start();

                long end = System.currentTimeMillis();

                System.out.println("Spend time is " + (end - start));
            }
        } finally {
            if (server != null) {
                System.out.println(" 服务器已关闭。");
                server.close();
            }

        }

    }

    public static void main(String[] args) throws InterruptedException{

        // 运行服务端
        new Thread(new Runnable() {
            public void run() {
                try {
                    Server.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
