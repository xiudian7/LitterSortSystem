package cn.litterSortSystem.xiudian7.identify.socket;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    //1.创建一个服务器端Socket，即ServerSocket，指定绑定的端口，并监听此端口
    ServerSocket serverSocket = new ServerSocket(5900);
    InetAddress address = InetAddress.getLocalHost();
    String ip = address.getHostAddress();
    Socket socket = null;
    //2.调用accept()等待客户端连接
    System.out.println("~~~服务端已就绪，等待客户端接入~，服务端ip地址: " + ip);
    socket = serverSocket.accept();
    //3.连接后获取输入流，读取客户端信息
    InputStream is=null;
    InputStreamReader isr=null;
    BufferedReader br=null;
    OutputStream os=null;
    PrintWriter pw=null;
    is = socket.getInputStream();     //获取输入流
    isr = new InputStreamReader(is,"UTF-8");
    br = new BufferedReader(isr);
    String info = null;
        while((info=br.readLine())!=null){//循环读取客户端的信息
        System.out.println("客户端发送过来的信息" + info);
    }
        socket.shutdownInput();//关闭输入流
        socket.close();
}
