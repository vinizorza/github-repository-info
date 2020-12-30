package com.zorzanelli.repositoryinfo.util;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Scraper {

    private static final String CLASS_ITEM_LIST = "js-navigation-open link-gray-dark";

    private static final String GITHUB_BASE_URL = "https://github.com";

    public static Map<String, Double> getFilesSize(String url){

        if(!url.contains(GITHUB_BASE_URL)){
            url = GITHUB_BASE_URL + url;
        }

        String pageContent = getPageContent(url);
        List<String> urls = getItemsUrl(pageContent);

        for (String e: urls) {
            System.out.println(e);
            getFilesSize(e);
        }

        return null;
    }

    private static String getPageContent(String url){
        String text = null;
        try {
            InputStream in = new URL(url).openStream();
            text = new BufferedReader(
                    new InputStreamReader(in, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    private static List<String> getItemsUrl(String pageContent){
        List<String> urls = new ArrayList<>();
        while(pageContent.contains(CLASS_ITEM_LIST)){
            int classIndex = pageContent.indexOf(CLASS_ITEM_LIST);
            int initUrlIndex = pageContent.indexOf("/", classIndex);
            int endUrlIndex = pageContent.indexOf("\">", classIndex);
            String url = pageContent.substring(initUrlIndex, endUrlIndex);
            urls.add(url);
            pageContent = pageContent.replaceFirst(CLASS_ITEM_LIST, "");
        }
        
        if(isFile(pageContent)){
            //TODO: put informations size and extension in map to return
            System.out.println("TRUE");
        }
        return urls;
    }

    private static boolean isFile(String pageContent) {
        return pageContent.contains("file-info-divider");
    }

    private Map<String, Double> getFilesExtensionAndSizeFromPage(String pageContent){
        return null;
    }

}
