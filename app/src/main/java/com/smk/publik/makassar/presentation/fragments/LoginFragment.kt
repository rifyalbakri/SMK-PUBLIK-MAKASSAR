package com.smk.publik.makassar.presentation.fragments

import ando.file.core.FileGlobal.OVER_LIMIT_EXCEPT_ALL
import ando.file.core.FileUtils
import ando.file.selector.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.afollestad.vvalidator.form
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.google.firebase.auth.FirebaseUser
import com.smk.publik.makassar.R
import com.smk.publik.makassar.databinding.FragmentLoginBinding
import com.smk.publik.makassar.domain.MataPelajaran
import com.smk.publik.makassar.interfaces.ActivityInterfaces
import com.smk.publik.makassar.interfaces.BaseOnClickView
import com.smk.publik.makassar.presentation.observer.MataPelajaranObserver
import com.smk.publik.makassar.presentation.observer.UserObserver
import com.smk.publik.makassar.presentation.viewmodel.MataPelajaranViewModel
import com.smk.publik.makassar.presentation.viewmodel.UserViewModel
import com.smk.publik.makassar.utils.inline.errorAnimation
import com.smk.publik.makassar.utils.inline.makeLoadingDialog
import com.smk.publik.makassar.utils.inline.showErrorToast
import com.smk.publik.makassar.utils.inline.showSuccessToast
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


/**
 * @Author Joseph Sanjaya on 27/12/2020,
 * @Github (https://github.com/JosephSanjaya}),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/)
 */

class LoginFragment : Fragment(R.layout.fragment_login), BaseOnClickView, UserObserver.Interfaces, MataPelajaranObserver.Interfaces {
    companion object {
        const val REQUEST_CHOOSE_FILE = 10
    }

    private val loading by lazy { requireContext().makeLoadingDialog(false) }
    private val binding by viewBinding(FragmentLoginBinding::bind)
    private var mActivityInterfaces: ActivityInterfaces? = null
    private val mViewModel: UserViewModel by viewModel()
    private val mMatpel: MataPelajaranViewModel by viewModel()

    private var mSelector: FileSelector? = null

    private val mValidator by lazy {
        form {
            input(binding.etEmail) {
                isEmail()
                isNotEmpty().description("Email tidak boleh kosong!")
            }.onErrors { _, errors ->
                binding.tlEmail.apply {
                    if (!errors.isNullOrEmpty()) errorAnimation()
                    error = errors.firstOrNull()?.description
                }
            }
            input(binding.etPassword) {
                isNotEmpty().description("Password tidak boleh kosong!")
            }.onErrors { _, errors ->
                binding.tlPassword.apply {
                    if (!errors.isNullOrEmpty()) errorAnimation()
                    error = errors.firstOrNull()?.description
                }
            }
        }
    }

    override fun onStart() {
        mActivityInterfaces?.onToolbarChanges("Masuk", false, isHide = false)
        super.onStart()
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.btnLogin -> if (mValidator.validate().success()) mViewModel.login(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
            binding.tvSignUp -> {
                mActivityInterfaces?.onFragmentChanges(
                    RegisterFragment(),
                    isBackstack = true,
                    isAnimate = true
                )
            }
            binding.tvForgot -> chooseFile()
        }
        super.onClick(p0)
    }

