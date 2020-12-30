package com.zorzanelli.repositoryinfo.service;

import com.zorzanelli.repositoryinfo.util.Scraper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GithubInfoService {

    public Map<String, Double> getGithubRepositoryFilesSize(String repositoryUrl){
        HashMap<String, Double> info = new HashMap<>();
        info.put(".java", (info.get(".java") == null ? 0D : info.get(".java")) + 4.3D);
        info.put(".xml", (info.get(".xml") == null ? 0D : info.get(".xml")) + 4.1D);
        info.put(".java", (info.get(".java") == null ? 0D : info.get(".java")) + 10.3D);
        info.put(".java", (info.get(".java") == null ? 0D : info.get(".java")) + 3.0D);
        info.put(".properties", (info.get(".properties") == null ? 0D : info.get(".properties")) + 2.3D);

        Scraper.getFilesSize(repositoryUrl);


        return info;
    }

}
