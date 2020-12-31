package com.zorzanelli.repositoryinfo.util;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Scraper {

    private static final String CLASS_ITEM_LIST = "js-navigation-open link-gray-dark";

    private static final String FILE_NAME_REFERENCE = "final-path";

    private static final String FILE_SIZE_REFERENCE = "file-info-divider";

    private static final String GITHUB_BASE_URL = "https://github.com";

    private Map<String, Double> repositoryInfo = new HashMap<>();

    public Map<String, Double> getFilesSize(String url){

        if(!url.contains(GITHUB_BASE_URL)){
            url = GITHUB_BASE_URL + url;
        }

        String pageContent = getPageContent(url);
        List<String> urls = getItemsUrl(pageContent);

        for (String e: urls) {
            System.out.println(e);
            getFilesSize(e);
        }

        return repositoryInfo;
    }

    private String getPageContent(String url){
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

    private List<String> getItemsUrl(String pageContent){
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
            String extension = getFileExtension(pageContent);
            Double size = getFileSize(pageContent);
            addRepositoryInfo(extension, size);
            System.out.println(extension + " -> " + size);
        }

        return urls;
    }

    private void addRepositoryInfo(String extension, Double size) {
        this.repositoryInfo.put(extension, size + (this.repositoryInfo.get(extension) == null ? 0D : this.repositoryInfo.get(extension)));
    }

    private Double getFileSize(String pageContent) {
        int referenceIndex = pageContent.indexOf(FILE_SIZE_REFERENCE);
        int initNameIndex = pageContent.indexOf("</span>", referenceIndex);
        int endNameIndex = pageContent.indexOf("</div>", referenceIndex);
        String result = pageContent.substring(initNameIndex + "</span>".length() , endNameIndex).trim();
        return parseToBytes(result);
    }

    private Double parseToBytes(String result) {
        String sizeSufix = result.substring(result.indexOf(" ")).trim();
        Double value = Double.parseDouble(result.substring(0, result.indexOf(" ")));

        switch (sizeSufix){
            case "Bytes":
                return value;
            case "KB":
                return value * 1024D;
            case "MB":
                return value * 1048576D;
            default:
                return 0D;
        }
    }

    private String getFileExtension(String pageContent) {
        int referenceIndex = pageContent.indexOf(FILE_NAME_REFERENCE);
        int initNameIndex = pageContent.indexOf(">", referenceIndex);
        int endNameIndex = pageContent.indexOf("<", referenceIndex);
        String fileName = pageContent.substring(initNameIndex + ">".length(), endNameIndex);

        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);

        else return "NO_EXTENSION";
    }

    private static boolean isFile(String pageContent) {
        return pageContent.contains("file-info-divider");
    }

}
