package com.smf.customer.utility


object CountryCodeSeparator {

    fun getCountry(argStringArray: ArrayList<String>, argText: String): String {
        var countryCode = ""
        if (argText.length >= 4) {
            for (i in argStringArray.indices) {
                val g = argStringArray[i].split(",").toTypedArray()
                if (g[0] == getFirstFourChar(argText)) {
                    countryCode = g[0]
                    break
                }
                if (g[0] == getFirstThreeChar(argText)) {
                    countryCode = g[0]
                    break
                }
                if (g[0] == getFirstTwoChar(argText)) {
                    countryCode = g[0]
                    break
                }
            }
        }
        return countryCode
    }

    private fun getFirstFourChar(argText: String): String {
        val threeChar: String
        val text = argText
        threeChar = text.substring(0, 4)
        return threeChar
    }

    private fun getFirstThreeChar(argText: String): String {
        val twoChar: String
        val text = argText
        twoChar = text.substring(0, 3)
        return twoChar
    }

    private fun getFirstTwoChar(argText: String): String {
        val oneChar: String
        val text = argText
        oneChar = text.substring(0, 2)
        return oneChar
    }
}
