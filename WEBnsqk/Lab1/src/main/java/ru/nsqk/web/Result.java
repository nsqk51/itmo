package ru.nsqk.web;

public record Result(
        double x,
        double y,
        double r,
        boolean hit,
        String clientTime,
        long execTimeNs
) {}