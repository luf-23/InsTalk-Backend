package org.instalkbackend.service;

import com.aliyuncs.auth.sts.AssumeRoleResponse;
import org.instalkbackend.model.vo.Result;

public interface OssService {
    Result<AssumeRoleResponse.Credentials> getCredentials();

    Result<String> getBucket();

    Result<String> getRegion();

    Result<String> getEndPoint();
}
