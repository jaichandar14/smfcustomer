package com.smf.customer.di.retrofit;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0007J\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0007J\b\u0010\u000b\u001a\u00020\nH\u0007J\b\u0010\f\u001a\u00020\rH\u0007J\u0010\u0010\u000e\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\rH\u0007\u00a8\u0006\u0010"}, d2 = {"Lcom/smf/customer/di/retrofit/NetworkModule;", "", "()V", "provideHeader", "Lokhttp3/Interceptor;", "provideRetrofitBank", "Lretrofit2/Retrofit$Builder;", "client", "Lokhttp3/OkHttpClient;", "gson", "Lcom/google/gson/Gson;", "providesGson", "providesHttpLoggingInterceptor", "Lokhttp3/logging/HttpLoggingInterceptor;", "providesOkHttpClient", "httpLoggingInterceptor", "app_devDebug"})
@dagger.Module()
public final class NetworkModule {
    
    public NetworkModule() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    @javax.inject.Singleton()
    @dagger.Provides()
    public final retrofit2.Retrofit.Builder provideRetrofitBank(@org.jetbrains.annotations.NotNull()
    okhttp3.OkHttpClient client, @org.jetbrains.annotations.NotNull()
    com.google.gson.Gson gson) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @javax.inject.Singleton()
    @dagger.Provides()
    public final okhttp3.OkHttpClient providesOkHttpClient(@org.jetbrains.annotations.NotNull()
    okhttp3.logging.HttpLoggingInterceptor httpLoggingInterceptor) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @javax.inject.Singleton()
    @dagger.Provides()
    public final okhttp3.logging.HttpLoggingInterceptor providesHttpLoggingInterceptor() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @dagger.Provides()
    public final okhttp3.Interceptor provideHeader() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @javax.inject.Singleton()
    @dagger.Provides()
    public final com.google.gson.Gson providesGson() {
        return null;
    }
}