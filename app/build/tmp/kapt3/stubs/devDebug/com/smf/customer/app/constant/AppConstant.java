package com.smf.customer.app.constant;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\"\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00020\u0006X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\u00020\u0006X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\bR\u0014\u0010\f\u001a\u00020\u0006X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\bR\u0014\u0010\u000e\u001a\u00020\u0006X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\bR\u0014\u0010\u0010\u001a\u00020\u0006X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\bR\u0014\u0010\u0012\u001a\u00020\u0006X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\bR\u0014\u0010\u0014\u001a\u00020\u0006X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\bR\u0014\u0010\u0016\u001a\u00020\u0006X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\bR\u0014\u0010\u0018\u001a\u00020\u0006X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\bR\u0014\u0010\u001a\u001a\u00020\u0006X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\bR\u0014\u0010\u001c\u001a\u00020\u0006X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\bR\u0014\u0010\u001e\u001a\u00020\u0006X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\bR\u0014\u0010 \u001a\u00020\u0006X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\bR\u0014\u0010\"\u001a\u00020\u0006X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\bR\u0014\u0010$\u001a\u00020\u0006X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\bR\u0014\u0010&\u001a\u00020\u0006X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\b\u00a8\u0006("}, d2 = {"Lcom/smf/customer/app/constant/AppConstant;", "", "()V", "AUTHORIZATION", "", "BAD_REQUEST", "", "getBAD_REQUEST", "()I", "EXTRA", "FORBIDDEN", "getFORBIDDEN", "INTERNAL_SERVER_ERROR_CODE", "getINTERNAL_SERVER_ERROR_CODE", "INVALID_OTP", "getINVALID_OTP", "NOT_FOUND", "getNOT_FOUND", "OTP_ALREADY_SENT_CODE", "getOTP_ALREADY_SENT_CODE", "OTP_EXPIRED", "getOTP_EXPIRED", "OTP_MATCH_CODE", "getOTP_MATCH_CODE", "OTP_NOT_SENT_CODE", "getOTP_NOT_SENT_CODE", "OTP_SENT_CODE", "getOTP_SENT_CODE", "SERVICE_UNAVAILABLE", "getSERVICE_UNAVAILABLE", "SERVICE_UNAVAILABLE_2", "getSERVICE_UNAVAILABLE_2", "SUCCESS_CODE", "getSUCCESS_CODE", "TIMEOUT_EXCEPTION", "getTIMEOUT_EXCEPTION", "TOO_MANY_REQUEST_CODE", "getTOO_MANY_REQUEST_CODE", "UNAUTHORISED", "getUNAUTHORISED", "app_devDebug"})
public final class AppConstant {
    @org.jetbrains.annotations.NotNull()
    public static final com.smf.customer.app.constant.AppConstant INSTANCE = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String AUTHORIZATION = "authorization";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA = "EXTRA";
    private static final int SUCCESS_CODE = 200;
    private static final int FORBIDDEN = 403;
    private static final int BAD_REQUEST = 400;
    private static final int NOT_FOUND = 404;
    private static final int UNAUTHORISED = 401;
    private static final int TIMEOUT_EXCEPTION = 440;
    private static final int TOO_MANY_REQUEST_CODE = 429;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int SERVICE_UNAVAILABLE_2 = 502;
    private static final int INTERNAL_SERVER_ERROR_CODE = 500;
    private static final int OTP_SENT_CODE = 20022;
    private static final int OTP_NOT_SENT_CODE = 20023;
    private static final int OTP_ALREADY_SENT_CODE = 20024;
    private static final int OTP_EXPIRED = 20025;
    private static final int INVALID_OTP = 20026;
    private static final int OTP_MATCH_CODE = 20027;
    
    private AppConstant() {
        super();
    }
    
    public final int getSUCCESS_CODE() {
        return 0;
    }
    
    public final int getFORBIDDEN() {
        return 0;
    }
    
    public final int getBAD_REQUEST() {
        return 0;
    }
    
    public final int getNOT_FOUND() {
        return 0;
    }
    
    public final int getUNAUTHORISED() {
        return 0;
    }
    
    public final int getTIMEOUT_EXCEPTION() {
        return 0;
    }
    
    public final int getTOO_MANY_REQUEST_CODE() {
        return 0;
    }
    
    public final int getSERVICE_UNAVAILABLE() {
        return 0;
    }
    
    public final int getSERVICE_UNAVAILABLE_2() {
        return 0;
    }
    
    public final int getINTERNAL_SERVER_ERROR_CODE() {
        return 0;
    }
    
    public final int getOTP_SENT_CODE() {
        return 0;
    }
    
    public final int getOTP_NOT_SENT_CODE() {
        return 0;
    }
    
    public final int getOTP_ALREADY_SENT_CODE() {
        return 0;
    }
    
    public final int getOTP_EXPIRED() {
        return 0;
    }
    
    public final int getINVALID_OTP() {
        return 0;
    }
    
    public final int getOTP_MATCH_CODE() {
        return 0;
    }
}