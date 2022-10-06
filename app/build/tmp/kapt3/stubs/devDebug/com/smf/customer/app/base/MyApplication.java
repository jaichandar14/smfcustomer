package com.smf.customer.app.base;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u0000 \u000e2\u00020\u0001:\u0001\u000eB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0011\u0010\t\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u000bH\u0086\u0002J\b\u0010\f\u001a\u00020\rH\u0016R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u00a8\u0006\u000f"}, d2 = {"Lcom/smf/customer/app/base/MyApplication;", "Landroid/app/Application;", "()V", "TAG", "", "getTAG", "()Ljava/lang/String;", "setTAG", "(Ljava/lang/String;)V", "get", "context", "Landroid/content/Context;", "onCreate", "", "Companion", "app_devDebug"})
public final class MyApplication extends android.app.Application {
    @org.jetbrains.annotations.NotNull()
    private java.lang.String TAG;
    @org.jetbrains.annotations.NotNull()
    public static final com.smf.customer.app.base.MyApplication.Companion Companion = null;
    public static android.content.Context appContext;
    public static com.smf.customer.di.AppComponent applicationComponent;
    
    public MyApplication() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTAG() {
        return null;
    }
    
    public final void setTAG(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.smf.customer.app.base.MyApplication get(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e\u00a8\u0006\u000f"}, d2 = {"Lcom/smf/customer/app/base/MyApplication$Companion;", "", "()V", "appContext", "Landroid/content/Context;", "getAppContext", "()Landroid/content/Context;", "setAppContext", "(Landroid/content/Context;)V", "applicationComponent", "Lcom/smf/customer/di/AppComponent;", "getApplicationComponent", "()Lcom/smf/customer/di/AppComponent;", "setApplicationComponent", "(Lcom/smf/customer/di/AppComponent;)V", "app_devDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.content.Context getAppContext() {
            return null;
        }
        
        public final void setAppContext(@org.jetbrains.annotations.NotNull()
        android.content.Context p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.smf.customer.di.AppComponent getApplicationComponent() {
            return null;
        }
        
        public final void setApplicationComponent(@org.jetbrains.annotations.NotNull()
        com.smf.customer.di.AppComponent p0) {
        }
    }
}