package com.example.e_commerce.auth_feature.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.e_commerce.R
import com.example.e_commerce.auth_feature.presentation.viewmodel.ForgetPasswordViewModel
import com.example.e_commerce.core.extensions.showSnakeBarError
import com.example.e_commerce.core.utils.ProgressDialog
import com.example.e_commerce.core.utils.Resource
import com.example.e_commerce.databinding.FragmentForgetPasswordBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgetPasswordFragment : BottomSheetDialogFragment() {

    private val progressDialog by lazy { ProgressDialog.createProgressDialog(requireActivity()) }

    private val viewModel: ForgetPasswordViewModel by viewModels()

    private var _binding: FragmentForgetPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            viewModel.forgetPasswordState.collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        progressDialog.show()
                    }

                    is Resource.Success -> {
                        progressDialog.dismiss()
                        showSentEmailSuccessDialog()
                    }

                    is Resource.Error -> {
                        progressDialog.dismiss()
                        val msg = state.exception?.message ?: getString(R.string.generic_err_msg)
                        view?.showSnakeBarError(msg)
                    }
                }
            }
        }
    }

    private fun showSentEmailSuccessDialog() {
        MaterialAlertDialogBuilder(requireActivity()).setTitle("Reset Password")
            .setMessage("We have sent you an email to reset your password. Please check your email.")
            .setPositiveButton(
                "OK"
            ) { dialog, _ ->
                dialog?.dismiss()
                this@ForgetPasswordFragment.dismiss()
            }.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
