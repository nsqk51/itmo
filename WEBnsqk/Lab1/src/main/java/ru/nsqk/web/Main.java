package ru.nsqk.web;

import com.fastcgi.FCGIInterface;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        // Подхват порта из ENV для удобства (дополнительно к -DFCGI_PORT)
        String envPort = System.getenv("FCGI_PORT");
        if (envPort != null && !envPort.isEmpty()) {
            System.setProperty("FCGI_PORT", envPort);
        }

        FCGIInterface fcgi = new FCGIInterface();
        RequestParser parser = new RequestParser();
        ResponseBuilder responseBuilder = new ResponseBuilder();

        while (fcgi.FCGIaccept() >= 0) {
            Properties props = System.getProperties();
            InputStream in = System.in;
            PrintStream out = System.out;

            long startNs = System.nanoTime();

            // Парсим параметры (GET + тело, хотя по ТЗ только GET)
            Map<String, List<String>> params = parser.parse(props, in);

            String xStr = first(params.get("x"));
            String yStr = first(params.get("y"));
            List<String> rList = params.getOrDefault("r", Collections.emptyList());
            String clientTime = first(params.get("clientTime"));

            String error = Validator.validate(xStr, yStr, rList);
            List<Result> results = new ArrayList<>();

            if (error == null) {
                double x = Validator.parseXToDouble(xStr);
                double y = Double.parseDouble(yStr.replace(',', '.'));

                for (String rStr : rList) {
                    double r = Double.parseDouble(rStr);
                    boolean hit = HitChecker.check(x, y, r);
                    long execTimeNs = System.nanoTime() - startNs;
                    results.add(new Result(x, y, r, hit, clientTime == null ? "" : clientTime, execTimeNs));
                }
            }

            out.print("Content-Type: application/json; charset=UTF-8\r\n\r\n");
            out.print(responseBuilder.build(results, error));
            out.flush();
        }
    }

    private static String first(List<String> list) {
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }
}