package com.zorzanelli.repositoryinfo.service;

import com.zorzanelli.repositoryinfo.entity.FileInfo;
import com.zorzanelli.repositoryinfo.util.Scraper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GithubInfoService {

    @Cacheable(cacheNames = "RepositoryInfo")
    public Map<String, FileInfo> getGithubRepositoryFilesSize(String repositoryUrl){
        Scraper scraper = new Scraper();
        return scraper.getFilesInfo(repositoryUrl);
    }

}
