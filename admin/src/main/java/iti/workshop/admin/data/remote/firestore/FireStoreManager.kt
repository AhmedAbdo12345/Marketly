package iti.workshop.admin.data.remote.firestore

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import iti.workshop.admin.presentation.features.auth.model.User

object FireStoreManager {
    const val ROOT_KEY = "USERS"
    private val firebaseFirestore: FirebaseFirestore by lazy {
        val settings = FirebaseFirestoreSettings.Builder().build()
        FirebaseFirestore.getInstance().apply { firestoreSettings = settings }
    }

    fun saveUser(user: User, onCompleteListener: OnCompleteListener<Void?>) {
        firebaseFirestore
            .collection(ROOT_KEY)
            .document(user.id?:"NuKnown")
            .set(user)
            .addOnCompleteListener(onCompleteListener)
    }

}