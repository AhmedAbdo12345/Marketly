package iti.mad.marketly.data.source.remote

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import iti.mad.marketly.data.model.customer.CustomerBody
import iti.mad.marketly.data.model.customer.CustomerResponse
import iti.mad.marketly.data.model.favourites.FavouriteResponse
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.source.remote.retrofit.ApiService
import iti.mad.marketly.data.model.productDetails.ProductDetails
import iti.mad.marketly.utils.Constants.FAVOURITE
import iti.mad.marketly.utils.Constants.USERS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await


class RemoteDataSource(
    private val api: ApiService,
) : IRemoteDataSource {
    override suspend fun registerUser(customerBody: CustomerBody): Flow<CustomerBody> = flow {
        emit(api.registerUser(customerBody))
    }

    override suspend fun loginWithEmail(email: String): Flow<CustomerResponse> = flow {
        emit(api.loginWithEmail(email))
    }

    override suspend fun getProductDetails(id: Long): Flow<ProductDetails> = flow {
        emit(api.getProductDetails(id))
    }

    override suspend fun addProductToFavourite(userID: String, product: Product): Flow<Unit> =
        flow {
            Firebase.firestore.collection(USERS).document(userID).collection(FAVOURITE)
                .document(product.id.toString()).set(product).await()
            emit(Unit)
        }

    override suspend fun isFavourite(product: Product, userID: String): Flow<Boolean> = flow {
        val q = Firebase.firestore.collection(USERS).document(userID).collection(FAVOURITE)
            .document(product.id.toString()).get().await()
        emit(q.exists())
    }

    override suspend fun getAllFavourite(userID: String): Flow<FavouriteResponse> = flow {
        val q = Firebase.firestore.collection(USERS).document(userID).collection(FAVOURITE).get()
            .await()
        val products = mutableListOf<Product>()
        Log.i("ppp",q.documents.get(0).data?.entries.toString())
        for (product in q.documents) {
            Log.i("ppp", product.toObject<Product>()?.body_html!!)
           Log.i("ppp",product.id)
            products.add(product.toObject<Product>()!!)

        }
        print(products)

        emit(FavouriteResponse(products))
    }


    override suspend fun getAllFavouriteIDS(userID: String): Flow<List<String>> = flow {
        val q = Firebase.firestore.collection(USERS).document(userID).collection(FAVOURITE).get()
            .await()
        val ids = mutableListOf<String>()
        for (item in q.documents) {
            ids.add(item.id)
        }
        emit(ids)
    }

    override suspend fun deleteFromFavourite(userID: String, product: Product): Flow<Unit> = flow {
        val q = Firebase.firestore.collection(USERS).document(userID).collection(FAVOURITE)
            .document(product.id.toString()).delete().await()
        emit(Unit)
    }

}