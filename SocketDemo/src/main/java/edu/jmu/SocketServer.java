package edu.jmu;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.*;

public class SocketServer {
    // 配置连接参数
    public final static int port = 4567;

    // 执行拍照，返回图片路径
    public static String getPhoto() throws IOException, InterruptedException {
        String substring = UUID.randomUUID().toString().substring(0, 6);
        Process process = Runtime.getRuntime().exec("ffmpeg -i /dev/video0 -frames:v 1 -f image2 "+substring+".jpg");
        process.waitFor(); //等待拍照完成
        return "/home/xiudian/mouseMice/"+substring+".jpg";
    }

    // 发送图片
    public static void send(Socket socket, String filePath) throws IOException {
        // 加载图片
        FileInputStream fis = new FileInputStream(filePath);
        // 获取自己的输出流
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        // 设置缓存数组
        byte[] buf = new byte[1024];
        int len = 0;
        // 发送图片
        while ((len = fis.read(buf)) != -1) {
            bufferedOutputStream.write(buf, 0, len);
        }
        // 通知, 数据发送完毕
        bufferedOutputStream.flush();
        // 关闭资源
        socket.shutdownOutput();
        bufferedOutputStream.close();
    }

    public static void main(String[] args) throws IOException {
        ExecutorService executorService = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(5, true),
                Executors.defaultThreadFactory());

        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("服务器启动成功");
            Socket socket = null;
            int count = 0; // 统计客户端的数量
            while (true) {
                socket = server.accept();
                count++;
                String clientIp = socket.getInetAddress().getHostAddress();
                System.out.println("第 " + count + " 个客户端成功连接，IP 地址: " + clientIp);
                final Socket clientSocket = socket;
                executorService.execute(() -> {
                    try {
                        String filePath = getPhoto();
                        send(clientSocket, filePath);
                        clientSocket.close(); //关闭Socket
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (server != null) {
                server.close(); //关闭ServerSocket
            }
        }
    }
}
