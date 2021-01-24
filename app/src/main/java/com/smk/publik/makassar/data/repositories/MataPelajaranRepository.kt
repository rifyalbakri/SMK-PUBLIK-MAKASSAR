package com.smk.publik.makassar.data.repositories

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.smk.publik.makassar.domain.MataPelajaran
import com.smk.publik.makassar.domain.State
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.File


class MataPelajaranRepository(
        private val firebaseAuth: FirebaseAuth,
        private val databaseReference: DatabaseReference,
        private val storageReference: StorageReference,
) {

    suspend fun buatMataPelajaran(nama: String, deskripsi: String) = flow {
        emit(State.Loading())
        val push = databaseReference.child("mata_pelajaran").push()
        push.setValue(MataPelajaran.Detail(nama, deskripsi)).await()
        emit(State.Success(push.key))
    }

    fun uploadMateri(file: File)= callbackFlow<State<Uri>> {
        offer(State.Loading())
        val fileUri = Uri.fromFile(file)
        val ref = storageReference.child("materi")
        val uploadTask = ref.putFile(fileUri)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener {
            if (it.isSuccessful) {
                val downloadUri = offer(State.Success(it.result))
            } else {
                throw Throwable("Gagal upload file!")
            }
        }
    }

    suspend fun tambahMateri(idMatpel : String, kelas: String, materi: MataPelajaran.Materi) = flow {
        emit(State.Loading())
        val pushedMateri = databaseReference.child("materi").push()
        pushedMateri.setValue(materi)
        val push = databaseReference.child("mata_pelajaran").child(idMatpel).child("materi").child(kelas).push()
        push.setValue(MataPelajaran.Materi(id = pushedMateri.key, materi.judul)).await()
        emit(State.Success(push.key))
    }

    suspend fun getMateriByKelas(idMatpel : String, kelas: String) = callbackFlow<State<List<MataPelajaran.Materi>>> {
        offer(State.Loading())
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = snapshot.children.mapNotNull {
                    it.getValue(MataPelajaran.Materi::class.java)
                }
                offer(State.Success(result))
            }
            override fun onCancelled(error: DatabaseError) {
                offer(State.Failed(error.toException()))
            }
        }
        databaseReference.child("mata_pelajaran").child(idMatpel).child("materi").child(kelas).addListenerForSingleValueEvent(listener)
        awaitClose { databaseReference.removeEventListener(listener) }
    }

    fun getMataPelajaran() = callbackFlow<State<List<MataPelajaran>>> {
        offer(State.Loading())
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = snapshot.children.mapNotNull {
                    it.getValue(MataPelajaran::class.java)
                }
                offer(State.Success(result))
            }
            override fun onCancelled(error: DatabaseError) {
                offer(State.Failed(error.toException()))
            }
        }
        databaseReference.child("mata_pelajaran").addListenerForSingleValueEvent(listener)
        awaitClose { databaseReference.removeEventListener(listener) }
    }
}