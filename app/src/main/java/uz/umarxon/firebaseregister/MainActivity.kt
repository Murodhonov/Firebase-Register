package uz.umarxon.firebaseregister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import uz.umarxon.firebaseregister.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding
    lateinit var googleSignInClient:GoogleSignInClient
    var RC_SIGN_IN = 1
    lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build())

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser!=null){
            startActivity(Intent(this,LoginedActivity::class.java))
        }else{


            binding.btnSign.setOnClickListener {

                signIn()

            }
        }

    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)

                //Picasso.get().load(account.photoUrl).into(binding.iv)

                //binding.tv.text = "Display name: ${account.displayName}\n\nEmail: ${account.email}\n\nFamily name: ${account.familyName}\n\nGiven Name: ${account.givenName}\n\nGranted Scope${account.grantedScopes}\n\nID:${account.id}\n\nID Token: ${account.idToken}\n\nIs Expired: ${account.isExpired}\n\nRequested Scope: ${account.requestedScopes}\n\nServer Auth Code: ${account.serverAuthCode}\n\n"
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
//                    updateUI(user)
                    startActivity(Intent(this,LoginedActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
//                    updateUI(null)
                    Toast.makeText(this, "Error \n${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
    override fun onBackPressed() {
        finishAffinity()
    }

}