package com.smf.customer.data.api_service;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\'\u00a8\u0006\u0007"}, d2 = {"Lcom/smf/customer/data/api_service/UserService;", "", "loginClient", "Lio/reactivex/Observable;", "Lcom/smf/customer/data/model/response/LoginResponseDTO;", "userRequestDTO", "Lcom/smf/customer/data/model/request/UserRequestDTO;", "app_devDebug"})
public abstract interface UserService {
    
    @org.jetbrains.annotations.NotNull()
    @retrofit2.http.POST(value = "user/v1/login/")
    public abstract io.reactivex.Observable<com.smf.customer.data.model.response.LoginResponseDTO> loginClient(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Body()
    com.smf.customer.data.model.request.UserRequestDTO userRequestDTO);
}