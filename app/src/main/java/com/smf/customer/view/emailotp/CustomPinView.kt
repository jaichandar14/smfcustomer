package com.smf.customer.view.emailotp

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import com.smf.customer.R
import com.smf.customer.databinding.EmailOtpActivityBinding
import javax.inject.Inject

class CustomPinView @Inject constructor() {
    private lateinit var otp0: EditText
    private lateinit var otp1: EditText
    private lateinit var otp2: EditText
    private lateinit var otp3: EditText

    fun initializePin(mDataBinding: EmailOtpActivityBinding) {
        otp0 = mDataBinding.otp1ed
        otp1 = mDataBinding.otp2ed
        otp2 = mDataBinding.otp3ed
        otp3 = mDataBinding.otp4ed

        otp0.tag = 0
        otp1.tag = 1
        otp2.tag = 2
        otp3.tag = 3

        //GenericTextWatcher here works only for moving to next EditText when a number is entered
        //first parameter is the current EditText and second parameter is next EditText
        otp0.addTextChangedListener(GenericTextWatcher(otp0, otp1))
        otp1.addTextChangedListener(GenericTextWatcher(otp1, otp2))
        otp2.addTextChangedListener(GenericTextWatcher(otp2, otp3))
        otp3.addTextChangedListener(GenericTextWatcher(otp3, null))

        //GenericKeyEvent here works for deleting the element and to switch back to previous EditText
        //first parameter is the current EditText and second parameter is previous EditText
        otp0.setOnKeyListener(GenericKeyEvent(otp0, null))
        otp1.setOnKeyListener(GenericKeyEvent(otp1, otp0))
        otp2.setOnKeyListener(GenericKeyEvent(otp2, otp1))
        otp3.setOnKeyListener(GenericKeyEvent(otp3, otp2))
        // Method for submit event implementation
        //  submitBtnClicked()
    }

    class GenericKeyEvent internal constructor(
        private val currentView: EditText,
        private val previousView: EditText?,
    ) : View.OnKeyListener {
        override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.otp1ed && currentView.text.isEmpty()) {
                //If current is empty then previous EditText's number will also be deleted
                previousView!!.text = null
                previousView.requestFocus()
                return true
            }
            return false
        }
    }

    class GenericTextWatcher internal constructor(
        private val currentView: View,
        private val nextView: View?,
    ) : TextWatcher {
        override fun afterTextChanged(editable: Editable) {
            val text = editable.toString()
            when (currentView.id) {
                R.id.otp1ed -> if (text.length == 1) nextView!!.requestFocus()
                R.id.otp2ed -> if (text.length == 1) nextView!!.requestFocus()
                R.id.otp3ed -> if (text.length == 1) nextView!!.requestFocus()
//                R.id.otp4ed -> if (text.length == 1) nextView!!.requestFocus()
                //You can use EditText4 same as above to hide the keyboard
            }
        }

        override fun beforeTextChanged(
            arg0: CharSequence,
            arg1: Int,
            arg2: Int,
            arg3: Int,
        ) {
        }

        override fun onTextChanged(
            arg0: CharSequence,
            arg1: Int,
            arg2: Int,
            arg3: Int,
        ) {
        }
    }
}