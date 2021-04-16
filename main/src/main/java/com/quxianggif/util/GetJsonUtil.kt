package com.quxianggif.util

import android.app.Application
import android.content.Context
import android.content.res.AssetManager
import com.google.gson.Gson
import com.quxianggif.core.model.ColorList
import java.io.*

/**
 * author jingting
 * date : 2021/4/15下午5:02
 */
object GetJsonUtil {

    fun getJsonString(context: Context, fileName: String) : String{
        val stringBuilder = StringBuilder()
        try {
            val assetManager: AssetManager = context.assets

            val isr = InputStreamReader(assetManager.open(fileName))
            val bf = BufferedReader(isr)
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            bf.close()
            isr.close()
            return stringBuilder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }


    fun jsonToObject(string: String, type: Class<ColorList>) : ColorList{
        val gson = Gson();
        return gson.fromJson(string, type)
    }
}