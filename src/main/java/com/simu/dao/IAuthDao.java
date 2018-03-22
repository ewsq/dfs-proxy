package com.simu.dao;

import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author DengrongGuan
 * @create 2018-02-09
 **/
public interface IAuthDao {

    /**
     *
     * @param resource /bucket/path
     * @param accessId
     * @param expires long seconds
     * @param signature
     * @param verb
     * @throws Exception
     */
    void validSignature(String resource, String accessId, Long expires, String signature, RequestMethod verb) throws RuntimeException;

    /**
     * 批量下载接口认证
     * @param resources
     * @param accessId
     * @param expires
     * @param signature
     * @param verb
     * @throws Exception
     */
    void validSignature(List<String> resources, String accessId, Long expires, String signature, RequestMethod verb) throws RuntimeException;
}
