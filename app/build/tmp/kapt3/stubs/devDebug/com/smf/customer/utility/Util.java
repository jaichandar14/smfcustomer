package com.smf.customer.utility;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0004"}, d2 = {"Lcom/smf/customer/utility/Util;", "", "()V", "Companion", "app_devDebug"})
public final class Util {
    @org.jetbrains.annotations.NotNull()
    public static final com.smf.customer.utility.Util.Companion Companion = null;
    
    public Util() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000b\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004J\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tJ\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0004J\u000e\u0010\r\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0004J\u000e\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0004J\u000e\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0004J\u0010\u0010\u0010\u001a\u00020\u000b2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0004J\u0010\u0010\u0012\u001a\u00020\u000b2\b\u0010\u0013\u001a\u0004\u0018\u00010\u0004J\u000e\u0010\u0014\u001a\u00020\u00072\u0006\u0010\u0015\u001a\u00020\u0004\u00a8\u0006\u0016"}, d2 = {"Lcom/smf/customer/utility/Util$Companion;", "", "()V", "convertToLocalCurrency", "", "amount", "getNewFCMToken", "", "preferenceHelper", "Lcom/smf/customer/di/sharedpreference/SharedPrefsHelper;", "isBankAccountValid", "", "string", "isIFSCValid", "isMobileNumberValid", "isOnlyNumber", "isValidEmail", "email", "isValidPassword", "password", "setUserIdToCrashlytics", "userId", "app_devDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final void setUserIdToCrashlytics(@org.jetbrains.annotations.NotNull()
        java.lang.String userId) {
        }
        
        public final void getNewFCMToken(@org.jetbrains.annotations.NotNull()
        com.smf.customer.di.sharedpreference.SharedPrefsHelper preferenceHelper) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String convertToLocalCurrency(@org.jetbrains.annotations.NotNull()
        java.lang.String amount) {
            return null;
        }
        
        public final boolean isOnlyNumber(@org.jetbrains.annotations.NotNull()
        java.lang.String string) {
            return false;
        }
        
        public final boolean isBankAccountValid(@org.jetbrains.annotations.NotNull()
        java.lang.String string) {
            return false;
        }
        
        public final boolean isIFSCValid(@org.jetbrains.annotations.NotNull()
        java.lang.String string) {
            return false;
        }
        
        public final boolean isMobileNumberValid(@org.jetbrains.annotations.NotNull()
        java.lang.String string) {
            return false;
        }
        
        public final boolean isValidEmail(@org.jetbrains.annotations.Nullable()
        java.lang.String email) {
            return false;
        }
        
        public final boolean isValidPassword(@org.jetbrains.annotations.Nullable()
        java.lang.String password) {
            return false;
        }
    }
}