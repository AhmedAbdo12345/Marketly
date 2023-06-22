package iti.workshop.admin.data.remote.firestore

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot
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
            .document(user.email?:"Unkown")
            .set(user)
            .addOnCompleteListener(onCompleteListener)
    }

    fun deleteUser(user: User, onCompleteListener: OnCompleteListener<Void?>,onError:(message:String)->Unit) {
        user.email?.let {
            firebaseFirestore
                .collection(ROOT_KEY)
                .document(it)
                .delete()
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener {
                    onError(it.message?:"Error Happened")
                }
        }

    }



    /*
     public void deletePlane(MealPlan mealPlan){
        if (sharedManager.isUser())
            firebaseFirestore
                    .collection(ROOT_KEY)
                    .document(sharedManager.getUser().getUID())
                    .collection(PLANE_KEY).document(mealPlan.getIdMeal()).delete();

    }
     */

    fun getUser(uid: String?, onSuccessListener: OnSuccessListener<DocumentSnapshot>) {
        uid?.let {
            firebaseFirestore
                .collection(ROOT_KEY)
                .document(it)
                .get().addOnSuccessListener(onSuccessListener)
        }

    }

    fun getUsers(usersCallBack:(users:List<User>)->Unit) {
            firebaseFirestore
                .collection(ROOT_KEY)
                .get().addOnSuccessListener {
                    val users = mutableListOf<User>()
                    for (item in it ){
                        val user:User = item.toObject(User::class.java)
                        users.add(user)
                    }
                    usersCallBack(users)
                }
    }





}