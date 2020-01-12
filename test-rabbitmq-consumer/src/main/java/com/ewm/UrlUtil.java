package com.ewm;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 微信二维码生成
 * @author cq
 */
public class UrlUtil {

    /**
     * (一) 获取 accessToken
     * @param request
     * @param response
     * @return 返回的是JSON类型 ; 获取accessToken:get("access_token");
     */
    public static JSONObject getAccessToken(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = null;

        try {
            /**
             * 获取小程序全局唯一后台接口调用凭据（access_token）
             * GET https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
             */

            URL url = new URL("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxbf2c92da621a22cb&secret=9b6a51d20e6f1770e92c87347975e9ed");
            HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
            urlcon.connect(); // 获取连接
            InputStream is = urlcon.getInputStream();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
            StringBuffer bs = new StringBuffer();
            String str = null;
            while ((str = buffer.readLine()) != null) {
                bs.append(str);
            }
            json = (JSONObject) JSONObject.parse(bs.toString());
            System.out.println("------------" + json);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }

    public static RestTemplate getInstanceByCharset(String charset) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName(charset)));
        return restTemplate;
    }
    /**
     * (二) 二维码生成：通过该接口生成的小程序码，永久有效，数量暂无限制
     * @param sceneStr 参数
     * @param accessToken  密匙
     * @return
     */
    public static Map<String, Object> getminiqrQrTwo(String sceneStr, String accessToken) {

        RestTemplate rest = new RestTemplate();
        rest.getMessageConverters().add(new StringHttpMessageConverter());
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;
            Map<String, Object> param = new HashMap<String,Object>();
            param.put("scene", "a1wJgLz0Dcg,sw_001");// 输入参数 最大32字符
            param.put("page", "pages/index/index");// 路径 如果没有默认跳转到首页面微信小程序发布后才可以使用不能添加参数
            param.put("width", "430");// 二维码尺寸
            param.put("is_hyaline", true); // 是否需要透明底色， is_hyaline 为true时，生成透明底色的小程序码 参数仅对小程序码生效
            param.put("auto_color", false); // 自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调 参数仅对小程序码生效
            // 颜色 auto_color 为 false 时生效，使用 rgb 设置颜色 例如 {"r":"xxx","g":"xxx","b":"xxx"}
            // 十进制表示
            Map<String, Object> line_color = new HashMap<String,Object>();
            line_color.put("r", 0);
            line_color.put("g", 0);
            line_color.put("b", 0);
            param.put("line_color", line_color);
            System.out.println("调用生成微信URL接口传参:" + param);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<String,String>();
            // 头部信息
            List<String> list = new ArrayList<String>();
            list.add("Content-Type");
            list.add("application/json");
            headers.put("header", list);
            HttpEntity<Object> requestEntity = new HttpEntity<Object>(param, headers);
            ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class,new Object[0]);
            System.out.println("调用小程序生成微信永久小程序码URL接口返回结果:" + entity.getBody());
            byte[] result = entity.getBody();
            System.out.println(Base64.encodeBase64String(result));
            inputStream = new ByteArrayInputStream(result);

            File file = new File("D:\1.png");// 这里返回的是生成的二维码
            if (!file.exists()) {
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();
        } catch (Exception e) {
            System.out.println("调用小程序生成微信永久小程序码URL接口异常 \r\n" + e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    // 测试代码
    public static void getErWeiMa(String access_token) {
        try {
            // URL url = new
            // URL("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+access_token);
            URL url = new URL("https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode?access_token=" + access_token);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");// 提交模式
            // conn.setConnectTimeout(10000);//连接超时 单位毫秒
            // conn.setReadTimeout(2000);//读取超时 单位毫秒
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            // 发送请求参数
            JSONObject paramJson = new JSONObject();
            paramJson.put("scene", "a1wJgLz0Dcg,sw_001");
            paramJson.put("page", "pages/index/index");
            paramJson.put("width", "430");
            paramJson.put("auto_color", true);

            // line_color生效
            paramJson.put("auto_color", false);
            JSONObject lineColor = new JSONObject();
            lineColor.put("r", 054);
            lineColor.put("g", 037);
            lineColor.put("b", 159);
            paramJson.put("line_color", lineColor);
            printWriter.write(paramJson.toString());
            // flush输出流的缓冲
            printWriter.flush();
            // 开始获取数据
            BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
            OutputStream os = new FileOutputStream(new File("D:\\erweima3.txt"));
            int len;
            byte[] arr = new byte[1024];
            while ((len = bis.read(arr)) != -1) {
                os.write(arr, 0, len);
                os.flush();
            }
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        //getAccessToken(request, response);
        getminiqrQrTwo("123456", getAccessToken(request, response).get("access_token").toString());
        //getErWeiMa(getAccessToken(request, response).get("access_token").toString());
        // getminiqrQrOne(getAccessToken(request,
        // response).get("access_token").toString());
    }
}
