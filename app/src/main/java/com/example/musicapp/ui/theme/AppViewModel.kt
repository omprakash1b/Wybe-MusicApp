package com.example.musicapp.ui.theme

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.dataModel.Song
import com.example.musicapp.dataModel.categoryClass
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.storage
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AppViewModel : ViewModel() {

    private val _splashScreenVisiblity = MutableStateFlow(true)
    val splashScreenVisiblity:MutableStateFlow<Boolean>get() = _splashScreenVisiblity
    private val database = Firebase.database

    //user
    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user:MutableStateFlow<FirebaseUser?>get() = _user
    //setter for user
    fun setUser(user: FirebaseUser?){
        _user.value=user
    }

    private var splashScreenJob : Job = viewModelScope.launch {
        delay(5000)
        _splashScreenVisiblity.value = false
    }

    //Ui State
    sealed interface ListScreenUiState{
        data class Success(val list:List<Song>,val category: categoryClass):ListScreenUiState
        data object Loading:ListScreenUiState
        data object Error:ListScreenUiState
    }

    var listScreenUiState:ListScreenUiState by mutableStateOf(ListScreenUiState.Loading)

    sealed interface PlayerScreenUiState{
        data class Success(val song: Song):PlayerScreenUiState
        data object Loading:PlayerScreenUiState
    }
    var playerScreenUiState:PlayerScreenUiState by mutableStateOf(PlayerScreenUiState.Loading)


    fun addSongToDatabase(song:Song){
        val songRef = database.getReference("categories").child("hindi")
            .child("songs")

        songRef.push().setValue(song)

    }

    fun setPlayerUiState(song: Song){
        playerScreenUiState = PlayerScreenUiState.Success(song = song)
    }

    fun uploadSongToStorage(
        uri: Uri?,
        title:String,
        context: Context
    ):String{
        val storageRef = Firebase.storage.reference
        val ref = storageRef.child("songs").child("$title.mp3")
        try {
            viewModelScope.launch {
                if (uri != null) {
                    ref.putFile(uri).await()
                }
            }
            return ref.storage.toString()
        }catch (e:Exception){
            Toast.makeText(context,e.message.toString(),Toast.LENGTH_SHORT).show()
        }
        return ""
    }


    fun readCategorySongFromDatabase(category:categoryClass){
        val ref = database.getReference("categories").child(category.name.lowercase()).child("songs")

        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list: MutableList<Song> = mutableListOf()
                for (child in snapshot.children){
                    val data = child.getValue(Song::class.java)
                    data?.let {
                        list += it
                    }
                }
                listScreenUiState = ListScreenUiState.Success(list = list, category = category)
            }

            override fun onCancelled(error: DatabaseError) {
                listScreenUiState = ListScreenUiState.Error
            }

        })

    }

    init {
        splashScreenJob.start()
    }

}