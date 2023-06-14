package iti.mad.marketly.data.repository.cart

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.model.settings.AddressData
import iti.mad.marketly.data.source.remote.IRemoteDataSource
import iti.mad.marketly.utils.SettingsManager
import kotlinx.coroutines.flow.Flow

class CartRepoImplementation(val remote: IRemoteDataSource):CartRepoInterface {
    override fun saveCartProduct(product: Product) {
        val db = Firebase.firestore
        db.collection("cart").document(SettingsManager.getDocumentID())
            .collection("CartProduct").document(product.id.toString()).set(product).addOnSuccessListener {
                Log.i("FireBassSuccess", "saveProduct: Data Saved")
            }.addOnFailureListener {
                Log.i("FireBassFailure", "saveProduct:${it.message}")
            }
    }

    override suspend fun getAllCartProducts(): Flow<List<Product>> =remote.getAllCartProducts()
}