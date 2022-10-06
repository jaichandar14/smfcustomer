package com.smf.customer.app.base;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b&\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010<\u001a\u00020=H\u0016J\u0006\u0010>\u001a\u00020=J\u0006\u0010?\u001a\u00020\u0010J\b\u0010@\u001a\u00020=H\u0014J\u0010\u0010A\u001a\u00020=2\u0006\u0010B\u001a\u00020CH\u0016J\u0010\u0010D\u001a\u00020=2\u0006\u0010E\u001a\u00020\u001eH\u0016J\u0010\u0010D\u001a\u00020=2\u0006\u0010F\u001a\u00020GH\u0016J\u000e\u0010H\u001a\u00020I2\u0006\u0010J\u001a\u00020IJ\u0006\u0010K\u001a\u00020=J\u000e\u0010L\u001a\u00020=2\u0006\u0010M\u001a\u00020\bJ\u0010\u0010L\u001a\u00020=2\b\u0010N\u001a\u0004\u0018\u00010\u0004R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000f\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u001a\u0010\u0014\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0011\"\u0004\b\u0015\u0010\u0013R \u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00100\u0017X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR&\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001e0\u001d0\u0017X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u0019\"\u0004\b \u0010\u001bR\u001e\u0010!\u001a\u00020\"8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&R\u001e\u0010\'\u001a\u00020(8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b)\u0010*\"\u0004\b+\u0010,R\u0014\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00100\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R \u0010.\u001a\b\u0012\u0004\u0012\u00020\b0\u0017X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b/\u0010\u0019\"\u0004\b0\u0010\u001bR \u00101\u001a\b\u0012\u0004\u0012\u00020\u00100\u0017X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b2\u0010\u0019\"\u0004\b3\u0010\u001bR \u00104\u001a\b\u0012\u0004\u0012\u00020\b0\u0017X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b5\u0010\u0019\"\u0004\b6\u0010\u001bR\u0017\u00107\u001a\b\u0012\u0004\u0012\u00020\u00040\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u0010\u0019R \u00109\u001a\b\u0012\u0004\u0012\u00020\u00040\u0017X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b:\u0010\u0019\"\u0004\b;\u0010\u001b\u00a8\u0006O"}, d2 = {"Lcom/smf/customer/app/base/BaseViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "TAG", "", "getTAG", "()Ljava/lang/String;", "count", "", "getCount", "()I", "setCount", "(I)V", "disposables", "Lio/reactivex/disposables/CompositeDisposable;", "isLastPage", "", "()Z", "setLastPage", "(Z)V", "isLoading", "setLoading", "logout", "Landroidx/lifecycle/MutableLiveData;", "getLogout", "()Landroidx/lifecycle/MutableLiveData;", "setLogout", "(Landroidx/lifecycle/MutableLiveData;)V", "observable", "Lio/reactivex/Observable;", "Lcom/smf/customer/data/model/response/ResponseDTO;", "getObservable", "setObservable", "preferenceHelper", "Lcom/smf/customer/di/sharedpreference/SharedPrefsHelper;", "getPreferenceHelper", "()Lcom/smf/customer/di/sharedpreference/SharedPrefsHelper;", "setPreferenceHelper", "(Lcom/smf/customer/di/sharedpreference/SharedPrefsHelper;)V", "retrofitHelper", "Lcom/smf/customer/di/retrofit/RetrofitHelper;", "getRetrofitHelper", "()Lcom/smf/customer/di/retrofit/RetrofitHelper;", "setRetrofitHelper", "(Lcom/smf/customer/di/retrofit/RetrofitHelper;)V", "retryDialog", "retryErrorMessage", "getRetryErrorMessage", "setRetryErrorMessage", "showLoading", "getShowLoading", "setShowLoading", "skip", "getSkip", "setSkip", "toastMessage", "getToastMessage", "userToken", "getUserToken", "setUserToken", "doNetworkOperation", "", "hideRetryDialogFlag", "isRetryDialogVisible", "onCleared", "onError", "throwable", "", "onSuccess", "responseDTO", "responseBody", "Lokhttp3/ResponseBody;", "prepareRequest", "Lcom/smf/customer/data/model/request/RequestDTO;", "requestDTO", "showRetryDialogFlag", "showToastMessage", "messageCode", "message", "app_devDebug"})
public abstract class BaseViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private androidx.lifecycle.MutableLiveData<java.lang.Boolean> showLoading;
    @org.jetbrains.annotations.NotNull()
    private androidx.lifecycle.MutableLiveData<java.lang.Integer> retryErrorMessage;
    @org.jetbrains.annotations.NotNull()
    private androidx.lifecycle.MutableLiveData<java.lang.Boolean> logout;
    @org.jetbrains.annotations.NotNull()
    private androidx.lifecycle.MutableLiveData<java.lang.String> userToken;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String TAG = null;
    private final io.reactivex.disposables.CompositeDisposable disposables = null;
    @org.jetbrains.annotations.NotNull()
    private androidx.lifecycle.MutableLiveData<io.reactivex.Observable<com.smf.customer.data.model.response.ResponseDTO>> observable;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    @org.jetbrains.annotations.NotNull()
    private androidx.lifecycle.MutableLiveData<java.lang.Integer> skip;
    private int count = 100;
    @javax.inject.Inject()
    public com.smf.customer.di.retrofit.RetrofitHelper retrofitHelper;
    @javax.inject.Inject()
    public com.smf.customer.di.sharedpreference.SharedPrefsHelper preferenceHelper;
    private androidx.lifecycle.MutableLiveData<java.lang.Boolean> retryDialog;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.String> toastMessage = null;
    
    public BaseViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getShowLoading() {
        return null;
    }
    
    public final void setShowLoading(@org.jetbrains.annotations.NotNull()
    androidx.lifecycle.MutableLiveData<java.lang.Boolean> p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.Integer> getRetryErrorMessage() {
        return null;
    }
    
    public final void setRetryErrorMessage(@org.jetbrains.annotations.NotNull()
    androidx.lifecycle.MutableLiveData<java.lang.Integer> p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getLogout() {
        return null;
    }
    
    public final void setLogout(@org.jetbrains.annotations.NotNull()
    androidx.lifecycle.MutableLiveData<java.lang.Boolean> p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.String> getUserToken() {
        return null;
    }
    
    public final void setUserToken(@org.jetbrains.annotations.NotNull()
    androidx.lifecycle.MutableLiveData<java.lang.String> p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTAG() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<io.reactivex.Observable<com.smf.customer.data.model.response.ResponseDTO>> getObservable() {
        return null;
    }
    
    public final void setObservable(@org.jetbrains.annotations.NotNull()
    androidx.lifecycle.MutableLiveData<io.reactivex.Observable<com.smf.customer.data.model.response.ResponseDTO>> p0) {
    }
    
    public final boolean isLastPage() {
        return false;
    }
    
    public final void setLastPage(boolean p0) {
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    public final void setLoading(boolean p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.Integer> getSkip() {
        return null;
    }
    
    public final void setSkip(@org.jetbrains.annotations.NotNull()
    androidx.lifecycle.MutableLiveData<java.lang.Integer> p0) {
    }
    
    public final int getCount() {
        return 0;
    }
    
    public final void setCount(int p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.smf.customer.di.retrofit.RetrofitHelper getRetrofitHelper() {
        return null;
    }
    
    public final void setRetrofitHelper(@org.jetbrains.annotations.NotNull()
    com.smf.customer.di.retrofit.RetrofitHelper p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.smf.customer.di.sharedpreference.SharedPrefsHelper getPreferenceHelper() {
        return null;
    }
    
    public final void setPreferenceHelper(@org.jetbrains.annotations.NotNull()
    com.smf.customer.di.sharedpreference.SharedPrefsHelper p0) {
    }
    
    public final void showRetryDialogFlag() {
    }
    
    public final void hideRetryDialogFlag() {
    }
    
    public final boolean isRetryDialogVisible() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.String> getToastMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.smf.customer.data.model.request.RequestDTO prepareRequest(@org.jetbrains.annotations.NotNull()
    com.smf.customer.data.model.request.RequestDTO requestDTO) {
        return null;
    }
    
    public final void showToastMessage(int messageCode) {
    }
    
    public final void showToastMessage(@org.jetbrains.annotations.Nullable()
    java.lang.String message) {
    }
    
    public void onError(@org.jetbrains.annotations.NotNull()
    java.lang.Throwable throwable) {
    }
    
    public void onSuccess(@org.jetbrains.annotations.NotNull()
    com.smf.customer.data.model.response.ResponseDTO responseDTO) {
    }
    
    public void onSuccess(@org.jetbrains.annotations.NotNull()
    okhttp3.ResponseBody responseBody) {
    }
    
    @java.lang.Override()
    protected void onCleared() {
    }
    
    public void doNetworkOperation() {
    }
}