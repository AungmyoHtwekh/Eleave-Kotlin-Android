package com.example.myapplication

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.yoma.yomahackathon.R
import com.yoma.yomahackathon.activity.AssociateLeaveActivity
import com.yoma.yomahackathon.adapters.DataAdpter
import com.yoma.yomahackathon.model.LoginData
import com.yoma.yomahackathon.model.LoginToken
import com.yoma.yomahackathon.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var edtUsername: TextInputEditText
    private lateinit var edtPassword: TextInputEditText
    private lateinit var progerssProgressDialog: ProgressDialog
    private lateinit var loginDataResponse: LoginToken
    private var PRIVATE_MODE = 0
    private lateinit var sharedPref: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtUsername = findViewById(R.id.username);
        edtPassword = findViewById(R.id.password);
        val btnLogin: Button = findViewById(R.id.btn_login);
        val btnBioLogin: Button = findViewById(R.id.btn_bio_login)
        progerssProgressDialog = ProgressDialog(this)
        progerssProgressDialog.setTitle("Logging in")
        progerssProgressDialog.setCancelable(false)
        sharedPref = getSharedPreferences(getString(R.string.login_data), PRIVATE_MODE)
        loginDataResponse = LoginToken()
        if (sharedPref.getBoolean(getString(R.string.biometric_login), false)) {
            btnBioLogin.visibility = View.VISIBLE
        }

        btnLogin.setOnClickListener {
            if (validate()) {
                progerssProgressDialog.show()
                authenticate(edtUsername.text.toString(), edtPassword.text.toString())
            }

        }

        btnBioLogin.setOnClickListener {
            authWithBiometric()
            biometricPrompt.authenticate(promptInfo)
        }

    }

    fun goToLeavePendingActivity() {
        intent = Intent(this, AssociateLeaveActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun authWithBiometric() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    progerssProgressDialog.show()
                    var username: String =
                        sharedPref.getString(getString(R.string.username), "").toString()
                    var password: String =
                        sharedPref.getString(getString(R.string.password), "").toString()
                    authenticate(username, password)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()

                }
            })
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Login")
            .setSubtitle("Authenticate with your biometrics.")
            .setNegativeButtonText("Use account password")
            .build()
    }

    fun validate(): Boolean {
        if (edtUsername.text.toString().length < 1) {
            edtUsername.setError("Please fill in this field.")
            return false
        }
        if (edtPassword.text.toString().length < 1) {
            edtPassword.setError("Please fill in this field.")
            return false
        }
        return true
    }

    private fun authenticate(username: String, password: String) {
        val call: Call<LoginToken> = ApiClient.getClient.authenticate(LoginData(username, password))
        call.enqueue(object : Callback<LoginToken> {
            override fun onFailure(call: Call<LoginToken>, t: Throwable) {
                progerssProgressDialog.dismiss()
            }

            override fun onResponse(call: Call<LoginToken>, response: Response<LoginToken>) {
                progerssProgressDialog.dismiss()
                if (response.code() == 200) {
                    loginDataResponse = response.body()!!
                    if (!loginDataResponse.accessToken.isEmpty()) {
                        sharedPref.edit().putString(
                            getString(R.string.access_token),
                            loginDataResponse.accessToken
                        ).apply()
                        if (!sharedPref.getBoolean(getString(R.string.biometric_login), false)) {
                            sharedPref.edit().putString(
                                getString(R.string.username),
                                edtUsername.text.toString()
                            ).apply()
                            sharedPref.edit().putString(
                                getString(R.string.password),
                                edtPassword.text.toString()
                            ).apply()
                        }
                        goToLeavePendingActivity()
                    }
                }
                if (response.code() == 401){
                    showDialog(getString(R.string.auth_fail))
                }
            }
        })
    }

    fun showDialog(errMsg: String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Login Failed!")
        builder.setMessage(errMsg)
        builder.setPositiveButton("TRY AGAIN"){dialog, which ->
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}
