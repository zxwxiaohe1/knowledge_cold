package com.cdhr.platform.comm.core.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author heyong
 * @date 2019/9/3
 */
public class StringUtils {

    public static String formatUrl(@NonNull String url) {
        URL target = null;
        try {
            target = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        StringBuffer sb = new StringBuffer().append(target.getProtocol())
                .append("://")
                .append(target.getHost());
        if (!"-1".equals(target.getPort())) {
            sb.append(":").append(target.getPort());
        }
        if (StringUtils.isNotBlank(target.getPath())) {
            sb.append(target.getPath().replaceAll("/+", "/"));
        }
        if (StringUtils.isNotBlank(target.getQuery())) {
            sb.append("?").append(target.getQuery());
        }
        return sb.toString();
    }

    public static String parse(String url, String name, String ticket) {

        if (StringUtils.isBlank(url)) {
            return "";
        }
        String[] urlParts = url.split("\\?");
        String baseUrl = urlParts[0];
        //没有参数
        if (urlParts.length == 1) {
            return baseUrl + "?" + name + "=" + ticket;
        }
        //有参数
        String[] params = urlParts[1].split("&");
        Boolean firtParam = true;
        for (String p : params) {
            if (StringUtils.isNotBlank(p)) {
                if (p.matches(name + "=.*")) {
                    continue;
                }
                if (firtParam) {
                    firtParam = false;
                    baseUrl = baseUrl + "?" + p;
                } else {
                    baseUrl = baseUrl + "&" + p;
                }
            }
        }
        if (firtParam) {
            baseUrl = baseUrl + "?" + name + "=" + ticket;
        } else {
            baseUrl = baseUrl + "&" + name + "=" + ticket;
        }
        return baseUrl;
    }
}
