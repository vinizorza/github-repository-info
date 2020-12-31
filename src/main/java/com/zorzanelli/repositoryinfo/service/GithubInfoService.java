package com.zorzanelli.repositoryinfo.service;

import com.zorzanelli.repositoryinfo.util.Scraper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GithubInfoService {

    public Map<String, Double> getGithubRepositoryFilesSize(String repositoryUrl){

        Scraper scraper = new Scraper();
        scraper.getFilesSize(repositoryUrl);

        return scraper.getFilesSize(repositoryUrl);
    }

}
