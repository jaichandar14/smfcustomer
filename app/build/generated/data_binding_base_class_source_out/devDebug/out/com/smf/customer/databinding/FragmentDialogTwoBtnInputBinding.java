// Generated by data binding compiler. Do not edit!
package com.smf.customer.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.smf.customer.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class FragmentDialogTwoBtnInputBinding extends ViewDataBinding {
  protected FragmentDialogTwoBtnInputBinding(Object _bindingComponent, View _root,
      int _localFieldCount) {
    super(_bindingComponent, _root, _localFieldCount);
  }

  @NonNull
  public static FragmentDialogTwoBtnInputBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_two_btn_input, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static FragmentDialogTwoBtnInputBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<FragmentDialogTwoBtnInputBinding>inflateInternal(inflater, R.layout.fragment_dialog_two_btn_input, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentDialogTwoBtnInputBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_two_btn_input, null, false, component)
   */
  @NonNull
  @Deprecated
  public static FragmentDialogTwoBtnInputBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<FragmentDialogTwoBtnInputBinding>inflateInternal(inflater, R.layout.fragment_dialog_two_btn_input, null, false, component);
  }

  public static FragmentDialogTwoBtnInputBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static FragmentDialogTwoBtnInputBinding bind(@NonNull View view,
      @Nullable Object component) {
    return (FragmentDialogTwoBtnInputBinding)bind(component, view, R.layout.fragment_dialog_two_btn_input);
  }
}