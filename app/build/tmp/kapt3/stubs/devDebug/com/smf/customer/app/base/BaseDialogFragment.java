package com.smf.customer.app.base;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b&\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\t\u001a\u00020\nH\u0016J\b\u0010\u000b\u001a\u00020\nH\u0016J\u001a\u0010\f\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0016J\b\u0010\u0011\u001a\u00020\nH&J\b\u0010\u0012\u001a\u00020\nH&R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u00a8\u0006\u0013"}, d2 = {"Lcom/smf/customer/app/base/BaseDialogFragment;", "Landroidx/fragment/app/DialogFragment;", "()V", "dialogDismissListener", "Lcom/smf/customer/app/listener/DialogDismissListener;", "getDialogDismissListener", "()Lcom/smf/customer/app/listener/DialogDismissListener;", "setDialogDismissListener", "(Lcom/smf/customer/app/listener/DialogDismissListener;)V", "onDestroy", "", "onStart", "onViewCreated", "view", "Landroid/view/View;", "savedInstanceState", "Landroid/os/Bundle;", "setData", "setupClickListeners", "app_devDebug"})
public abstract class BaseDialogFragment extends androidx.fragment.app.DialogFragment {
    public com.smf.customer.app.listener.DialogDismissListener dialogDismissListener;
    
    public BaseDialogFragment() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.smf.customer.app.listener.DialogDismissListener getDialogDismissListener() {
        return null;
    }
    
    public final void setDialogDismissListener(@org.jetbrains.annotations.NotNull()
    com.smf.customer.app.listener.DialogDismissListener p0) {
    }
    
    @java.lang.Override()
    public void onStart() {
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    public abstract void setData();
    
    public abstract void setupClickListeners();
}