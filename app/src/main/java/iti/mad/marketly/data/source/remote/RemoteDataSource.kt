package iti.mad.marketly.data.source.remote

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import iti.mad.marketly.data.model.customer.CustomerBody
import iti.mad.marketly.data.model.customer.CustomerResponse
import iti.mad.marketly.data.model.settings.CurrencyResponse
import iti.mad.marketly.data.model.favourites.FavouriteResponse
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.source.remote.retrofit.ApiService
import iti.mad.marketly.utils.Constants
import iti.mad.marketly.data.model.productDetails.ProductDetails
import iti.mad.marketly.data.model.settings.Address
import iti.mad.marketly.utils.Constants.FAVOURITE
import iti.mad.marketly.utils.Constants.USERS
import iti.mad.marketly.utils.SettingsManager
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

    override suspend fun getExchangeRate(): Flow<CurrencyResponse> = flow {
        emit(api.getExchangeRate(Constants.API_CUR_KEY))
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
        for (product in q.documents) {
            Log.i("ppp", product.toObject<Product>()?.body_html!!)
            Log.i("ppp", product.id)
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

    override suspend fun getAllAddresses(): Flow<List<Address>> = flow {
        val db = Firebase.firestore.collection("settings").document(SettingsManager.getDocumentID())
            .collection("Addresses").get().await()

        val addressResponse: MutableList<Address> = mutableListOf()
        for (items in db.documents) {
            addressResponse.add(
                Address(
                    items.get("addressID") as String,
                    items.get("country") as String,
                    items.get("city") as String,
                    items.get("street") as String
                )
            )
        }
        emit(addressResponse)
    }

    override suspend fun getAllCartProducts(): Flow<List<Product>> = flow {
        val db = Firebase.firestore.collection("cart").document(SettingsManager.getDocumentID())
            .collection("CartProduct").get().await()

        val cartResponse : MutableList<Product> = mutableListOf()
        for(items in db.documents){
            cartResponse.add(items.toObject(Product::class.java)!!)
        }
        emit(cartResponse)
    }
    }

