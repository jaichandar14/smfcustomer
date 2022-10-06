package com.smf.customer.dialog;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u0000 \u00122\u00020\u0001:\u0001\u0012B\u0005\u00a2\u0006\u0002\u0010\u0002J$\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0016J\b\u0010\u000f\u001a\u00020\u0010H\u0016J\b\u0010\u0011\u001a\u00020\u0010H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/smf/customer/dialog/OneInputTwoBtnDialogFragment;", "Lcom/smf/customer/app/base/BaseDialogFragment;", "()V", "dataBinding", "Lcom/smf/customer/databinding/FragmentDialogTwoBtnInputBinding;", "oneButtonStringParamListener", "Lcom/smf/customer/app/listener/OneButtonStringParamListener;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "setData", "", "setupClickListeners", "Companion", "app_devDebug"})
public final class OneInputTwoBtnDialogFragment extends com.smf.customer.app.base.BaseDialogFragment {
    private com.smf.customer.app.listener.OneButtonStringParamListener oneButtonStringParamListener;
    @org.jetbrains.annotations.NotNull()
    public static final com.smf.customer.dialog.OneInputTwoBtnDialogFragment.Companion Companion = null;
    private static final java.lang.String KEY_TITLE = "KEY_TITLE";
    private static final java.lang.String KEY_INPUT_TYPE = "KEY_INPUT_TYPE";
    private static final java.lang.String KEY_INPUT_HINT = "KEY_INPUT_HINT";
    private static final java.lang.String KEY_POSITIVE = "KEY_POSITIVE";
    private static final java.lang.String KEY_NEGATIVE = "KEY_NEGATIVE";
    private com.smf.customer.databinding.FragmentDialogTwoBtnInputBinding dataBinding;
    
    public OneInputTwoBtnDialogFragment() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void setData() {
    }
    
    @java.lang.Override()
    public void setupClickListeners() {
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J:\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\r2\b\b\u0002\u0010\u0012\u001a\u00020\rR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/smf/customer/dialog/OneInputTwoBtnDialogFragment$Companion;", "", "()V", "KEY_INPUT_HINT", "", "KEY_INPUT_TYPE", "KEY_NEGATIVE", "KEY_POSITIVE", "KEY_TITLE", "newInstance", "Lcom/smf/customer/dialog/OneInputTwoBtnDialogFragment;", "title", "inputType", "", "inputHint", "oneButtonStringParamListener", "Lcom/smf/customer/app/listener/OneButtonStringParamListener;", "positiveButton", "negativeButton", "app_devDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.smf.customer.dialog.OneInputTwoBtnDialogFragment newInstance(@org.jetbrains.annotations.NotNull()
        java.lang.String title, int inputType, @org.jetbrains.annotations.NotNull()
        java.lang.String inputHint, @org.jetbrains.annotations.NotNull()
        com.smf.customer.app.listener.OneButtonStringParamListener oneButtonStringParamListener, int positiveButton, int negativeButton) {
            return null;
        }
    }
}