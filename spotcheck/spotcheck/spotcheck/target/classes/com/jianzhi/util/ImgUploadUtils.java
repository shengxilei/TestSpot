package com.jianzhi.util;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;


/**
 * @description: 从服务器获得一个输入流(本例是指从服务器获得一个image输入流)
 * @author: Jeff
 * @date: 2019年12月7日
 * @return
 */
public class ImgUploadUtils {


    public static InputStream getInputStream(String urlPath) {
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try {
            URL url = new URL(urlPath);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            // 设置网络连接超时时间
            httpURLConnection.setConnectTimeout(3000);
            // 设置应用程序要从网络连接读取数据
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("GET");
            int responseCode = httpURLConnection.getResponseCode();
            System.out.println("responseCode is:" + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 从服务器返回一个输入流
                inputStream = httpURLConnection.getInputStream();
            } else {
                inputStream = httpURLConnection.getErrorStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    /**
     * @param resp
     * @param inputStream
     * @description: 将输入流输出到页面
     * @author: Jeff
     * @date: 2019年12月7日
     */
    public static void writeFile(HttpServletResponse resp, InputStream inputStream) {
        OutputStream out = null;
        try {
            out = resp.getOutputStream();
            int len = 0;
            byte[] b = new byte[1024];
            while ((len = inputStream.read(b)) != -1) {
                out.write(b, 0, len);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
