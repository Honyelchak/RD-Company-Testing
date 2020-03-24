package com.rd.test.crawler.request;

import com.rd.test.crawler.entity.IssuanceAudit;
import com.rd.test.crawler.parse.ParseHTML;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.html.Option;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.GZIPInputStream;


/**
 * @author Honyelchak
 * @create 2020-03-24 10:35
 */
public class Request {




    public static String sendRequest(String url, Map<String, String> headersMap) {

        RestTemplate restTemplate = new RestTemplate();

        //设置请求header 为 APPLICATION_FORM_URLENCODED
        HttpHeaders headers = new HttpHeaders();

        headers.setAll(headersMap);
        // 请求体，包括请求数据 body 和 请求头 headers
        HttpEntity httpEntity = new HttpEntity(headers);

        try {
            //使用 exchange 发送请求，以String的类型接收返回的数据
            //ps，我请求的数据，其返回是一个json

            ResponseEntity<String> strbody = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
            System.out.println("状态码：" + strbody.getStatusCode());
            // 判断是否为空
            String result = Optional.ofNullable(strbody.getBody()).orElse("null");
            // gzip对字符串进行解压
            result = ParseHTML.uncompressString(result);
            // 解析返回的数据
            return result;
        } catch (Exception e) {
            System.out.println(e);
        }
        return "null";
    }

}
