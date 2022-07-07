package com.example.netnew.util

import okhttp3.logging.HttpLoggingInterceptor.setLevel
import okhttp3.HttpUrl.host
import okhttp3.OkHttpClient.Builder.connectTimeout
import okhttp3.OkHttpClient.Builder.writeTimeout
import okhttp3.OkHttpClient.Builder.readTimeout
import okhttp3.OkHttpClient.Builder.addInterceptor
import okhttp3.OkHttpClient.Builder.cookieJar
import okhttp3.OkHttpClient.Builder.build
import okhttp3.Request.Builder.url
import okhttp3.Request.Builder.build
import okhttp3.FormBody.Builder.add
import okhttp3.FormBody.Builder.build
import okhttp3.Request.Builder.post
import okhttp3.MultipartBody.Builder.setType
import okhttp3.MultipartBody.Builder.addFormDataPart
import okhttp3.MultipartBody.Builder.build
import okhttp3.OkHttpClient.newCall
import okhttp3.Call.enqueue
import okhttp3.Response.code
import okhttp3.Response.body
import okhttp3.Response.isSuccessful
import okhttp3.ResponseBody.string
import okhttp3.Request.Builder.addHeader
import com.example.netnew.util.OkHttpTool.ResponseCallback
import com.example.netnew.util.OkHttpTool
import okhttp3.logging.HttpLoggingInterceptor
import android.graphics.Bitmap
import android.util.Log
import com.squareup.okhttp.Request
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.BitmapCallback
import okhttp3.*
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.lang.StringBuilder
import java.nio.charset.Charset
import java.util.ArrayList
import java.util.HashMap
import java.util.concurrent.TimeUnit
import kotlin.Throws

/**
 * 便于使用okhttp3的工具类
 */
class OkHttpTool {
    companion object {
        //日志标志
        private const val TAG = "OkHttpTool"

        //OkHttpClient类
        private val myOkHttpClient: OkHttpClient? = null
        //================对外方法=====================
        /**
         * Get 请求
         * @param url{String} 请求地址
         * @param parameters{Map<String></String>, Object>} 请求参数
         * @param responseCallback{ResponseCallback} 请求回调
         */
        fun httpGet(url: String, parameters: Map<String, Any>, responseCallback: ResponseCallback) {
            val request = createGetRequest(url, parameters)
            doRequest(request, responseCallback)
        }

        /**
         * POST 请求
         * @param url{String} 请求地址
         * @param parameters{Map<String></String>, Object>} 请求参数
         * @param responseCallback{ResponseCallback} 请求回调
         */
        fun httpPost(
            url: String,
            parameters: Map<String, Any>?,
            responseCallback: ResponseCallback
        ) {
            val request = createPostRequest(url, parameters)
            doRequest(request, responseCallback)
        }

        /**
         * POST 请求 JSON格式参数
         * @param url{String} 请求地址
         * @param json{String} JSON格式参数
         * @param responseCallback{ResponseCallback} 请求回调
         */
        fun httpPostJson(url: String, json: String, responseCallback: ResponseCallback) {
            val request = createPostRequestJson(url, json)
            doRequest(request, responseCallback)
        }

        /**
         * POST 请求 文件上传
         * @param url{String} 请求地址
         * @param parameters{Map<String></String>, Object>} 请求参数
         * @param files{Map<String></String>, File>} 上传的文件列表
         * @param responseCallback{ResponseCallback} 请求回调
         */
        fun httpPostWithFile(
            url: String,
            parameters: Map<String, Any>?,
            files: Map<String, File>?,
            responseCallback: ResponseCallback
        ) {
            val request = createPostRequestWithFile(url, parameters, files)
            doRequest(request, responseCallback)
        }

        /**
         * POST 请求 文件上传 byte数组
         * @param url{String} 请求地址
         * @param parameters{Map<String></String>, Object>} 请求参数
         * @param files{Map<String></String>, byte[]>}上传的文件列表
         * @param responseCallback{ResponseCallback} 请求回调
         */
        fun httpPostWithFileByte(
            url: String,
            parameters: Map<String, Any>?,
            files: Map<String, ByteArray>?,
            responseCallback: ResponseCallback
        ) {
            val request = createPostRequestWithFileByte(url, parameters, files)
            doRequest(request, responseCallback)
        }

        //===========私有方法===============
        //====构建请求====
        //构建 Get请求
        private fun createGetRequest(url: String, parameters: Map<String, Any>): okhttp3.Request {
            val urlBuilder = StringBuilder()
            urlBuilder.append(url)
            if (url.indexOf('?') <= -1) {
                //未拼接参数
                urlBuilder.append("?")
            }
            for ((key, value) in parameters) {
                urlBuilder.append("&")
                urlBuilder.append(key)
                urlBuilder.append("=")
                urlBuilder.append(value.toString())
            }
            return baseRequest.url(urlBuilder.toString()).build()
        }

        //构建 POST 请求
        private fun createPostRequest(url: String, parameters: Map<String, Any>?): okhttp3.Request {
            val builder = Builder(Charset.forName("UTF-8"))
            if (parameters != null) {
                for ((key, value) in parameters) {
                    builder.add(key, value.toString())
                }
            }
            val formBody: FormBody = builder.build()
            return baseRequest.url(url).post(formBody).build()
        }

        //构建 POST JSON格式参数请求
        private fun createPostRequestJson(url: String, json: String): okhttp3.Request {
            val mediaType: MediaType = parse.parse("application/json;charset=utf-8")
            val body: RequestBody = create.create(json, mediaType)
            return baseRequest.url(url).post(body).build()
        }

        //构建 POST带文件的 请求
        private fun createPostRequestWithFile(
            url: String,
            parameters: Map<String, Any>?,
            files: Map<String, File>?
        ): okhttp3.Request {
            // form 表单形式上传
            val requestBody: Builder = Builder().setType(MultipartBody.FORM)
            if (files != null) {
                for ((key, file) in files) {
                    if (file != null) {
                        // MediaType.parse() 里面是上传的文件类型。
                        val body: RequestBody =
                            create.create(file, parse.parse("application/octet-stream"))
                        val filename = file.name
                        // 参数分别为， 请求key ，文件名称 ， RequestBody
                        requestBody.addFormDataPart(key, filename, body)
                    }
                }
            }
            if (parameters != null) {
                // map 里面是请求中所需要的 key 和 value
                for ((key, value1) in parameters) {
                    val value = value1.toString()
                    requestBody.addFormDataPart(key, value)
                }
            }
            return baseRequest.url(url).post(requestBody.build()).build()
        }

        //构建 POST带文件的 请求
        private fun createPostRequestWithFileByte(
            url: String,
            parameters: Map<String, Any>?,
            files: Map<String, ByteArray>?
        ): okhttp3.Request {
            // form 表单形式上传
            val requestBody: Builder = Builder().setType(MultipartBody.FORM)
            if (files != null) {
                for ((key, file) in files) {
                    if (file != null) {
                        // MediaType.parse() 里面是上传的文件类型。
                        val body: RequestBody =
                            create.create(file, parse.parse("application/octet-stream"))
                        // 参数分别为， 请求key ，文件名称 ， RequestBody
                        requestBody.addFormDataPart(key, key, body)
                    }
                }
            }
            if (parameters != null) {
                // map 里面是请求中所需要的 key 和 value
                for ((key, value1) in parameters) {
                    val value = value1.toString()
                    requestBody.addFormDataPart(key, value)
                }
            }
            return baseRequest.url(url).post(requestBody.build()).build()
        }

        //===实际进行请求的方法
        private fun doRequest(request: okhttp3.Request, responseCallback: ResponseCallback) {
            //使用okhttp3的异步请求
            myOkHttpClient!!.newCall(request).enqueue(object : Callback {
                //失败回调
                override fun onFailure(call: Call, e: IOException) {
                    //回调
                    responseCallback.onResponse(false, -1, null, e)
                    //用于输出错误调试信息
                    if (e.message != null) {
                        Log.e(TAG, e.message!!)
                    }
                }

                //成功回调
                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    val responseCode = response.code //获取响应码
                    val responseBody = response.body //获取 ResponseBody
                    if (response.isSuccessful && responseBody != null) {
                        val strResponse = responseBody.string()
                        //回调
                        responseCallback.onResponse(true, responseCode, strResponse, null)
                    } else {
                        //回调
                        responseCallback.onResponse(false, responseCode, null, null)
                    }
                }
            })
        }

