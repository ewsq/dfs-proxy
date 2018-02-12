package com.simu.service;


/**
 * @author DengrongGuan
 * @create 2018-02-06 下午3:14
 **/
public interface IAuthService {
    /**
     * 验证签名
     * @param path
     * @param signature
     * @param accessKeyId
     * @param expires
     */
    void verifyAuth(String path, String signature, String accessKeyId, String expires);
}
