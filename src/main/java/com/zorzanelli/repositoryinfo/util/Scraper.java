package com.zorzanelli.repositoryinfo.util;

import com.zorzanelli.repositoryinfo.entity.FileInfo;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Scraper extends Constants{

    private final static Logger LOGGER = Logger.getLogger(Scraper.class.getName());

    private Map<String, FileInfo> repositoryInfo = new HashMap<>();

    public Map<String, FileInfo> getFilesSize(String url){

        if(!url.contains(GITHUB_BASE_URL)){
            url = GITHUB_BASE_URL + url;
        }

        String pageContent = getPageContent(url);
        List<String> urls = getItemsUrl(pageContent);

        for (String path: urls) {
            LOGGER.info(path);
            try{
                getFilesSize(path);
            }catch (Exception ex){
                LOGGER.info("Unknown page pattern: " + path);
            }
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
        while(pageContent.contains(URL_REFERENCE)){
            String url = getSubstringBasedOnReference(pageContent, URL_REFERENCE, "href=\"", "\">");
            urls.add(url);
            pageContent = pageContent.replaceFirst(URL_REFERENCE, EMPTY_STRING);
        }
        
        if(isFile(pageContent)){
            String extension = getFileExtension(pageContent);
            Double size = getFileSize(pageContent);
            Long totalLines = isBinaryFile(pageContent) ? 0L : getFileLineCount(pageContent);
            addRepositoryInfo(extension, size, totalLines);
            LOGGER.info(extension + ": " + size + " | " + totalLines);
        }

        return urls;
    }

    private void addRepositoryInfo(String extension, Double size, Long totalLines) {
        size = size + (this.repositoryInfo.get(extension) == null
                ? 0D : this.repositoryInfo.get(extension).getSize());

        totalLines = totalLines + (this.repositoryInfo.get(extension) == null
                ? 0L : this.repositoryInfo.get(extension).getTotalLines());

        FileInfo fileInfo = new FileInfo(totalLines, size);
        this.repositoryInfo.put(extension, fileInfo);
    }

    private Long getFileLineCount(String pageContent){
        String result = getSubstringBasedOnReference(pageContent, FILE_INFO_REFERENCE, "\"", "line");
        return Long.parseLong(result.replaceAll(REGEX_NON_DIGIT,EMPTY_STRING));
    }

    private Double getFileSize(String pageContent) {
        String result;
        if(isBinaryFile(pageContent)){
            result = getSubstringBasedOnReference(pageContent, FILE_INFO_REFERENCE, ">", "<");
        }else{
            result = getSubstringBasedOnReference(pageContent, FILE_INFO_REFERENCE, "</span>", "</div>");
        }
        return parseToBytes(result);
    }

    private String getSubstringBasedOnReference(String pageContent, String reference, String start, String end){
        int referenceIndex = pageContent.indexOf(reference);
        if(referenceIndex == -1)
            return EMPTY_STRING;

        int initNameIndex = pageContent.indexOf(start, referenceIndex);
        int endNameIndex = pageContent.indexOf(end, referenceIndex);
        return pageContent.substring(initNameIndex + start.length() , endNameIndex).trim();
    }

    private Double parseToBytes(String result) {
        String sizeSufix = result.substring(result.indexOf(WHITE_SPACE)).trim();
        Double value = Double.parseDouble(result.substring(0, result.indexOf(WHITE_SPACE)));

        switch (sizeSufix){
            case BYTES_SUFIX:
                return value;
            case KB_SUFIX:
                return value * 1024D;
            case MB_SUFIX:
                return value * 1048576D;
            default:
                return 0D;
        }
    }

    private String getFileExtension(String pageContent) {
        String fileName = getSubstringBasedOnReference(pageContent, FILE_NAME_REFERENCE, ">", "<");

        if(fileName.lastIndexOf(POINT) != -1 && fileName.lastIndexOf(POINT) != 0)
            return fileName.substring(fileName.lastIndexOf(POINT)+1);

        else return NO_EXTENSION;
    }

    private static boolean isFile(String pageContent) {
        return pageContent.contains(IS_FILE_REFERENCE);
    }

    private static boolean isBinaryFile(String pageContent){
        return !pageContent.contains(IS_BINARY_REFERENCE);
    }

}