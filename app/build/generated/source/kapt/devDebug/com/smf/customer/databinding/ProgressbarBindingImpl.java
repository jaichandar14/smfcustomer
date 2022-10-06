package com.smf.customer.databinding;
import com.smf.customer.R;
import com.smf.customer.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ProgressbarBindingImpl extends ProgressbarBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.clProgress, 1);
        sViewsWithIds.put(R.id.tvLoading, 2);
        sViewsWithIds.put(R.id.fullscreen_content, 3);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ProgressbarBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 4, sIncludes, sViewsWithIds));
    }
    private ProgressbarBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (androidx.core.widget.ContentLoadingProgressBar) bindings[1]
            , (android.widget.TextView) bindings[3]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[0]
            , (android.widget.TextView) bindings[2]
            );
        this.progressBar.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.visibility == variableId) {
            setVisibility((java.lang.Integer) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setVisibility(@Nullable java.lang.Integer Visibility) {
        this.mVisibility = Visibility;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.visibility);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        int androidxDatabindingViewDataBindingSafeUnboxVisibility = 0;
        java.lang.Integer visibility = mVisibility;

        if ((dirtyFlags & 0x3L) != 0) {



                // read androidx.databinding.ViewDataBinding.safeUnbox(visibility)
                androidxDatabindingViewDataBindingSafeUnboxVisibility = androidx.databinding.ViewDataBinding.safeUnbox(visibility);
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            this.progressBar.setVisibility(androidxDatabindingViewDataBindingSafeUnboxVisibility);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): visibility
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}