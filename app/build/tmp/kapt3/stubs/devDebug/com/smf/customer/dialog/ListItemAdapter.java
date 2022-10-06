package com.smf.customer.dialog;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u001fB\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\b\u0010\u0013\u001a\u00020\u000eH\u0016J\u0018\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00022\u0006\u0010\u0017\u001a\u00020\u000eH\u0016J\u0018\u0010\u0018\u001a\u00020\u00022\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u000eH\u0016J\u0016\u0010\u001c\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\u0002J\u001e\u0010\u001d\u001a\u00020\u00152\u0016\u0010\u001e\u001a\u0012\u0012\u0004\u0012\u00020\u000b0\nj\b\u0012\u0004\u0012\u00020\u000b`\fR\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\u0005R\u001e\u0010\t\u001a\u0012\u0012\u0004\u0012\u00020\u000b0\nj\b\u0012\u0004\u0012\u00020\u000b`\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u00020\u000eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012\u00a8\u0006 "}, d2 = {"Lcom/smf/customer/dialog/ListItemAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/smf/customer/dialog/ListItemAdapter$ListItemViewHolder;", "adapterOneClickListener", "Lcom/smf/customer/app/listener/AdapterOneClickListener;", "(Lcom/smf/customer/app/listener/AdapterOneClickListener;)V", "getAdapterOneClickListener", "()Lcom/smf/customer/app/listener/AdapterOneClickListener;", "setAdapterOneClickListener", "mDialogListItemList", "Ljava/util/ArrayList;", "Lcom/smf/customer/data/model/dto/DialogListItem;", "Lkotlin/collections/ArrayList;", "selectedPosition", "", "getSelectedPosition", "()I", "setSelectedPosition", "(I)V", "getItemCount", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "setBackground", "setDialogListItemList", "dialogListItemList", "ListItemViewHolder", "app_devDebug"})
public final class ListItemAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.smf.customer.dialog.ListItemAdapter.ListItemViewHolder> {
    @org.jetbrains.annotations.NotNull()
    private com.smf.customer.app.listener.AdapterOneClickListener adapterOneClickListener;
    private java.util.ArrayList<com.smf.customer.data.model.dto.DialogListItem> mDialogListItemList;
    private int selectedPosition = -1;
    
    public ListItemAdapter(@org.jetbrains.annotations.NotNull()
    com.smf.customer.app.listener.AdapterOneClickListener adapterOneClickListener) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.smf.customer.app.listener.AdapterOneClickListener getAdapterOneClickListener() {
        return null;
    }
    
    public final void setAdapterOneClickListener(@org.jetbrains.annotations.NotNull()
    com.smf.customer.app.listener.AdapterOneClickListener p0) {
    }
    
    public final int getSelectedPosition() {
        return 0;
    }
    
    public final void setSelectedPosition(int p0) {
    }
    
    public final void setDialogListItemList(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<com.smf.customer.data.model.dto.DialogListItem> dialogListItemList) {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.smf.customer.dialog.ListItemAdapter.ListItemViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.smf.customer.dialog.ListItemAdapter.ListItemViewHolder holder, int position) {
    }
    
    public final void setBackground(int position, @org.jetbrains.annotations.NotNull()
    com.smf.customer.dialog.ListItemAdapter.ListItemViewHolder holder) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\u0004\u00a8\u0006\f"}, d2 = {"Lcom/smf/customer/dialog/ListItemAdapter$ListItemViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "layout", "Lcom/smf/customer/databinding/LayoutDialogListItemBinding;", "(Lcom/smf/customer/databinding/LayoutDialogListItemBinding;)V", "getLayout", "()Lcom/smf/customer/databinding/LayoutDialogListItemBinding;", "setLayout", "setData", "", "dialogListItem", "Lcom/smf/customer/data/model/dto/DialogListItem;", "app_devDebug"})
    public static final class ListItemViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private com.smf.customer.databinding.LayoutDialogListItemBinding layout;
        
        public ListItemViewHolder(@org.jetbrains.annotations.NotNull()
        com.smf.customer.databinding.LayoutDialogListItemBinding layout) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.smf.customer.databinding.LayoutDialogListItemBinding getLayout() {
            return null;
        }
        
        public final void setLayout(@org.jetbrains.annotations.NotNull()
        com.smf.customer.databinding.LayoutDialogListItemBinding p0) {
        }
        
        public final void setData(@org.jetbrains.annotations.NotNull()
        com.smf.customer.data.model.dto.DialogListItem dialogListItem) {
        }
    }
}