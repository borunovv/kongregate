package com.borunovv.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author borunovv
 */
public final class StringUtils {

    public static String toUtf8String(byte[] data) {
        return toUtf8String(data, 0, data.length);
    }

    public static String toUtf8String(byte[] data, int offset, int length) {
        try {
            return new String(data, offset, length, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to convert bytes to utf-8", e);
        }
    }

    public static byte[] uft8StringToBytes(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Can't convert utf8 string to bytes", e);
        }
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String ensureString(String source) {
        return source != null ?
                source :
                "";
    }

    public static String urlEncode(String string) {
        String res = string;
        try {
            res = URLEncoder.encode(ensureString(string), "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return res;
    }

    public static String urlDecode(String string) {
        String res = string;
        try {
            res = URLDecoder.decode(ensureString(string), "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return res;
    }
}
