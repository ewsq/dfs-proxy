package com.simu.config;

import com.simu.seaweedfs.core.FileSource;
import com.simu.seaweedfs.core.FileTemplate;
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

    @Bean("fileSource")
    public FileSource getFileSource(@Value("${weed.servers.urls}")String urls) throws Exception{
        FileSource fileSource = new FileSource();
        fileSource.setUrls(Arrays.asList(urls.split(",")));
        fileSource.setConnectionTimeout(1000);
        fileSource.startup();
        return fileSource;
    }

    @Bean("fileTemplate")
    public FileTemplate getFileTemplate(@Qualifier("fileSource") FileSource fileSource){
        return new FileTemplate(fileSource.getConnection());
    }
}
