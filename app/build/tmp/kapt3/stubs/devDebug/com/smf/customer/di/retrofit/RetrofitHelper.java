package com.smf.customer.di.retrofit;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0017\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0005\u001a\u00020\u0006J\b\u0010\u0007\u001a\u00020\bH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/smf/customer/di/retrofit/RetrofitHelper;", "", "retrofit", "Lretrofit2/Retrofit$Builder;", "(Lretrofit2/Retrofit$Builder;)V", "getUserRepository", "Lcom/smf/customer/data/repository/UserRepositoryImpl;", "getUserService", "Lcom/smf/customer/data/api_service/UserService;", "app_devDebug"})
@javax.inject.Singleton()
public class RetrofitHelper {
    private final retrofit2.Retrofit.Builder retrofit = null;
    
    @javax.inject.Inject()
    public RetrofitHelper(@org.jetbrains.annotations.NotNull()
    retrofit2.Retrofit.Builder retrofit) {
        super();
    }
    
    private final com.smf.customer.data.api_service.UserService getUserService() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.smf.customer.data.repository.UserRepositoryImpl getUserRepository() {
        return null;
    }
}