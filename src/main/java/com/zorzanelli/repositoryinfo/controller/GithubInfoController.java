package com.zorzanelli.repositoryinfo.controller;

import com.zorzanelli.repositoryinfo.entity.FileInfo;
import com.zorzanelli.repositoryinfo.service.GithubInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/info")
public class GithubInfoController {

    @Autowired
    GithubInfoService githubInfoService;

    @RequestMapping(method= RequestMethod.GET)
    public Map<String, FileInfo> getGithubRepositoryFilesSize(@RequestParam("url") String repositoryUrl){
        return githubInfoService.getGithubRepositoryFilesSize(repositoryUrl);
    }
}
