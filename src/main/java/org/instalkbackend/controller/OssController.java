package org.instalkbackend.controller;

import com.aliyuncs.auth.sts.AssumeRoleResponse;
import org.instalkbackend.model.vo.Result;
import org.instalkbackend.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oss")
public class OssController {

    @Autowired
    private OssService ossService;

    @GetMapping("/credentials")
    public Result<AssumeRoleResponse.Credentials> getCredentials(){ return ossService.getCredentials();}

    @GetMapping("/bucket")
    public Result<String> getBucket() {
        return ossService.getBucket();
    }

    @GetMapping("/region")
    public Result<String> getRegion() {
        return ossService.getRegion();
    }

    @GetMapping("/endpoint")
    public Result<String> getEndPoint() {
        return ossService.getEndPoint();
    }

}
