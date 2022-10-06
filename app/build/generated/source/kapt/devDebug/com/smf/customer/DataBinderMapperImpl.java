package com.smf.customer;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.smf.customer.databinding.FragmentDialogListBindingImpl;
import com.smf.customer.databinding.FragmentDialogOneButtonBindingImpl;
import com.smf.customer.databinding.FragmentDialogTwoBtnInputBindingImpl;
import com.smf.customer.databinding.FragmentDialogTwoButtonBindingImpl;
import com.smf.customer.databinding.LayoutDialogListItemBindingImpl;
import com.smf.customer.databinding.ProgressbarBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_FRAGMENTDIALOGLIST = 1;

  private static final int LAYOUT_FRAGMENTDIALOGONEBUTTON = 2;

  private static final int LAYOUT_FRAGMENTDIALOGTWOBTNINPUT = 3;

  private static final int LAYOUT_FRAGMENTDIALOGTWOBUTTON = 4;

  private static final int LAYOUT_LAYOUTDIALOGLISTITEM = 5;

  private static final int LAYOUT_PROGRESSBAR = 6;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(6);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.smf.customer.R.layout.fragment_dialog_list, LAYOUT_FRAGMENTDIALOGLIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.smf.customer.R.layout.fragment_dialog_one_button, LAYOUT_FRAGMENTDIALOGONEBUTTON);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.smf.customer.R.layout.fragment_dialog_two_btn_input, LAYOUT_FRAGMENTDIALOGTWOBTNINPUT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.smf.customer.R.layout.fragment_dialog_two_button, LAYOUT_FRAGMENTDIALOGTWOBUTTON);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.smf.customer.R.layout.layout_dialog_list_item, LAYOUT_LAYOUTDIALOGLISTITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.smf.customer.R.layout.progressbar, LAYOUT_PROGRESSBAR);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_FRAGMENTDIALOGLIST: {
          if ("layout/fragment_dialog_list_0".equals(tag)) {
            return new FragmentDialogListBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_dialog_list is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTDIALOGONEBUTTON: {
          if ("layout/fragment_dialog_one_button_0".equals(tag)) {
            return new FragmentDialogOneButtonBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_dialog_one_button is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTDIALOGTWOBTNINPUT: {
          if ("layout/fragment_dialog_two_btn_input_0".equals(tag)) {
            return new FragmentDialogTwoBtnInputBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_dialog_two_btn_input is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTDIALOGTWOBUTTON: {
          if ("layout/fragment_dialog_two_button_0".equals(tag)) {
            return new FragmentDialogTwoButtonBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_dialog_two_button is invalid. Received: " + tag);
        }
        case  LAYOUT_LAYOUTDIALOGLISTITEM: {
          if ("layout/layout_dialog_list_item_0".equals(tag)) {
            return new LayoutDialogListItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for layout_dialog_list_item is invalid. Received: " + tag);
        }
        case  LAYOUT_PROGRESSBAR: {
          if ("layout/progressbar_0".equals(tag)) {
            return new ProgressbarBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for progressbar is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(2);

    static {
      sKeys.put(0, "_all");
      sKeys.put(1, "visibility");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(6);

    static {
      sKeys.put("layout/fragment_dialog_list_0", com.smf.customer.R.layout.fragment_dialog_list);
      sKeys.put("layout/fragment_dialog_one_button_0", com.smf.customer.R.layout.fragment_dialog_one_button);
      sKeys.put("layout/fragment_dialog_two_btn_input_0", com.smf.customer.R.layout.fragment_dialog_two_btn_input);
      sKeys.put("layout/fragment_dialog_two_button_0", com.smf.customer.R.layout.fragment_dialog_two_button);
      sKeys.put("layout/layout_dialog_list_item_0", com.smf.customer.R.layout.layout_dialog_list_item);
      sKeys.put("layout/progressbar_0", com.smf.customer.R.layout.progressbar);
    }
  }
}
