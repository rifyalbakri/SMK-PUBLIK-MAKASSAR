package com.smk.publik.makassar.presentation.viewmodel

import androidx.datastore.core.DataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.ToastUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.smk.publik.makassar.datastore.User
import com.smk.publik.makassar.domain.UserState
import com.smk.publik.makassar.domain.Users
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
    private val mUserDataStore: DataStore<User?>
) : ViewModel() {

    private val mAuth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private val mRealtimeDatabase: DatabaseReference by lazy {
        Firebase.database.reference
    }

    private val _user: MutableLiveData<User?> = MutableLiveData()
    val mUser: LiveData<User?> get() = _user

    fun getUser() {
        viewModelScope.launch {
            mUserDataStore.data.catch {
                ToastUtils.showShort(it.message)
            }.collect {
                _user.postValue(it)
            }
        }
    }

    private val _newUserData: MutableLiveData<User?> = MutableLiveData()
    val mNewUserData: LiveData<User?> get() = _newUserData

    fun updateUser(user: User?) {
        viewModelScope.launch {
            _newUserData.postValue(mUserDataStore.updateData { user })
        }
    }

    private val _register: MutableLiveData<UserState.Register> = MutableLiveData()
    val mRegister: LiveData<UserState.Register> get() = _register

    fun registerNew(email: String, pass: String) {
        _register.postValue(UserState.Register.Loading)
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCanceledListener {
            _register.postValue(UserState.Register.Cancelled)
        }.addOnSuccessListener {userResult ->
            viewModelScope.launch {
                mUserDataStore.updateData { it?.toBuilder()
                    ?.setUserId(userResult.user?.uid.toString())
                    ?.setUsername(email)
                    ?.build()
                }
            }
            mRealtimeDatabase.child("users").child(userResult.user?.uid.toString()).setValue(Users(
                email, pass
            )).addOnSuccessListener {
                _register.postValue(UserState.Register.Success(userResult.user))
            }.addOnFailureListener {
                _register.postValue(UserState.Register.Failed(it))
            }
        }.addOnFailureListener {
            _register.postValue(UserState.Register.Failed(it))
        }
    }


    private val _userData: MutableLiveData<UserState.Data> = MutableLiveData()
    val mUserData: LiveData<UserState.Data> get() = _userData

    fun getUserData(userId: String) {
        mRealtimeDatabase.child("users").child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _userData.postValue(UserState.Data.Success(snapshot.getValue<Users>()))
            }
            override fun onCancelled(error: DatabaseError) {
                _userData.postValue(UserState.Data.Failed(error.toException()))
            }
        })
    }
}