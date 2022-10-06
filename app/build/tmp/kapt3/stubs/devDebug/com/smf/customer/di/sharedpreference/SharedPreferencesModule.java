package com.smf.customer.di.sharedpreference;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u0007\u001a\u00020\u0005H\u0007J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0005H\u0007R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/smf/customer/di/sharedpreference/SharedPreferencesModule;", "", "context", "Landroid/content/Context;", "sharedPreferenceName", "", "(Landroid/content/Context;Ljava/lang/String;)V", "getMainKeyAlias", "provideSharedPreferences", "Landroid/content/SharedPreferences;", "mainKeyAlias", "app_devDebug"})
@dagger.Module()
public final class SharedPreferencesModule {
    private final android.content.Context context = null;
    private final java.lang.String sharedPreferenceName = null;
    
    public SharedPreferencesModule(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String sharedPreferenceName) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    @javax.inject.Singleton()
    @dagger.Provides()
    public final android.content.SharedPreferences provideSharedPreferences(@org.jetbrains.annotations.NotNull()
    java.lang.String mainKeyAlias) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @dagger.Provides()
    public final java.lang.String getMainKeyAlias() {
        return null;
    }
}