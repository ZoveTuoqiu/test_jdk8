import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.HttpClient;

import java.io.IOException;

/**
 * Created by gaoziye on 2017/4/21.
 */
public class RequestTest {

//    public static void main(String[] args) {
//        for (int i = 0; i < 500; i++) {
//            String url = "https://pub.m.iqiyi.com/h5/mina/bubble/index/" + i + "/";
//            long start = System.currentTimeMillis();
//            String response = doGet(url, "utf-8");
//            System.out.println(url + " total time: " + (System.currentTimeMillis() - start) + "ms");
//
//            JSONObject object = JSONObject.parseObject(response);
//            JSONArray list = object.getJSONObject("data").getJSONArray("list");
//            System.out.println("list size: " + list.size());
//        }
//    }

//    private static String doGet(String url, String charset) {
//                /*
//         * 使用 GetMethod 来访问一个 URL 对应的网页,实现步骤: 1:生成一个 HttpClinet 对象并设置相应的参数。
//         * 2:生成一个 GetMethod 对象并设置响应的参数。 3:用 HttpClinet 生成的对象来执行 GetMethod 生成的Get
//         * 方法。 4:处理响应状态码。 5:若响应正常，处理 HTTP 响应内容。 6:释放连接。
//         */
//        /* 1 生成 HttpClinet 对象并设置参数 */
//        HttpClient httpClient = new HttpClient();
//        // 设置 Http 连接超时为5秒
//        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
//
//        /* 2 生成 GetMethod 对象并设置参数 */
//        GetMethod getMethod = new GetMethod(url);
//        // 设置 get 请求超时为 5 秒
//        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
//        // 设置请求重试处理，用的是默认的重试处理：请求三次
//        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
//        String response = "";
//        /* 3 执行 HTTP GET 请求 */
//        try {
//            int statusCode = httpClient.executeMethod(getMethod);
//            /* 4 判断访问的状态码 */
//            if (statusCode != HttpStatus.SC_OK) {
//                System.err.println("Method failed: " + getMethod.getStatusLine());
//            }
//            /* 5 处理 HTTP 响应内容 */
//            // HTTP响应头部信息，这里简单打印
//            Header[] headers = getMethod.getResponseHeaders();
////            for (Header h : headers)
////                System.out.println(h.getName() + "------------ " + h.getValue());
//            // 读取 HTTP 响应内容，这里简单打印网页内容
//            byte[] responseBody = getMethod.getResponseBody();// 读取为字节数组
//            response = new String(responseBody, charset);
//            System.out.println("----------response:" + response);
//            // 读取为 InputStream，在网页内容数据量大时候推荐使用
//            // InputStream response = getMethod.getResponseBodyAsStream();
//
//        } catch (HttpException e) {
//            // 发生致命的异常，可能是协议不对或者返回的内容有问题
//            System.out.println("Please check your provided http address!");
//            e.printStackTrace();
//        } catch (IOException e) {
//            // 发生网络异常
//            e.printStackTrace();
//        } finally {
//            /* 6 .释放连接 */
//            getMethod.releaseConnection();
//        }
//        return response;
//    }

}
