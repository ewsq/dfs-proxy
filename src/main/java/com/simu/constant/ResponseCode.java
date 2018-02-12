package com.simu.constant;

/**
 * Created by zp on 2016/7/26.
 */
public class ResponseCode {

  public static final int Error = -1;
  public static final int OK = 0;
  public static final int BAD_REQUEST = 400;//参数错误
  public static final int UNAUTHORIZED = 401;//未登录
  public static final int FORBIDDEN = 403;//操作没有权限
  public static final int CATCH_EXCEPTION = 2500;//普通异常
  public static final int DATA_BASE_EXCEPTION = 2600;//数据库异常
  public static final int SEARCH_EXCEPTION = 2700;//搜索异常
  public static final int NP_EXCEPTION = 2501;//空指针引用异常
  public static final int CLASS_CAST_EXCEPTION = 2502;// 类型强制转换异常
  public static final int ILLEGAL_ARGUMENT_EXCEPTION = 2503;//传递非法参数异常
  public static final int ARITHMETIC_EXCEPTION = 2504;//算术运算异常
  public static final int ARRAY_STORE_EXCEPTION = 2505;// 向数组中存放与声明类型不兼容对象异常
  public static final int INDEX_OUT_OF_BOUNDS_EXCEPTION = 2506;// 下标越界异常
  public static final int SECURITY_EXCEPTION = 2507;// 安全异常
  public static final int UNSUPPORTED_OPERATION_EXCEPTION = 2508;// 不支持的操作异常

  // BUCKET
  public static final int BUCKET_NOT_EXIST = 3000;



}
