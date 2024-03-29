package com.example.homepage.features.homeTab.favouriteWebPage.Repo

import androidx.lifecycle.MutableLiveData
import com.example.homepage.utils.models.FavouriteWebpageData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class FavouriteWebRepo {
    val auth = Firebase.auth
    val user = auth.currentUser!!.uid
    private val websiteReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("user-favouriteWebsites").child(user)

    @Volatile
    private var INSTANCE: FavouriteWebRepo? = null

    fun getInstance(): FavouriteWebRepo {
        return INSTANCE ?: synchronized(this) {

            val instance = FavouriteWebRepo()
            INSTANCE = instance
            instance
        }
    }

    fun loadWebsites(allWebsites: MutableLiveData<List<FavouriteWebpageData>>) {

        websiteReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                try {

                    val websiteList: List<FavouriteWebpageData> =
                        snapshot.children.map { dataSnapshot ->

                            dataSnapshot.getValue(FavouriteWebpageData::class.java)!!

                        }
                    allWebsites.postValue(websiteList)

                } catch (_: Exception) {

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }


        })
        websiteReference.keepSynced(true)
    }
}
