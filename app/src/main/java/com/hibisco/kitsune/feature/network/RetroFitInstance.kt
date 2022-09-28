package com.hibisco.kitsune.feature.network

import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

object RetroFitInstance {
    private const val baseUrl = "http://10.0.2.2:8080/"
    // private const val baseUrl = "http://10.0.2.2:8080/"

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun getRetrofit(): API {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(NullOnEmptyConverterFactory())
            .build()

        return retrofit.create(API::class.java)
    }

    class NullOnEmptyConverterFactory : Converter.Factory() {
        override fun responseBodyConverter(
            type: Type,
            annotations: Array<Annotation>,
            retrofit: Retrofit
        ): Converter<ResponseBody, *> {
            val delegate: Converter<ResponseBody, *> =
                retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
            return Converter { body ->
                if (body.contentLength() == 0L) null else delegate.convert(
                    body
                )
            }
        }
    }

}