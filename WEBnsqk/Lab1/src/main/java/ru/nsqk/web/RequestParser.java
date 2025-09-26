package ru.nsqk.web;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Парсер GET/POST параметров.
 * Поддержка повторяющихся ключей (checkbox r=...&r=...).
 */
public class RequestParser {

    public Map<String, List<String>> parse(Properties props, InputStream in) {
        Map<String, List<String>> params = new LinkedHashMap<>();

        String query = props.getProperty("QUERY_STRING", "");
        parseQuery(query, params);

        int contentLength = 0;
        try {
            contentLength = Integer.parseInt(props.getProperty("CONTENT_LENGTH", "0"));
        } catch (Exception ignored) {}

        if (contentLength > 0) { // На всякий случай (хотя ТЗ требует GET)
            try {
                byte[] buf = new byte[contentLength];
                int off = 0;
                while (off < contentLength) {
                    int read = in.read(buf, off, contentLength - off);
                    if (read < 0) break;
                    off += read;
                }
                String body = new String(buf, 0, off, StandardCharsets.UTF_8);
                parseQuery(body, params);
            } catch (Exception ignored) {}
        }

        return params;
    }

    private void parseQuery(String query, Map<String, List<String>> params) {
        if (query == null || query.isEmpty()) return;
        String[] pairs = query.split("&");
        for (String raw : pairs) {
            if (raw.isEmpty()) continue;
            String[] kv = raw.split("=", 2);
            if (kv.length != 2) continue;
            String key = decode(kv[0]);
            String val = decode(kv[1]);
            params.computeIfAbsent(key, k -> new ArrayList<>()).add(val);
        }
    }

    private String decode(String s) {
        return java.net.URLDecoder.decode(s, StandardCharsets.UTF_8);
    }

    // Для отладки (опционально)
    @Override
    public String toString() {
        return super.toString();
    }
}