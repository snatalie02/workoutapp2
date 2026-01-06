package com.example.workoutapp.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// BAGIAN : SHARON
object ApiClient {

    // ✅ UNTUK EMULATOR ANDROID
    private const val BASE_URL = "http://10.0.2.2:3000/api/"
    //10.0.2.2 = Localhost dari perspective emulator Android
    //10.0.2.2:3000 = Port 3000 di laptop kamu

    // ✅ UNTUK DEVICE FISIK (ganti dengan IP komputer kamu)
    // private const val BASE_URL = "http://192.168.1.100:3000/api/"

    // ✅ UNTUK TESTING DI LOCALHOST (kalau pakai Chrome/Browser)
    // private const val BASE_URL = "http://localhost:3000/api/"
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder() // make the HTTP calls
        .addInterceptor(logging) // debugging in logcat
        .build()

    val instance: ApiService by lazy {
        // returns
        Retrofit.Builder() // Builds Retrofit
            .baseUrl(BASE_URL) // Connects to http://10.0.2.2:3000/api/
            .addConverterFactory(GsonConverterFactory.create()) // convert data body/head/etc kotlin <> json
            .client(client) // make HTTP
            .build() // ready to use retrofit
            .create(ApiService::class.java)
        // api service (@POST("login") suspend fun login(@Body body: LoginRequest): TokenResponse) just a string no real connection
        // so it tells retrofit to create real @POST/@GET,etc so that apiservice can call the backend
        // returns : ApiService (with working @POST/@GET/@PUT,etc)
    }
}
