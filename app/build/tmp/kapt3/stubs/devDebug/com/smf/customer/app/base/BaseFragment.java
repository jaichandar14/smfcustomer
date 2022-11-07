package com.smf.customer.app.base;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000b\b&\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0003B\u0005\u00a2\u0006\u0002\u0010\u0004R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001e\u0010\u000b\u001a\u00020\f8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001c\u0010\u0011\u001a\u00028\u0000X\u0086.\u00a2\u0006\u0010\n\u0002\u0010\u0016\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015\u00a8\u0006\u0017"}, d2 = {"Lcom/smf/customer/app/base/BaseFragment;", "T", "Lcom/smf/customer/app/base/BaseViewModel;", "Landroidx/fragment/app/Fragment;", "()V", "TAG", "", "getTAG", "()Ljava/lang/String;", "setTAG", "(Ljava/lang/String;)V", "preferenceHelper", "Lcom/smf/customer/di/sharedpreference/SharedPrefsHelper;", "getPreferenceHelper", "()Lcom/smf/customer/di/sharedpreference/SharedPrefsHelper;", "setPreferenceHelper", "(Lcom/smf/customer/di/sharedpreference/SharedPrefsHelper;)V", "viewModel", "getViewModel", "()Lcom/smf/customer/app/base/BaseViewModel;", "setViewModel", "(Lcom/smf/customer/app/base/BaseViewModel;)V", "Lcom/smf/customer/app/base/BaseViewModel;", "app_devDebug"})
public abstract class BaseFragment<T extends com.smf.customer.app.base.BaseViewModel> extends androidx.fragment.app.Fragment {
    @javax.inject.Inject()
    public com.smf.customer.di.sharedpreference.SharedPrefsHelper preferenceHelper;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String TAG;
    public T viewModel;
    
    public BaseFragment() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.smf.customer.di.sharedpreference.SharedPrefsHelper getPreferenceHelper() {
        return null;
    }
    
    public final void setPreferenceHelper(@org.jetbrains.annotations.NotNull()
    com.smf.customer.di.sharedpreference.SharedPrefsHelper p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTAG() {
        return null;
    }
    
    public final void setTAG(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final T getViewModel() {
        return null;
    }
    
    public final void setViewModel(@org.jetbrains.annotations.NotNull()
    T p0) {
    }
}