    private fun chooseFile() {
        PermissionUtils.permission(PermissionConstants.STORAGE).callback(object :
            PermissionUtils.SimpleCallback {
            override fun onGranted() {
                val optionsImage = FileSelectOptions().apply {
                    fileType = FileType.IMAGE
                    fileTypeMismatchTip = "文件类型不匹配"
                    singleFileMaxSize = 5242880
                    singleFileMaxSizeTip = "图片最大不超过5M！"
                    allFilesMaxSize = 10485760
                    allFilesMaxSizeTip = "总图片大小不超过10M！"//单选条件下无效,只做单个图片大小判断
                    fileCondition = object : FileSelectCondition {
                        override fun accept(fileType: IFileType, uri: Uri?): Boolean {
                            return (fileType == FileType.IMAGE && uri != null && !uri.path.isNullOrBlank() && !FileUtils.isGif(
                                uri
                            ))
                        }
                    }
                }
                mSelector = FileSelector.with(requireContext())
                    .setRequestCode(REQUEST_CHOOSE_FILE)
                    .setTypeMismatchTip("文件类型不匹配")
                    .setMinCount(1, "至少选一个文件!")
                    .setMaxCount(10, "最多选十个文件!")
                    .setOverLimitStrategy(OVER_LIMIT_EXCEPT_ALL)
                    .setSingleFileMaxSize(1048576, "大小不能超过1M！")
                    .setAllFilesMaxSize(10485760, "总大小不能超过10M！")
                    .setMimeTypes("image/*")
                    .applyOptions(optionsImage)
                    .filter(object : FileSelectCondition {
                        override fun accept(fileType: IFileType, uri: Uri?): Boolean {
                            return when (fileType) {
                                FileType.IMAGE -> (uri != null && !uri.path.isNullOrBlank() && !FileUtils.isGif(
                                    uri
                                ))
                                FileType.VIDEO -> false
                                FileType.AUDIO -> false
                                else -> false
                            }
                        }
                    })
                    .callback(object : FileSelectCallBack {
                        override fun onSuccess(results: List<FileSelectResult>?) {
                            if (!results.isNullOrEmpty()) {
                                mMatpel.uploadMateri(File(results[0].filePath ?: ""))
                            }
                        }
                        override fun onError(e: Throwable?) {
                            requireContext().showErrorToast(e?.message.toString())
                        }
                    }).choose()
            }

            override fun onDenied() {
                requireContext().showErrorToast("Mohon memberikan izin untuk mengakses penyimpanan anda, untuk dapat mengambil data!")
            }
        }).request()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mSelector?.obtainResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycle.addObserver(UserObserver(this, mViewModel, viewLifecycleOwner))
        viewLifecycleOwner.lifecycle.addObserver(
            MataPelajaranObserver(
                this,
                mMatpel,
                viewLifecycleOwner
            )
        )
        binding.listener = this
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onFetchMataPelajaranSuccess(data: List<MataPelajaran>) {
        requireContext().showSuccessToast(data.toString())
        super.onFetchMataPelajaranSuccess(data)
    }

    override fun onLoginIdle() {
        loading.second.dismiss()
        super.onLoginIdle()
    }

    override fun onLoginLoading() {
        loading.second.show()
        super.onLoginLoading()
    }

    override fun onLoginFailed(e: Throwable) {
        requireContext().showErrorToast(e.message.toString())
        mViewModel.resetLoginState()
        super.onLoginFailed(e)
    }

    override fun onLoginSuccess(user: FirebaseUser?) {
        requireContext().showSuccessToast("Success, login ${user?.uid}")
        mViewModel.resetLoginState()
        super.onLoginSuccess(user)
    }

    override fun onSendForgotPasswordLoading() {
        loading.second.show()
        super.onSendForgotPasswordLoading()
    }

    override fun onSendForgotPasswordSuccess() {
        loading.second.dismiss()
        requireContext().showSuccessToast("Success, silahkan cek email anda")
        super.onSendForgotPasswordSuccess()
    }

    override fun onSendForgotPasswordFailed(e: Throwable) {
        loading.second.dismiss()
        requireContext().showErrorToast(e.message.toString())
        super.onSendForgotPasswordFailed(e)
    }

    override fun onUploadMateriLoading() {
        loading.second.show()
        super.onUploadMateriLoading()
    }

    override fun onUploadMateriFailed(e: Throwable) {
        requireContext().showErrorToast(e.message.toString())
        super.onUploadMateriFailed(e)
    }

    override fun onUploadMateriSuccess(url: Uri) {
        requireContext().showSuccessToast("Success,$url")
        super.onUploadMateriSuccess(url)
    }

    override fun onAttach(context: Context) {
        if (context is ActivityInterfaces) mActivityInterfaces = context
        super.onAttach(context)
    }

    override fun onDetach() {
        if (loading.second.isShowing) loading.second.dismiss()
        mActivityInterfaces = null
        super.onDetach()
    }
}