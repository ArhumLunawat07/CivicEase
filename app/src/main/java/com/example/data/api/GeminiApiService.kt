package com.example.data.api

import com.example.BuildConfig
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class GeminiRequest(
  val contents: List<GeminiContent>,
  val systemInstruction: GeminiContent? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
  val role: String? = null,
  val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiPart(
  val text: String
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
  val candidates: List<GeminiCandidate>? = null,
  val error: GeminiError? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
  val content: GeminiContent? = null
)

@JsonClass(generateAdapter = true)
data class GeminiError(
  val message: String? = null,
  val code: Int? = null
)

interface GeminiApiService {
  @POST("v1beta/models/gemini-3.8-flash:generateContent")
  suspend fun generateContent(
    @Query("key") apiKey: String,
    @Body request: GeminiRequest
  ): GeminiResponse
}

data class ChatMessage(
  val id: String = java.util.UUID.randomUUID().toString(),
  val senderIsUser: Boolean,
  val text: String,
  val timestamp: Long = System.currentTimeMillis()
)

object GeminiClient {
  private const val BASE_URL = "https://generativelanguage.googleapis.com/"

  private val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(60, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS)
    .build()

  private val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

  val service: GeminiApiService by lazy {
    Retrofit.Builder()
      .baseUrl(BASE_URL)
      .client(okHttpClient)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .build()
      .create(GeminiApiService::class.java)
  }

  private const val SYSTEM_PROMPT = """
You are the CivicEase AI Scheme Advisor, an empathetic, highly knowledgeable civic guide helping Indian citizens understand government welfare schemes and public benefits.
Guidelines:
1. Explain eligibility and government procedures in simple, reassuring, plain English without bureaucratic jargon.
2. When citizens ask about required documents (e.g. Income Certificate, Ration Card, Domicile, EWS Certificate, DBT bank seeding), explain practical steps to obtain them (such as through local CSC, Tehsildar, or DigiLocker).
3. Emphasize that CivicEase is an informational tool and that final decisions depend on official rules. Refer citizens to official platforms like https://www.myscheme.gov.in/ and https://www.india.gov.in/ for official submissions.
4. Never ask the citizen for their Aadhaar number, bank account credentials, password, or OTP.
5. If uncertain about specific state quotas or evolving deadlines, advise the citizen to check their local District Collectorate or state portal rather than guessing.
6. Keep responses well-structured with bullet points and clear actionable takeaways.
"""

  suspend fun askSchemeAdvisor(history: List<ChatMessage>, newQuestion: String): Result<String> = withContext(Dispatchers.IO) {
    try {
      val apiKey = BuildConfig.GEMINI_API_KEY
      if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
        return@withContext Result.success(
          "Hello! I am your CivicEase Scheme Advisor. To enable live AI intelligence, please set your Gemini API key in the Secrets panel in AI Studio.\n\n" +
          "In the meantime, you can explore all verified scheme details, eligibility checklists, and official portal links directly within the app!"
        )
      }

      val contentsList = mutableListOf<GeminiContent>()

      // Add recent context (up to last 6 messages)
      val recentHistory = history.takeLast(6)
      for (msg in recentHistory) {
        contentsList.add(
          GeminiContent(
            role = if (msg.senderIsUser) "user" else "model",
            parts = listOf(GeminiPart(text = msg.text))
          )
        )
      }

      contentsList.add(
        GeminiContent(
          role = "user",
          parts = listOf(GeminiPart(text = newQuestion))
        )
      )

      val request = GeminiRequest(
        contents = contentsList,
        systemInstruction = GeminiContent(
          parts = listOf(GeminiPart(text = SYSTEM_PROMPT.trimIndent()))
        )
      )

      val response = service.generateContent(apiKey = apiKey, request = request)

      if (response.error != null) {
        return@withContext Result.failure(Exception(response.error.message ?: "Gemini API error"))
      }

      val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
      if (text.isNullOrBlank()) {
        Result.failure(Exception("No response received from advisor"))
      } else {
        Result.success(text.trim())
      }
    } catch (e: Exception) {
      Result.failure(e)
    }
  }
}
