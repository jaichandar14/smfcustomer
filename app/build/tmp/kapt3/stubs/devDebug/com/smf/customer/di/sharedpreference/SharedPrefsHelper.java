package com.smf.customer.di.sharedpreference;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\b\u0005\b\u0017\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eJ\u0019\u0010\u000f\u001a\u00020\u00102\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u0010H\u0086\u0002J\u0019\u0010\u000f\u001a\u00020\u00122\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u0012H\u0086\u0002J\u0019\u0010\u000f\u001a\u00020\u00132\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u0013H\u0086\u0002J\u0019\u0010\u000f\u001a\u00020\u00142\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u0014H\u0086\u0002J\u0019\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u000eH\u0086\u0002J\u0006\u0010\u0015\u001a\u00020\u000eJ\u0016\u0010\u0016\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0017\u001a\u00020\u0010J\u0016\u0010\u0016\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0017\u001a\u00020\u0012J\u0016\u0010\u0016\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0017\u001a\u00020\u0013J\u0016\u0010\u0016\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0017\u001a\u00020\u0014J\u0016\u0010\u0016\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0017\u001a\u00020\u000eJ\u000e\u0010\u0018\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eR\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/smf/customer/di/sharedpreference/SharedPrefsHelper;", "", "mSharedPreferences", "Landroid/content/SharedPreferences;", "(Landroid/content/SharedPreferences;)V", "editor", "Landroid/content/SharedPreferences$Editor;", "getEditor", "()Landroid/content/SharedPreferences$Editor;", "setEditor", "(Landroid/content/SharedPreferences$Editor;)V", "deleteSavedData", "", "key", "", "get", "", "defaultValue", "", "", "", "getFullName", "put", "value", "remove", "app_devDebug"})
@javax.inject.Singleton()
public class SharedPrefsHelper {
    private final android.content.SharedPreferences mSharedPreferences = null;
    @org.jetbrains.annotations.NotNull()
    private android.content.SharedPreferences.Editor editor;
    
    @javax.inject.Inject()
    public SharedPrefsHelper(@org.jetbrains.annotations.NotNull()
    android.content.SharedPreferences mSharedPreferences) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.content.SharedPreferences.Editor getEditor() {
        return null;
    }
    
    public final void setEditor(@org.jetbrains.annotations.NotNull()
    android.content.SharedPreferences.Editor p0) {
    }
    
    public final void remove(@org.jetbrains.annotations.NotNull()
    java.lang.String key) {
    }
    
    public final void put(@org.jetbrains.annotations.NotNull()
    java.lang.String key, @org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void put(@org.jetbrains.annotations.NotNull()
    java.lang.String key, int value) {
    }
    
    public final void put(@org.jetbrains.annotations.NotNull()
    java.lang.String key, float value) {
    }
    
    public final void put(@org.jetbrains.annotations.NotNull()
    java.lang.String key, boolean value) {
    }
    
    public final void put(@org.jetbrains.annotations.NotNull()
    java.lang.String key, long value) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String get(@org.jetbrains.annotations.NotNull()
    java.lang.String key, @org.jetbrains.annotations.NotNull()
    java.lang.String defaultValue) {
        return null;
    }
    
    public final int get(@org.jetbrains.annotations.NotNull()
    java.lang.String key, int defaultValue) {
        return 0;
    }
    
    public final long get(@org.jetbrains.annotations.NotNull()
    java.lang.String key, long defaultValue) {
        return 0L;
    }
    
    public final float get(@org.jetbrains.annotations.NotNull()
    java.lang.String key, float defaultValue) {
        return 0.0F;
    }
    
    public final boolean get(@org.jetbrains.annotations.NotNull()
    java.lang.String key, boolean defaultValue) {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFullName() {
        return null;
    }
    
    public final void deleteSavedData(@org.jetbrains.annotations.NotNull()
    java.lang.String key) {
    }
}