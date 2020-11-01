package com.proxy.load_balancer;

import java.util.Collections;
import java.util.List;

public abstract class LoadBalancer {
    final List <String> urlList;

    public LoadBalancer(List<String> urlList) {
        this.urlList = Collections.unmodifiableList(urlList);
    }

    abstract String getUrl();
}
