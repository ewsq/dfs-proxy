package com.simu.utils;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author DengrongGuan
 * @create 2018-02-11
 **/
public class TimeUtil {
    public static Timestamp getCurrentSqlTime() {
        return new Timestamp(new Date().getTime());
    }
}
