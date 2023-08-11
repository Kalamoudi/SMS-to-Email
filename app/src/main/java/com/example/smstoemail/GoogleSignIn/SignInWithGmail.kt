package com.example.smstoemail.GoogleSignIn

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.smstoemail.Interfaces.ApiService
import com.example.smstoemail.R
import com.example.smstoemail.Utils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignInWithGmail {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    private val REQ_ONE_TAP = 9002
    private var showOneTapUI = true


    fun handleSignIn(context: Context){

        val appCompatActivityContext = context as AppCompatActivity
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //  .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso)

        val signInButtonLayout: RelativeLayout = appCompatActivityContext.findViewById(R.id.googleSignInButtonLayout)
        signInButtonLayout.setOnClickListener {
            signInWithGoogle(context)
        }

        //  validateGoogleIdToken(getString(R.string.default_web_client_id), this)

//        val signInButton: ImageView = findViewById(R.id.googleSignInButton)
//        signInButton.setOnClickListener{
//            val intent = Intent(this, SignInActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//            startActivity(intent)
//
//        }


    }

    private fun signInWithGoogle(context: Context) {
        val signInIntent = mGoogleSignInClient.signInIntent
        (context as Activity).startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    fun handleGoogleSignInResult(context: Context, completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val idToken = account?.idToken

            if (idToken != null) {
                val baseUrl = "http://192.168.0.6:8000" // Your server's base URL

                val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val service = retrofit.create(ApiService::class.java)
                val call = service.validateAndCreateUser(idToken)

                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            Utils.showToast(context, "User creation Successful: ")
                            Log.d("User Creation in server", "Successful")
                            // User creation on the server was successful
                            // You can handle success here
                        } else {
                            Utils.showToast(context, "User creation Failed: ")
                            Log.d("User Creation in server", "Failed")
                            // Handle server error
                            // You can parse response.errorBody() for details
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // Handle network failure
                    }
                })
            }
            val currAccount = GoogleSignIn.getLastSignedInAccount(context)
            var userName: String? = ""
            if (account != null) {
                userName = currAccount?.email

            }
            Utils.showToast(context, "Signed in: $userName")
            Log.d("Google Sign In", "Successful")
        } catch (e: ApiException) {
            Utils.showToast(context, "Sign in failed")
            Log.e("Google Sign In", "Failed with status code: ${e.statusCode}", e)
            // Sign-in failed
            // Handle error
        }
    }

    private fun readClientIdFromAssets(context: Context): String? {
        val jsonFileName = "client_secret_615818751861-6rlvc6tm5libfni0khc2qqkpsorb0qlo.apps.googleusercontent.com.json" // Replace with your JSON file name
        return try {
            val inputStream = context.assets.open(jsonFileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            val jsonContent = String(buffer, Charsets.UTF_8)
            val jsonObject = JSONObject(jsonContent)
            return jsonObject.getString("client_id") // Make sure the key matches the one in your JSON file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun validateGoogleIdToken(idToken: String, context: Context) {
        val clientId = readClientIdFromAssets(context)

        if (clientId.isNullOrEmpty()) {
            Log.e(ContentValues.TAG, "Client ID not found")
            return
        }

        val verifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
            .setAudience(listOf(clientId))
            .build()

        val googleIdToken = verifier.verify(idToken)

        if (googleIdToken != null) {
            val payload = googleIdToken.payload
            val userId = payload.subject
            val email = payload.email
            // ... Handle other user information
        } else {
            Log.e(ContentValues.TAG, "Invalid token")
        }
    }


}