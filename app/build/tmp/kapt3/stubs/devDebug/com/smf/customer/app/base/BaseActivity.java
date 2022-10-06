package com.smf.customer.app.base;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b&\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u00032\u00020\u0004B\u0005\u00a2\u0006\u0002\u0010\u0005J\b\u0010\u0018\u001a\u00020\u0019H\u0016J\b\u0010\u001a\u001a\u00020\u0019H\u0016J\u0010\u0010\u001b\u001a\u00020\u00192\u0006\u0010\u001c\u001a\u00020\u001dH\u0016J\u0010\u0010\u001e\u001a\u00020\u00192\u0006\u0010\u001c\u001a\u00020\u001dH\u0016J\u0012\u0010\u001f\u001a\u00020\u00192\b\u0010 \u001a\u0004\u0018\u00010!H\u0014J\b\u0010\"\u001a\u00020\u0019H\u0016R\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001e\u0010\f\u001a\u00020\r8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001c\u0010\u0012\u001a\u00028\u0000X\u0086.\u00a2\u0006\u0010\n\u0002\u0010\u0017\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016\u00a8\u0006#"}, d2 = {"Lcom/smf/customer/app/base/BaseActivity;", "T", "Lcom/smf/customer/app/base/BaseViewModel;", "Lcom/smf/customer/app/base/AppActivity;", "Lcom/smf/customer/listener/DialogTwoButtonListener;", "()V", "TAG", "", "getTAG", "()Ljava/lang/String;", "setTAG", "(Ljava/lang/String;)V", "preferenceHelper", "Lcom/smf/customer/di/sharedpreference/SharedPrefsHelper;", "getPreferenceHelper", "()Lcom/smf/customer/di/sharedpreference/SharedPrefsHelper;", "setPreferenceHelper", "(Lcom/smf/customer/di/sharedpreference/SharedPrefsHelper;)V", "viewModel", "getViewModel", "()Lcom/smf/customer/app/base/BaseViewModel;", "setViewModel", "(Lcom/smf/customer/app/base/BaseViewModel;)V", "Lcom/smf/customer/app/base/BaseViewModel;", "observer", "", "onDialogDismissed", "onNegativeClick", "dialogFragment", "Landroidx/fragment/app/DialogFragment;", "onPositiveClick", "onPostCreate", "savedInstanceState", "Landroid/os/Bundle;", "showRetryDialog", "app_devDebug"})
public abstract class BaseActivity<T extends com.smf.customer.app.base.BaseViewModel> extends com.smf.customer.app.base.AppActivity implements com.smf.customer.listener.DialogTwoButtonListener {
    @javax.inject.Inject()
    public com.smf.customer.di.sharedpreference.SharedPrefsHelper preferenceHelper;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String TAG;
    public T viewModel;
    
    public BaseActivity() {
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
    
    public void observer() {
    }
    
    @java.lang.Override()
    protected void onPostCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    public void showRetryDialog() {
    }
    
    @java.lang.Override()
    public void onNegativeClick(@org.jetbrains.annotations.NotNull()
    androidx.fragment.app.DialogFragment dialogFragment) {
    }
    
    @java.lang.Override()
    public void onPositiveClick(@org.jetbrains.annotations.NotNull()
    androidx.fragment.app.DialogFragment dialogFragment) {
    }
    
    @java.lang.Override()
    public void onDialogDismissed() {
    }
}