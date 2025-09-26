package ru.nsqk.web;

import java.util.List;

public class ResponseBuilder {

    public String build(List<Result> results, String error) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (error != null) {
            sb.append("\"error\":\"").append(escape(error)).append("\"");
        } else {
            sb.append("\"results\":[");
            for (int i = 0; i < results.size(); i++) {
                Result r = results.get(i);
                if (i > 0) sb.append(",");
                sb.append("{")
                        .append("\"x\":").append(r.x()).append(",")
                        .append("\"y\":").append(r.y()).append(",")
                        .append("\"r\":").append(r.r()).append(",")
                        .append("\"hit\":").append(r.hit()).append(",")
                        .append("\"clientTime\":\"").append(escape(r.clientTime())).append("\",")
                        .append("\"execTimeNs\":").append(r.execTimeNs())
                        .append("}");
            }
            sb.append("]");
        }
        sb.append("}");
        return sb.toString();
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("\"", "\\\"");
    }
}