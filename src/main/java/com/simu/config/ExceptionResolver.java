package com.simu.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simu.constant.ResponseCode;
import com.simu.exception.ErrorCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@Configuration
public class ExceptionResolver extends SimpleMappingExceptionResolver {

  private static final Logger LOG = LoggerFactory.getLogger(ExceptionResolver.class);

  private ObjectMapper objectMapper;

  public ExceptionResolver() {
    objectMapper = new ObjectMapper();
  }


  @Override
  public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                       Object handler, Exception ex) {
    response.setStatus(200);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    response.setHeader("Cache-Control", "no-cache, must-revalidate");
    Map<String, Object> map = new HashMap<>();
    if (ex instanceof NullPointerException) {
      map.put("code", ResponseCode.NP_EXCEPTION);
    } else if (ex instanceof ClassCastException) {
      map.put("code", ResponseCode.CLASS_CAST_EXCEPTION);
    } else if (ex instanceof IllegalArgumentException) {
      map.put("code", ResponseCode.ILLEGAL_ARGUMENT_EXCEPTION);
    } else if (ex instanceof ArrayStoreException) {
      map.put("code", ResponseCode.ARRAY_STORE_EXCEPTION);
    } else if (ex instanceof IndexOutOfBoundsException) {
      map.put("code", ResponseCode.INDEX_OUT_OF_BOUNDS_EXCEPTION);
    } else if (ex instanceof SecurityException) {
      map.put("code", ResponseCode.SECURITY_EXCEPTION);
    } else if (ex instanceof UnsupportedOperationException) {
      map.put("code", ResponseCode.UNSUPPORTED_OPERATION_EXCEPTION);
    } else if(ex instanceof ErrorCodeException){
      map.put("code", ((ErrorCodeException) ex).getCode());
    } else {
      map.put("code", ResponseCode.CATCH_EXCEPTION);
    }

    String url = request.getRequestURL().toString();
    try {
      if(url.contains("inner")){
        response.setStatus(500);
        response.setHeader("error",ex.getMessage() );
      }else{
        map.put("data", ex.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(map));
      }
    } catch (Exception e) {
      // ignorex
    }
    LOG.error(url, ex);
    return new ModelAndView();
  }
}
