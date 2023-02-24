package com.smf.customer.utility

import android.util.Log
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.result.AuthSessionResult
import com.amplifyframework.core.Amplify
import com.smf.customer.app.base.MyApplication
import com.smf.customer.app.constant.AppConstant
import com.smf.customer.di.sharedpreference.SharedPrefConstant
import com.smf.customer.di.sharedpreference.SharedPrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import javax.inject.Inject

class Tokens @Inject constructor() {
    init {
        MyApplication.applicationComponent?.inject(this)
    }

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    private var userIdToken: String? = null

    // Method for verify token validity
    fun checkTokenExpiry(idToken: String): String? {
        val newTime = Date().time / 1000
        val splitToken = idToken.split('.')
        try {
            val decodedBytes =
                android.util.Base64.decode(splitToken[1], android.util.Base64.DEFAULT)
            val decodeToken = String(decodedBytes)
            val tokenObj = JSONObject(decodeToken)
            val tokenObjExp = tokenObj.getString("exp").toLong()
            val newTimeMin = newTime + 1 * 60
            if (newTimeMin < tokenObjExp) {
                //Token not expired
                userIdToken = idToken
            } else {
                //  fetchNewIdToken()
                userIdToken = fetchNewIdToken()
            }
        } catch (e: Exception) {
            Log.d("TAG", "checkTokenExpiry refereshTokentime exception block $e")
        }
        return userIdToken
    }

    //Method for fetching token
    private fun fetchNewIdToken(
    ): String {
        Amplify.Auth.fetchAuthSession({
            val session = it as AWSCognitoAuthSession
            userIdToken = AuthSessionResult.success(session.userPoolTokens.value?.idToken).value
            // here we get the updated token
            GlobalScope.launch(Dispatchers.Main) {
                sharedPrefsHelper.put(
                    SharedPrefConstant.ACCESS_TOKEN, "${AppConstant.BEARER} $userIdToken"
                )
            }
        }, { Log.e("AuthQuickStart", "Failed to fetch session", it) })

        return sharedPrefsHelper[SharedPrefConstant.ACCESS_TOKEN, ""]
    }
}