package com.smf.customer.utility

import com.google.gson.Gson
import com.smf.customer.app.base.BaseViewModel
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CustomErrorAPI @Inject constructor() {

     fun errorMessageFromAPI(httpException: HttpException): String? {
        var errorMessage: String? = null
        val error = httpException.response()?.errorBody()
        try {

            val adapter = Gson().getAdapter(BaseViewModel.ErrorResponse::class.java)
            val errorParser = adapter.fromJson(error?.string())
            errorMessage = errorParser.errorMessage
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            return errorMessage
        }
    }

}