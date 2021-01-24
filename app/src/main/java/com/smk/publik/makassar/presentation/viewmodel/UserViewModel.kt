package com.smk.publik.makassar.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.smk.publik.makassar.data.repositories.UserRepository
import com.smk.publik.makassar.datastore.User
import com.smk.publik.makassar.domain.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * @Author Joseph Sanjaya on 20/12/2020,
 * @Company (PT. Solusi Finansialku Indonesia),
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/)
 */

class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {
    val isLoggedIn: Boolean get() = repository.firebaseAuth.currentUser != null

    private val _localUser: MutableLiveData<State<User?>> = MutableLiveData()
    val localUser: LiveData<State<User?>> get() = _localUser

    fun resetLocalUserState() = _login.postValue(State.Idle())
    fun getLocalUserData() {
        viewModelScope.launch {
            repository.getLocalUserData().catch { _localUser.postValue(State.Failed(it)) }
                .collect { _localUser.postValue(it) }
        }
    }

    private val _login: MutableLiveData<State<FirebaseUser?>> = MutableLiveData()
    val login: LiveData<State<FirebaseUser?>> get() = _login

    fun resetLoginState() = _login.postValue(State.Idle())
    fun login(email: String, password: String) = viewModelScope.launch {
        repository.login(email, password).catch { _login.postValue(State.Failed(it)) }
            .collect { _login.postValue(it) }
        delay(100)
    }

    private val _register: MutableLiveData<State<FirebaseUser?>> = MutableLiveData()
    val register: LiveData<State<FirebaseUser?>> get() = _register

    fun resetRegisterState() = _register.postValue(State.Idle())
    fun register(email: String, password: String) = viewModelScope.launch {
        repository.register(email, password).catch { _register.postValue(State.Failed(it)) }
            .collect { _register.postValue(it) }
    }

    private val _emailVerify: MutableLiveData<State<Boolean>> = MutableLiveData()
    val emailVerify: LiveData<State<Boolean>> get() = _emailVerify

    fun resetEmailVerifyState() = _emailVerify.postValue(State.Idle())
    fun sendEmailVerification() {
        viewModelScope.launch {
            repository.sendEmailVerification().catch { _emailVerify.postValue(State.Failed(it)) }
                .collect { _emailVerify.postValue(it) }
        }
    }

    private val _sendForgot: MutableLiveData<State<Boolean>> = MutableLiveData()
    val sendForgot: LiveData<State<Boolean>> get() = _sendForgot

    fun resetSendForgotState() = _sendForgot.postValue(State.Idle())
    fun sendForgotPassword(email: String) {
        viewModelScope.launch {
            repository.sendPasswordResetEmail(email).catch { _sendForgot.postValue(State.Failed(it)) }
                .collect { _sendForgot.postValue(it) }
        }
    }

    private val _verifyCodePassword: MutableLiveData<State<String>> = MutableLiveData()
    val verifyCodePassword: LiveData<State<String>> get() = _verifyCodePassword

    fun resetVerifyCodePasswordState() = _verifyCodePassword.postValue(State.Idle())
    fun verifyCodePassword(code: String) {
        viewModelScope.launch {
            repository.verifyPasswordResetCode(code).catch { _verifyCodePassword.postValue(State.Failed(it)) }
                .collect { _verifyCodePassword.postValue(it) }
        }
    }

    private val _changePassword: MutableLiveData<State<Boolean>> = MutableLiveData()
    val changePassword: LiveData<State<Boolean>> get() = _changePassword

    fun resetChangePasswordState() = _sendForgot.postValue(State.Idle())
    fun changePassword(code: String, email: String) {
        viewModelScope.launch {
            repository.changePassword(code, email).catch { _sendForgot.postValue(State.Failed(it)) }
                .collect { _sendForgot.postValue(it) }
        }
    }
}