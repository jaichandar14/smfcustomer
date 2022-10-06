package com.smf.customer.app.listener;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\bf\u0018\u00002\u00020\u0001J\u001a\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H&\u00a8\u0006\b"}, d2 = {"Lcom/smf/customer/app/listener/OnDateOrTimeSelectListener;", "", "onDateOrTimeSelect", "", "dateTime", "", "timeStamp", "", "app_devDebug"})
public abstract interface OnDateOrTimeSelectListener {
    
    public abstract void onDateOrTimeSelect(@org.jetbrains.annotations.NotNull()
    java.lang.String dateTime, long timeStamp);
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 3)
    public final class DefaultImpls {
    }
}