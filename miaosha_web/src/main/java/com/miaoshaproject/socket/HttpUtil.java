package com.miaoshaproject.socket;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;


/**
 * 工具类，http链接池
 * Created by 谢伟刚 on 2018/05/07.
 */
public class HttpUtil {
    public static int maxTotal = 200; // 最大连接数
    public static int defaultMaxPerRoute = 100; // 每个路由的最大连接数
    public static int connectTimeout = 20000; // 连接超时时间
    public static int socketTimeout = 20000; // 读取超时时间
    public static int requestTimeout = 5000; // 从池中获取连接超时时间
    public static CloseableHttpClient httpclient = null;

    /**
     * 初始化client对象
     */
    public static void init() {
        if (httpclient != null) {
            return;
        }
        // 创建连接管理器
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager();
        // 设置最大连接数
        connMgr.setMaxTotal(maxTotal);
        // 设置每个路由的最大连接数
        connMgr.setDefaultMaxPerRoute(defaultMaxPerRoute);
        // 创建httpClient
        httpclient = HttpClients.custom().setConnectionManager(connMgr).build();
    }

    /**
     * 关闭连接池
     */
    public static void close() {
        if (httpclient != null) {
            try {
                httpclient.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * Get方式取得URL的内容
     */
    public static String getUrlContent(String url) {
        // 参数检查
        if (httpclient == null) {
            throw new RuntimeException("httpclient not init.");
        }
        if (url == null || url.trim().length() == 0) {
            throw new RuntimeException("url is blank.");
        }
        HttpGet httpGet = new HttpGet(url);
        // 设置内容
        httpGet.addHeader("accept", "*/*");
        httpGet.addHeader("connection", "Keep-Alive");
        httpGet.addHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpclient.execute(httpGet, HttpClientContext.create());
            // 转换结果
            HttpEntity entity = response.getEntity();
            String html = EntityUtils.toString(entity);
            // 消费掉内容
            EntityUtils.consume(entity);
            return html;
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            httpGet.releaseConnection();
        }
        return "";
    }

    /**
     * Post方式取得URL的内容，默认为"application/x-www-form-urlencoded"格式，charset为UTF-8
     */
    public static String postToUrl(String url, String content) {
        return postToUrl(url, content, "application/x-www-form-urlencoded", "UTF-8");
    }

    /**
     * json字符串形式请求
     */
    public static String postForJson(String url, String content) {
        return postToUrl(url, content, "application/json", "UTF-8");
    }

    /**
     * Post方式取得URL的内容
     */
    public static String postToUrl(String url, String content, String contentType, String charset) {
        // 参数检查
        if (httpclient == null) {
            throw new RuntimeException("httpclient not init.");
        }
        if (url == null || url.trim().length() == 0) {
            throw new RuntimeException("url is blank.");
        }
        HttpPost httpPost = new HttpPost(url);
        // 设置内容
        ContentType type = ContentType.create(contentType, Charset.forName(charset));
        StringEntity reqEntity = new StringEntity(content, type);
        httpPost.setEntity(reqEntity);

        httpPost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE .0; Windows NT 6.1; Trident/4.0; SLCC2;)");
        httpPost.addHeader("Accept-Encoding", "*");
        // 设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectTimeout) // 连接超时时间
                .setSocketTimeout(socketTimeout) // 读取超时时间（等待数据超时时间）
                .setConnectionRequestTimeout(requestTimeout) // 从池中获取连接超时时间
                .build();
        httpPost.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpclient.execute(httpPost, HttpClientContext.create());
            // 转换结果
            HttpEntity entity = response.getEntity();
            String html = EntityUtils.toString(entity, charset);
            // 消费掉内容
            EntityUtils.consume(entity);
            return html;
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            httpPost.releaseConnection();
        }
        return "";
    }

    /**
     * json字符串形式请求
     */
    public static String putForJson(String url, String content) {
        return putToUrl(url, content, "application/json", "UTF-8");
    }

    /**
     * Put方式取得URL的内容
     */
    public static String putToUrl(String url, String content, String contentType, String charset) {
        // 参数检查
        if (httpclient == null) {
            throw new RuntimeException("httpclient not init.");
        }
        if (url == null || url.trim().length() == 0) {
            throw new RuntimeException("url is blank.");
        }
        HttpPut httpPut = new HttpPut(url);
        // 设置内容
        ContentType type = ContentType.create(contentType, Charset.forName(charset));
        StringEntity reqEntity = new StringEntity(content, type);
        httpPut.setEntity(reqEntity);

        httpPut.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE .0; Windows NT 6.1; Trident/4.0; SLCC2;)");
        httpPut.addHeader("Accept-Encoding", "*");
        // 设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectTimeout) // 连接超时时间
                .setSocketTimeout(socketTimeout) // 读取超时时间（等待数据超时时间）
                .setConnectionRequestTimeout(requestTimeout) // 从池中获取连接超时时间
                .build();
        httpPut.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpclient.execute(httpPut, HttpClientContext.create());
            // 转换结果
            HttpEntity entity = response.getEntity();
            String html = EntityUtils.toString(entity, charset);
            // 消费掉内容
            EntityUtils.consume(entity);
            return html;
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            httpPut.releaseConnection();
        }
        return "";
    }

    /**
     * Post方式取得URL的内容
     */
    public static String apiPost(String url, String content) {
        // 参数检查
        if (httpclient == null) {
            throw new RuntimeException("httpclient not init.");
        }
        if (url == null || url.trim().length() == 0) {
            throw new RuntimeException("url is blank.");
        }

        HttpPost httpPost = new HttpPost(url);
        ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
        pairs.add(new BasicNameValuePair("requestData", content));
        if (pairs != null) {
            try {
                UrlEncodedFormEntity formEntity;
                formEntity = new UrlEncodedFormEntity(pairs, "utf-8");
                httpPost.setEntity(formEntity);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        httpPost.addHeader("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
        // 设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectTimeout) // 连接超时时间
                .setSocketTimeout(socketTimeout) // 读取超时时间（等待数据超时时间）
                .setConnectionRequestTimeout(requestTimeout) // 从池中获取连接超时时间
                .build();
        httpPost.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpclient.execute(httpPost, HttpClientContext.create());
            // 转换结果
            HttpEntity entity = response.getEntity();
            String html = EntityUtils.toString(entity, "utf-8");
            // 消费掉内容
            EntityUtils.consume(entity);
            return html;
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            httpPost.releaseConnection();
        }
        return "";
    }

    /**
     * 工具类测试
     */
    public static void main(String[] args) {
        HttpUtil.init();
        String url = "https://bp.winning-health.com.cn:15574/winbx/p?action=GETMZHZZJH&hzxm=测试001&zjh=410724199202085533&yydm=42030005-01&accesskey=279F1BE055B321804C4746C1B9E7A8C39C635E62CC246607176645DF61EA45DD6AD131CAB48306CE17085664578C46DFD1DDB50F2359EF4D";
        String resString = HttpUtil.getUrlContent(url);
        System.out.println(resString);
        HttpUtil.close();
    }
}