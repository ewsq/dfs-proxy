package com.simu.dao.impl;

import com.aliyun.oss.common.auth.ServiceSignature;
import com.simu.constant.ResponseCodeEnum;
import com.simu.dao.IAuthDao;
import com.simu.exception.ErrorCodeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.List;

/**
 * @author DengrongGuan
 * @create 2018-02-09
 **/
@Repository
public class AuthDao implements IAuthDao{

    @Value("${weed.auth.accessId}")
    private String accessId;

    @Value("${weed.auth.accessKey}")
    private String accessKey;

    @Override
    public void validSignature(String resource, String accessId, Long expires, String signature, RequestMethod verb) throws RuntimeException{
        // expire test
        if (null == expires || new Date().getTime() > expires * 1000){
            throw new ErrorCodeException(ResponseCodeEnum.AUTH_SIGNATURE_EXPIRED);
        }
        if (!accessId.equals(this.accessId)){
            throw new ErrorCodeException(ResponseCodeEnum.AUTH_ACCESS_ID_NOT_MATCH);
        }
        StringBuilder canonicalString = new StringBuilder();
        canonicalString.append(verb.name()).append("\n");
        canonicalString.append(expires).append("\n");
        canonicalString.append(resource);
        String signatureTmp = ServiceSignature.create()
                .computeSignature(accessKey, canonicalString.toString());
        if (!signatureTmp.equals(signature)){
            throw new ErrorCodeException(ResponseCodeEnum.AUTH_SIGNATURE_NOT_MATCH);
        }

    }

    @Override
    public void validSignature(List<String> resources, String accessId, Long expires, String signature, RequestMethod verb) throws RuntimeException {
        // expire test
        if (null == expires || new Date().getTime() > expires * 1000){
            throw new ErrorCodeException(ResponseCodeEnum.AUTH_SIGNATURE_EXPIRED);
        }
        if (!accessId.equals(this.accessId)){
            throw new ErrorCodeException(ResponseCodeEnum.AUTH_ACCESS_ID_NOT_MATCH);
        }
        StringBuilder canonicalString = new StringBuilder();
        canonicalString.append(verb.name()).append("\n");
        canonicalString.append(expires).append("\n");
        resources.stream().forEach(r -> canonicalString.append(r).append("&"));
        canonicalString.replace(canonicalString.length()-1, canonicalString.length(),"");
        String signatureTmp = ServiceSignature.create()
                .computeSignature(accessKey, canonicalString.toString());
        if (!signatureTmp.equals(signature)){
            throw new ErrorCodeException(ResponseCodeEnum.AUTH_SIGNATURE_NOT_MATCH);
        }
    }
}
