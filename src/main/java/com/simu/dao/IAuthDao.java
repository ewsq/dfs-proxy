package com.simu.dao;

import org.springframework.web.bind.annotation.RequestMethod;

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
    void validSignature(String resource, String accessId, long expires, String signature, RequestMethod verb) throws Exception;
}
