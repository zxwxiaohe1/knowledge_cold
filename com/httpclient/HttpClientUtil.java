package com.sinux.common.https;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sinux.common.ConstantBusiness;
import com.sinux.common.utils.StringUtils;
import com.sinux.logger.Logger;
import com.sinux.service.dingTalk.models.OApiException;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * HttpClientUtil：利用httpClient进行post请求
 *
 * @author lihp
 * @date 2018/3/13
 */
public class HttpClientUtil {

    private static final Integer RESPONSE_STATUS_OK = 200;

    /**
     * 处理https GET/POST请求
     *
     * @param requestUrl    ： 请求地址
     * @param requestMethod ： 请求方式（get/post）
     * @param outputStr     ： 参数
     * @return
     */
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        StringBuffer buffer = null;
        try {
            //创建SSLContext
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManager[] tm = {new MyX509TrustManager()};
            //初始化
            sslContext.init(null, tm, new java.security.SecureRandom());
            //获取SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(requestMethod);
            //设置当前实例使用的SSLSoctetFactory
            conn.setSSLSocketFactory(ssf);
            conn.connect();
            //往服务器端写内容
            if (null != outputStr) {
                OutputStream os = conn.getOutputStream();
                os.write(outputStr.getBytes("utf-8"));
                os.close();
            }

            //读取服务器端返回的内容
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            buffer = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
        StringBuffer buffer = null;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(requestMethod);
            conn.connect();
            //往服务器端写内容
            if (null != outputStr) {
                OutputStream os = conn.getOutputStream();
                os.write(outputStr.getBytes("utf-8"));
                os.close();
            }

            //读取服务器端返回的内容
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            buffer = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public static String httpsDoPost(String requestUrl, String outputStr) {
        return httpsRequest(requestUrl, "POST", outputStr);
    }

    public static String httpsDoGet(String requestUrl) {
        return httpsRequest(requestUrl, "GET", null);
    }

    public static String DoGet(String requestUrl) {
        if (requestUrl.toUpperCase().startsWith("HTTPS")) {
            return httpsRequest(requestUrl, "GET", null);
        } else {
            return httpRequest(requestUrl, "GET", null);
        }
    }

    public static CloseableHttpResponse doPostWithResponse(String url, String jsonstr, String charset) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().
                setSocketTimeout(5000).setConnectTimeout(5000).build();
        httpPost.setConfig(requestConfig);
        httpPost.addHeader("Content-Type", "application/json");

        StringEntity requestEntity = new StringEntity(jsonstr, charset);

        httpPost.setEntity(requestEntity);

        response = httpClient.execute(httpPost, new BasicHttpContext());

        return response;
    }

    public static String doPostReturnResult(CloseableHttpResponse response, String url, String charset) throws IOException {
        if (response.getStatusLine().getStatusCode() != RESPONSE_STATUS_OK) {
            Logger.logError("request url failed", "http code=" + response.getStatusLine().getStatusCode()
                    + ", url=" + url);

            return null;
        }

        HttpEntity entity = response.getEntity();
        if (entity == null) {

            return null;
        }

        String resultStr = EntityUtils.toString(entity, charset);

        // close io
        if (response != null) {
            try {
                response.close();
            } catch (IOException e) {
                Logger.logError("request url=" + url, e);
            }
        }

        return resultStr;
    }

    /**
     * post请求
     *
     * @param url：请求地址
     * @param jsonstr：请求参数
     * @param charset：编码格式
     * @return
     */
    @SuppressWarnings("resource")
    public static String doPost(String url, String jsonstr, String charset) {
        CloseableHttpResponse response = null;

        try {
            response = doPostWithResponse(url, jsonstr, charset);

           return doPostReturnResult(response, url, charset);
        } catch (IOException e) {
            Logger.logError("request url=" + url, e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    Logger.logError("request url=" + url, e);
                }
            }
        }

        return null;
    }

    public static String doPost(String url, String jsonStr) throws OApiException {
        return doPost(url, jsonStr, "utf-8");
    }

    public static <T> T dingTalkHttpPost(String url, Object data, Class<T> clazz) throws OApiException {
        String resp = dingTalkHttpPost(url, data);
        net.sf.json.JSONObject obj = net.sf.json.JSONObject.fromObject(resp);
        return (T) net.sf.json.JSONObject.toBean(obj, clazz);
    }

    public static <T> T dingTalkHttpPost(String url, Object data, Class<T> clazz, Map<String, Class> classMap) throws OApiException {
        String resp = dingTalkHttpPost(url, data);
        net.sf.json.JSONObject obj = net.sf.json.JSONObject.fromObject(resp);
        return (T) net.sf.json.JSONObject.toBean(obj, clazz, classMap);
    }

    public static String dingTalkHttpPost(String url, Object data) throws OApiException {
        String resultStr = doPost(url, JSON.toJSONString(data));
        if (StringUtils.isBlank(resultStr)) {
            throw new OApiException(OApiException.ERR_RESULT_RESOLUTION, "未通过"+url+"调取到响应参数");
        }

        JSONObject result = JSON.parseObject(resultStr);
        if (result.getInteger(ConstantBusiness.DING_TALK_ERRCODE_KEY) == 0) {
            result.remove(ConstantBusiness.DING_TALK_ERRCODE_KEY);
            result.remove(ConstantBusiness.DING_TALK_ERRMSG_KEY);

            return result.toString();
        }

        Integer errCode = result.getInteger(ConstantBusiness.DING_TALK_ERRCODE_KEY);
        String errMsg = result.getString(ConstantBusiness.DING_TALK_ERRMSG_KEY);
        throw new OApiException(errCode, errMsg);
    }

    public static <T> T dingTalkHttpGet(String url, Class<T> clazz) throws OApiException {
        String resp = dingTalkHttpGet(url);
        net.sf.json.JSONObject obj = net.sf.json.JSONObject.fromObject(resp);
        return (T) net.sf.json.JSONObject.toBean(obj, clazz);
    }

    public static String dingTalkHttpGet(String url) throws OApiException {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().
                setSocketTimeout(2000).setConnectTimeout(2000).build();
        httpGet.setConfig(requestConfig);

        try {
            response = httpClient.execute(httpGet, new BasicHttpContext());

            if (response.getStatusLine().getStatusCode() != 200) {

                System.out.println("request url failed, http code=" + response.getStatusLine().getStatusCode()
                        + ", url=" + url);
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String resultStr = EntityUtils.toString(entity, "utf-8");

                JSONObject result = JSON.parseObject(resultStr);
                if (result.getInteger("errcode") == 0) {
                    return result.toString();
                } else {
                    System.out.println("request url=" + url + ",return value=");
                    System.out.println(resultStr);
                    int errCode = result.getInteger("errcode");
                    String errMsg = result.getString("errmsg");
                    throw new OApiException(errCode, errMsg);
                }
            }
        } catch (IOException e) {
            System.out.println("request url=" + url + ", exception, msg=" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}