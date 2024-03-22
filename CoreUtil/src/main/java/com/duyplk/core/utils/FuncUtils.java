package com.duyplk.core.utils;

import io.swagger.v3.oas.models.servers.Server;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FuncUtils {

    public static List<Server> servers(String url) {
        return Collections.singletonList(new Server().url(url.toLowerCase()));
    }

    public static List<Server> servers(List<String> urls) {
        return urls.stream().map(url -> new Server().url(url.toLowerCase())).collect(Collectors.toList());
    }
}
