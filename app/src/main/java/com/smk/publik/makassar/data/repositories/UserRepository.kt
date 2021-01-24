package com.smk.publik.makassar.data.repositories

import androidx.datastore.core.DataStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.smk.publik.makassar.datastore.User
import com.smk.publik.makassar.domain.State
import com.smk.publik.makassar.domain.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await


/**
 * @Author Joseph Sanjaya on 31/12/2020,
 * @Github (https://github.com/JosephSanjaya}),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/)
 */

class UserRepository(
        private val userDataStore: DataStore<User?>,
        val firebaseAuth: FirebaseAuth,
        private val databaseReference: DatabaseReference
) {
    suspend fun getLocalUserData() = flow<State<User?>> {
        emit(State.Loading())
        userDataStore.data
            .catch { emit(State.Failed(it)) }
            .collect {
                emit(State.Success(it))
            }
    }.flowOn(Dispatchers.IO)

   suspend fun login(email: String, password: String) = flow {
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
       emit(State.Loading())
       userDataStore.updateData {
            it?.toBuilder()
                ?.setUserId(result.user?.uid.toString())
                ?.setUsername(email)
                ?.build()
        }
        emit(State.Success(result.user))
    }.catch {
        emit(State.Failed(it))
    }.flowOn(Dispatchers.IO)

    suspend fun register(email: String, password: String) = flow {
        emit(State.Loading())
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        databaseReference.child("users").child(result.user?.uid.toString()).setValue(
            Users(
                email, password
            )
        ).await()
        userDataStore.updateData {
            it?.toBuilder()
                ?.setUserId(result.user?.uid.toString())
                ?.setUsername(email)
                ?.build()
        }
        emit(State.Success(result.user))
    }.catch {
        emit(State.Failed(it))
    }.flowOn(Dispatchers.IO)

    suspend fun sendEmailVerification() = flow {
        emit(State.Loading())
        val result = firebaseAuth.currentUser?.sendEmailVerification()?.isSuccessful
            ?: throw Throwable("User tidak ditemukan, silahkan login terlebih dahulu!")
        if(!result) throw Throwable("Terjadi kesalahan, silahkan coba lagi!!")
        else emit(State.Success(result))
    }

    suspend fun sendPasswordResetEmail(email: String) = flow {
        emit(State.Loading())
        val result = firebaseAuth.sendPasswordResetEmail(email).isSuccessful
        emit(State.Success(result))
    }

    suspend fun verifyPasswordResetCode(code: String) = flow {
        emit(State.Loading())
        val verify = firebaseAuth.verifyPasswordResetCode(code).await()
        emit(State.Success(verify))
    }

    suspend fun changePassword(code: String, password: String) = flow {
        emit(State.Loading())
        val result = firebaseAuth.confirmPasswordReset(code, password).isSuccessful
        if(result) emit(State.Success(result))
        else throw Throwable("Terjadi kesalahan, silahkan coba lagi!")
    }

}