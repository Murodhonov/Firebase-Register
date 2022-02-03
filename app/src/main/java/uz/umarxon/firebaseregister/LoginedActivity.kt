package uz.umarxon.firebaseregister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_logined.*
import uz.umarxon.firebaseregister.databinding.ActivityLoginedBinding

class LoginedActivity : AppCompatActivity() {

    lateinit var binding:ActivityLoginedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser!!
        var number = "null"

        if (user.phoneNumber != null){
            number = user.phoneNumber.toString()
        }

        Picasso.get().load(user.photoUrl).into(binding.iv)

        binding.alltv.text = "Display name: ${user.displayName}\n\nEmail: ${user.email}\n\nIs anonymous: ${user.isAnonymous}\n\nMetadata: ${user.metadata}\n\nMultifactor: ${user.multiFactor}\n\nProvider data: ${user.providerData}\n\nTentant id:${user.tenantId}\n\nPhone number: ${number}\n\nProvider id: ${user.providerId}\n\nUID: ${user.uid}"

        binding.btnSignout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this,MainActivity::class.java))
        }

    }

    override fun onBackPressed() {
        finishAffinity()
    }
}