        //获取请求 指定client为Android
        private val baseRequest: Builder
            private get() {
                val builder = Builder()
                builder.addHeader("client", "Android")
                return builder
            }

        init {
            //========日志拦截器=========
            //Log拦截器
            val loggingInterceptor = HttpLoggingInterceptor(object : Logger() {
                override fun log(message: String) {
                    Log.i(TAG, message)
                }
            })
            //设置日志显示级别
            val level: Level = HttpLoggingInterceptor.Level.BODY
            com.example.netnew.util.loggingInterceptor.setLevel(com.example.netnew.util.level)

            //========cookie处理--让服务端记住app
            //这里是设置cookie的,但是并没有做持久化处理;只是把cookie保存在内存中
            val cookieJar: CookieJar = object : CookieJar {
                private val cookieStore = HashMap<String, List<Cookie>>()

                //保存cookie
                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    cookieStore[url.host] = cookies
                }

                //获取cookie
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    val cookies = cookieStore[url.host]
                    return cookies ?: ArrayList()
                }
            }

            //创建OkHttpClient
            myOkHttpClient = Builder()
                .connectTimeout(10, TimeUnit.SECONDS) //连接超时
                .writeTimeout(20, TimeUnit.SECONDS) //写入超时
                .readTimeout(20, TimeUnit.SECONDS) //读取超时
                .addInterceptor(com.example.netnew.util.loggingInterceptor) //添加日志处理拦截器
                .cookieJar(com.example.netnew.util.cookieJar)
                .build()
        }
    }

    /**
     * 获取图片  返回
     * @param url
     * @return Bitmap
     */
    fun getImage(url: String?) {
        var response: Bitmap
        OkHttpUtils
            .get() //
            .url(url) //
            .tag(this) //
            .build() //
            .connTimeOut(20000) //链接超时
            .readTimeOut(20000) //读取超时
            .writeTimeOut(20000) //写入超时
            .execute(object : BitmapCallback() {
                override fun onError(request: Request, e: Exception) {}
                override fun onResponse(response: Bitmap) {}
            })
    }

    //=====回调接口======
    interface ResponseCallback {
        fun onResponse(
            isSuccess: Boolean,
            responseCode: Int,
            response: String?,
            exception: Exception?
        )
    }
}