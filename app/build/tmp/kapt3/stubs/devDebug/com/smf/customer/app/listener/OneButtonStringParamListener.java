package com.smf.customer.app.listener;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\bf\u0018\u00002\u00020\u00012\u00020\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H&\u00a8\u0006\u0007"}, d2 = {"Lcom/smf/customer/app/listener/OneButtonStringParamListener;", "Lcom/smf/customer/app/listener/DialogDismissListener;", "Lcom/smf/customer/app/listener/DialogOneButtonListener;", "onPositiveClick", "", "inputValue", "", "app_devDebug"})
public abstract interface OneButtonStringParamListener extends com.smf.customer.app.listener.DialogDismissListener, com.smf.customer.app.listener.DialogOneButtonListener {
    
    public abstract void onPositiveClick(@org.jetbrains.annotations.NotNull()
    java.lang.String inputValue);
}