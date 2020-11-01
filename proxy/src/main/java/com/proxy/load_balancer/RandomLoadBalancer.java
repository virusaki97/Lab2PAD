package com.proxy.load_balancer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RandomLoadBalancer extends LoadBalancer {
    public RandomLoadBalancer(List<String> urlList) {
        super(urlList);
    }

    @Override
    public String getUrl() {
        Random random = new Random();
        return urlList.get(random.nextInt(urlList.size()));
    }

    public ArrayList<String> getUrlWithout(String url) {
        Iterator iterator = urlList.iterator();
        ArrayList list = new ArrayList<String>();

        while(iterator.hasNext()) {
            String currUrl = (String) iterator.next();
            if(!currUrl.equals(url)) {
                list.add(currUrl);
            }
        }

        return list;
    }
}
