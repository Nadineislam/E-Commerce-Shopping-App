package com.example.e_commerce.auth_feature.presentation.fragments

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.BuildConfig
import com.example.e_commerce.R
import com.example.e_commerce.auth_feature.presentation.viewmodel.RegisterViewModel
import com.example.e_commerce.core.extensions.showSnakeBarError
import com.example.e_commerce.core.fragments.BaseFragment
import com.example.e_commerce.core.utils.CrashlyticsUtils
import com.example.e_commerce.core.utils.RegisterException
import com.example.e_commerce.core.utils.Resource
import com.example.e_commerce.databinding.FragmentRegisterBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding, RegisterViewModel>() {

    private val callbackManager: CallbackManager by lazy { CallbackManager.Factory.create() }
    private val loginManager: LoginManager by lazy { LoginManager.getInstance() }

    override val viewModel: RegisterViewModel by viewModels()


    // ActivityResultLauncher for the sign-in intent
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignUpResult(task)
            } else {
                view?.showSnakeBarError(getString(R.string.google_sign_in_field_msg))
            }
        }

    override fun getLayoutResId(): Int = R.layout.fragment_register
    override fun init() {
        initViewModel()
        initListeners()
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            viewModel.registerState.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        progressDialog.show()
                    }

                    is Resource.Success -> {
                        progressDialog.dismiss()

                        showLoginSuccessDialog()
                    }

                    is Resource.Error -> {
                        progressDialog.dismiss()
                        val msg = resource.exception?.message ?: getString(R.string.generic_err_msg)
                        view?.showSnakeBarError(msg)
                    }
                }
            }
        }
    }

    private fun initListeners() {
        binding.signInTv.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.googleSignupBtn.setOnClickListener {
            registerWithGoogleRequest()
        }
        binding.facebookSignupBtn.setOnClickListener {
            registerWithFacebook()
        }

    }

    private fun signOut() {
        loginManager.logOut()
    }

    private fun isLoggedIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null && !accessToken.isExpired
    }

    private fun registerWithFacebook() {
        if (isLoggedIn()) signOut()
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                val token = result.accessToken.token
                firebaseAuthWithFacebook(token)
            }

            override fun onCancel() {
            }

            override fun onError(error: FacebookException) {
                val msg = error.message ?: getString(R.string.generic_err_msg)
                view?.showSnakeBarError(msg)
                logAuthIssueToCrashlytics(msg, "Facebook")
            }
        })

        loginManager.logInWithReadPermissions(
            this, callbackManager, listOf("email", "public_profile")
        )
    }

    private fun firebaseAuthWithFacebook(token: String) {
        viewModel.registerWithFacebook(token)
    }

    private fun registerWithGoogleRequest() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.clientServerId).requestEmail().requestProfile()
            .requestServerAuthCode(BuildConfig.clientServerId).build()

        val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        googleSignInClient.signOut()
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }


    private fun handleSignUpResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: Exception) {
            view?.showSnakeBarError(e.message ?: getString(R.string.generic_err_msg))
            val msg = e.message ?: getString(R.string.generic_err_msg)
            logAuthIssueToCrashlytics(msg, "Google")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        viewModel.signUpWithGoogle(idToken)
    }


    private fun logAuthIssueToCrashlytics(msg: String, provider: String) {
        CrashlyticsUtils.sendCustomLogToCrashlytics<RegisterException>(
            msg,
            CrashlyticsUtils.REGISTER_KEY to msg,
            CrashlyticsUtils.LOGIN_PROVIDER to provider,
        )
    }


    private fun showLoginSuccessDialog() {
        MaterialAlertDialogBuilder(requireActivity()).setTitle("Register Success")
            .setMessage("Your registration is complete. Please proceed to the login page to access your account.")
            .setPositiveButton(
                "OK"
            ) { dialog, _ ->
                dialog?.dismiss()
                findNavController().popBackStack()
            }.create().show()
    }
}