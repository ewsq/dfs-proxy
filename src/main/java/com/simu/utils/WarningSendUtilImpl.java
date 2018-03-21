package com.simu.utils;

import com.simu.seaweedfs.util.WarningSendUtil;

/**
 * @author DengrongGuan
 * @create 2018-03-20
 **/
public class WarningSendUtilImpl implements WarningSendUtil{
    private String email;
    private String phone;

    public WarningSendUtilImpl() {
    }

    public WarningSendUtilImpl(String email, String phone) {
        this.email = email;
        this.phone = phone;
    }

    @Override
    public void sendEmail(String content) {

    }

    @Override
    public void sendSMS(String content) {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
