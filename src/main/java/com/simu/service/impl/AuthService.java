package com.simu.service.impl;

import com.simu.service.IAuthService;
import org.springframework.stereotype.Service;

/**
 * @author DengrongGuan
 * @create 2018-02-06 下午3:14
 **/
@Service
public class AuthService implements IAuthService{
    @Override
    public void verifyAuth(String path, String signature, String accessKeyId, String expires) {

    }
}
