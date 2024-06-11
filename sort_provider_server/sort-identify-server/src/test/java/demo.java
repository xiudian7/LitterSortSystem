

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class demo {
    public static void main(String[] args) {
        try {
            // 创建 URL 对象
            URL url = new URL("http://192.168.2.121:5000");

            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 设置请求方法为 POST
            connection.setRequestMethod("POST");

            // 设置请求头
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");

            // 允许写入
            connection.setDoOutput(true);

            // 创建 JSON 请求体
            String jsonInputString = "{\"imagePath\": \"images/1.jpg\"}";

            // 将请求体写入到连接的输出流中
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // 获取响应码
            int code = connection.getResponseCode();
            System.out.println("Response Code: " + code);

            // 读取响应内容
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            // 解析 JSON 响应
            JSONObject jsonResponse = JSON.parseObject(response.toString());
            int prediction = jsonResponse.getIntValue("prediction");
            System.out.println(prediction);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}