package com.smf.customer.dialog;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\u0018\u0000 )2\u00020\u00012\u00020\u0002:\u0001)B\u0005\u00a2\u0006\u0002\u0010\u0003J$\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010 2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016J\u0010\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020\u0014H\u0016J\b\u0010&\u001a\u00020$H\u0016J\b\u0010\'\u001a\u00020$H\u0016J\b\u0010(\u001a\u00020$H\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R \u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u00020\u000eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0013\u001a\u00020\u0014X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006*"}, d2 = {"Lcom/smf/customer/dialog/ListDialogFragment;", "Lcom/smf/customer/app/base/BaseDialogFragment;", "Lcom/smf/customer/app/listener/AdapterOneClickListener;", "()V", "dataBinding", "Lcom/smf/customer/databinding/FragmentDialogListBinding;", "dialogListItemList", "Ljava/util/ArrayList;", "Lcom/smf/customer/data/model/dto/DialogListItem;", "getDialogListItemList", "()Ljava/util/ArrayList;", "setDialogListItemList", "(Ljava/util/ArrayList;)V", "listItemAdapter", "Lcom/smf/customer/dialog/ListItemAdapter;", "getListItemAdapter", "()Lcom/smf/customer/dialog/ListItemAdapter;", "setListItemAdapter", "(Lcom/smf/customer/dialog/ListItemAdapter;)V", "mPosition", "", "getMPosition", "()I", "setMPosition", "(I)V", "twoButtonListener", "Lcom/smf/customer/app/listener/SelectItemStringParamListener;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onOneClick", "", "position", "onPause", "setData", "setupClickListeners", "Companion", "app_devDebug"})
public final class ListDialogFragment extends com.smf.customer.app.base.BaseDialogFragment implements com.smf.customer.app.listener.AdapterOneClickListener {
    private com.smf.customer.app.listener.SelectItemStringParamListener twoButtonListener;
    @org.jetbrains.annotations.NotNull()
    public static final com.smf.customer.dialog.ListDialogFragment.Companion Companion = null;
    private static final java.lang.String KEY_TITLE = "KEY_TITLE";
    private static final java.lang.String KEY_LIST_ITEM = "LIST_ITEM";
    private static final java.lang.String KEY_BUTTON_1 = "KEY_BUTTON_1";
    private static final java.lang.String KEY_BUTTON_2 = "KEY_BUTTON_2";
    @org.jetbrains.annotations.NotNull()
    private java.util.ArrayList<com.smf.customer.data.model.dto.DialogListItem> dialogListItemList;
    private com.smf.customer.databinding.FragmentDialogListBinding dataBinding;
    @org.jetbrains.annotations.NotNull()
    private com.smf.customer.dialog.ListItemAdapter listItemAdapter;
    private int mPosition = -1;
    
    public ListDialogFragment() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.ArrayList<com.smf.customer.data.model.dto.DialogListItem> getDialogListItemList() {
        return null;
    }
    
    public final void setDialogListItemList(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<com.smf.customer.data.model.dto.DialogListItem> p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.smf.customer.dialog.ListItemAdapter getListItemAdapter() {
        return null;
    }
    
    public final void setListItemAdapter(@org.jetbrains.annotations.NotNull()
    com.smf.customer.dialog.ListItemAdapter p0) {
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
    
    @java.lang.Override()
    public void onPause() {
    }
    
    public final int getMPosition() {
        return 0;
    }
    
    public final void setMPosition(int p0) {
    }
    
    @java.lang.Override()
    public void onOneClick(int position) {
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002JL\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00042\u0016\u0010\u000b\u001a\u0012\u0012\u0004\u0012\u00020\r0\fj\b\u0012\u0004\u0012\u00020\r`\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u00122\b\b\u0002\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u0015\u001a\u00020\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/smf/customer/dialog/ListDialogFragment$Companion;", "", "()V", "KEY_BUTTON_1", "", "KEY_BUTTON_2", "KEY_LIST_ITEM", "KEY_TITLE", "newInstance", "Lcom/smf/customer/dialog/ListDialogFragment;", "title", "dialogListItemList", "Ljava/util/ArrayList;", "Lcom/smf/customer/data/model/dto/DialogListItem;", "Lkotlin/collections/ArrayList;", "twoButtonListener", "Lcom/smf/customer/app/listener/SelectItemStringParamListener;", "isCancelable", "", "positiveBtn", "", "negativeBtn", "app_devDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.smf.customer.dialog.ListDialogFragment newInstance(@org.jetbrains.annotations.NotNull()
        java.lang.String title, @org.jetbrains.annotations.NotNull()
        java.util.ArrayList<com.smf.customer.data.model.dto.DialogListItem> dialogListItemList, @org.jetbrains.annotations.NotNull()
        com.smf.customer.app.listener.SelectItemStringParamListener twoButtonListener, boolean isCancelable, int positiveBtn, int negativeBtn) {
            return null;
        }
    }
}