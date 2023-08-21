package com.example.smstoemail.GoogleSignIn

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.media.Image
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.recreate
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.smstoemail.GoogleSignIn.GoogleSignInUtils.generateColorArray
import com.example.smstoemail.Interfaces.ApiService
import com.example.smstoemail.R
import com.example.smstoemail.Utils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import okhttp3.ResponseBody
import org.json.JSONObject
import org.w3c.dom.Text
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
    private lateinit var signInButtonLayout: RelativeLayout
    private lateinit var sighInButton: ImageView
    private lateinit var sighOutButton: ImageView


    fun handleSignIn(context: Context) {

        val appCompatActivityContext = context as AppCompatActivity
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //  .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .requestScopes(Scope("https://www.googleapis.com/auth/gmail.compose"))
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
        signInButtonLayout = appCompatActivityContext.findViewById(R.id.googleSignInButtonLayout)
        sighInButton = appCompatActivityContext.findViewById(R.id.googleSignInButton)
        sighOutButton = appCompatActivityContext.findViewById(R.id.googleSignOutButton)

        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(context)
        val iconBackgroundColor = GoogleSignInUtils.generateColorArray(context, account)


        processSignInButton(context, account, iconBackgroundColor, gso)


    }


    private fun signInWithGoogle(context: Context) {
        val signInIntent = mGoogleSignInClient.signInIntent
        (context as Activity).startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun sightOutFromGoogle(context: Context, gso: GoogleSignInOptions) {
        val googleSignInClient: GoogleSignInClient =
            GoogleSignIn.getClient(context, gso) // Replace 'gso' with your GoogleSignInOptions

        googleSignInClient.signOut()
            .addOnCompleteListener(context as Activity) { task ->
                if (task.isSuccessful) {
                    // Sign out successful
                    // You can perform any necessary actions after sign-out here
                    Utils.showToast(context, "Signed Out Successfully")
                } else {
                    // Sign out failed
                    // You might want to handle errors here
                    Utils.showToast(context, "Signed Out Failed")
                }
            }
    }

    fun handleGoogleSignInResult(context: Context, completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val idToken = account?.idToken

            if (idToken != null) {
                //val baseUrl = "http://192.168.0.6:8000" // Your server's base URL
                val baseUrl = "http://localhost:8000"

                val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val service = retrofit.create(ApiService::class.java)
                val call = service.validateAndCreateUser(idToken)

                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                         //   Utils.showToast(context, "User creation Successful: ")
                         //   Log.d("User Creation in server", "Successful")
                            // User creation on the server was successful
                            // You can handle success here
                        } else {
                        //    Utils.showToast(context, "User creation Failed: ")
                      //      Log.d("User Creation in server", "Failed")
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
        val jsonFileName =
            "client_secret_615818751861-6rlvc6tm5libfni0khc2qqkpsorb0qlo.apps.googleusercontent.com.json" // Replace with your JSON file name
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


    private fun processSignInButton(context: Context, account: GoogleSignInAccount?, iconBackgroundColor: Int,
                                    gso:GoogleSignInOptions) {


        if (account == null) {
            sighInButton.visibility = View.VISIBLE
            sighOutButton.visibility = View.GONE
            signInButtonLayout.setOnClickListener {
                signInWithGoogle(context)
            }
        } else {
            sighInButton.visibility = View.GONE // Hide SignInButton

            val lastAccount: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(context)

            if (lastAccount != null) {
                val customIcon =
                    GoogleSignInUtils.generateGoogleAccountIcon(account, iconBackgroundColor)
                sighOutButton.setImageBitmap(customIcon)
                sighOutButton.visibility = View.VISIBLE // Show custom icon ImageView

            }
            processSignOutTab(context, account, iconBackgroundColor, gso)
        }
    }

    private fun processSignOutTab(context: Context, account: GoogleSignInAccount?, iconBackgroundColor: Int,
                                  gso:GoogleSignInOptions) {

        val appCompatActivityContext = context as AppCompatActivity

        val tabContent: RelativeLayout = appCompatActivityContext.findViewById(R.id.signOutTab)
        val outerTab: RelativeLayout =
            appCompatActivityContext.findViewById(R.id.SignOutOuterTab)
        val tabImage: ImageView =
            appCompatActivityContext.findViewById(R.id.signOutTabAccountImage)
        val tabName: TextView =
            appCompatActivityContext.findViewById(R.id.signOutTabAccountName)
        val tabEmail: TextView =
            appCompatActivityContext.findViewById(R.id.signOutTabAccountEmail)
        val signOutButtonLayout: RelativeLayout =
            appCompatActivityContext.findViewById(R.id.signOutButtonLayout)
        val closeSignOutTab: ImageView =
            appCompatActivityContext.findViewById(R.id.signOutTabClose)
        val signOutTabView: View = appCompatActivityContext.findViewById(R.id.signOutTabView)

        signInButtonLayout.setOnClickListener {

            tabContent.visibility = View.VISIBLE
            signOutTabView.visibility = View.VISIBLE
            val accountIcon =
                GoogleSignInUtils.generateGoogleAccountIcon(account, iconBackgroundColor)
            tabImage.setImageBitmap(accountIcon)
            tabImage.visibility = View.VISIBLE

            tabName.text = account?.displayName
            tabEmail.text = account?.email

            signOutButtonLayout.setOnClickListener {

                sightOutFromGoogle(context, gso)
                ActivityCompat.recreate(appCompatActivityContext)

            }

            closeSignOutTab.setOnClickListener {

                tabContent.visibility = View.INVISIBLE
                //  viewInTab.visibility = View.INVISIBLE
                //  tabImage.visibility = View.INVISIBLE
            }

            signOutTabView.setOnClickListener {

                tabContent.visibility = View.INVISIBLE
                // viewInTab.visibility = View.INVISIBLE
                //  tabImage.visibility = View.INVISIBLE

            }

            outerTab.setOnClickListener {


            }

        }
    }

}



