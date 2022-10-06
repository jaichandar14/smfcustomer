package com.smf.customer.data.repository;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u00a2\u0006\u0002\u0010\u0003J\u0016\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\b\u001a\u00020\tH\u0016R\u000e\u0010\u0004\u001a\u00020\u0001X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/smf/customer/data/repository/UserRepositoryImpl;", "Lcom/smf/customer/data/api_service/UserService;", "userService", "(Lcom/smf/customer/data/api_service/UserService;)V", "mUserService", "loginClient", "Lio/reactivex/Observable;", "Lcom/smf/customer/data/model/response/LoginResponseDTO;", "requestDTO", "Lcom/smf/customer/data/model/request/UserRequestDTO;", "app_devDebug"})
public final class UserRepositoryImpl implements com.smf.customer.data.api_service.UserService {
    private com.smf.customer.data.api_service.UserService mUserService;
    
    @javax.inject.Inject()
    public UserRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.smf.customer.data.api_service.UserService userService) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public io.reactivex.Observable<com.smf.customer.data.model.response.LoginResponseDTO> loginClient(@org.jetbrains.annotations.NotNull()
    com.smf.customer.data.model.request.UserRequestDTO requestDTO) {
        return null;
    }
}