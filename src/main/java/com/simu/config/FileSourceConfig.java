package com.simu.config;

import com.simu.seaweedfs.core.FileSource;
import com.simu.seaweedfs.core.FileTemplate;
import com.simu.seaweedfs.util.WarningSendUtil;
import com.simu.utils.WarningSendUtilImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * @author DengrongGuan
 * @create 2018-02-08
 **/
@Configuration
public class FileSourceConfig {

    @Bean("warningSendUtil")
    public WarningSendUtil getWarningSendUtil(@Value("${weed.subscriber.email}")String email, @Value("${weed.subscriber.phone}")String phone){
        return new WarningSendUtilImpl(email, phone);
    }

    @Bean("fileSource")
    public FileSource getFileSource(@Value("${weed.servers.urls}")String urls, @Qualifier("warningSendUtil")WarningSendUtil warningSendUtil) throws Exception{
        FileSource fileSource = new FileSource();
        fileSource.setUrls(Arrays.asList(urls.split(",")));
        fileSource.setConnectionTimeout(1000);
//        fileSource.setStatusExpiry(5);
        fileSource.setVolumeStatusCheckInterval(20 * 60); //volume节点状态每隔20分钟更新一次
        fileSource.setWarningSendUtil(warningSendUtil);
        fileSource.startup();
        return fileSource;
    }

    @Bean("fileTemplate")
    public FileTemplate getFileTemplate(@Qualifier("fileSource") FileSource fileSource){
        return new FileTemplate(fileSource.getConnection());
    }
}
