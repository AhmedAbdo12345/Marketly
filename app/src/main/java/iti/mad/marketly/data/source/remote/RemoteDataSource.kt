package iti.mad.marketly.data.source.remote

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import iti.mad.marketly.data.model.cart.CartModel
import iti.mad.marketly.data.model.customer.CustomerBody
import iti.mad.marketly.data.model.customer.CustomerResponse
import iti.mad.marketly.data.model.favourites.FavouriteResponse
import iti.mad.marketly.data.model.order.OrderModel
import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.model.productDetails.ProductDetails
import iti.mad.marketly.data.model.settings.Address
import iti.mad.marketly.data.model.settings.CurrencyResponse
import iti.mad.marketly.data.source.remote.retrofit.ApiService
import iti.mad.marketly.utils.Constants
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

    override suspend fun getAllCartProducts(): Flow<List<CartModel>> = flow {
        val db = Firebase.firestore.collection("cart").document(SettingsManager.getDocumentID())
            .collection("CartProduct").get().await()

        val cartResponse: MutableList<CartModel> = mutableListOf()
        for (items in db.documents) {
            cartResponse.add(
                CartModel(
                    items.get("id") as Long,
                    items.get("imageURL") as String,
                    items.get("quantity") as Long,
                    items.get("price") as Double,
                    items.get("title") as String
                )
            )
        }

        emit(cartResponse)
    }

    override fun deleteAddress(addressID: String) {
        val db = Firebase.firestore
        db.collection("settings").document(SettingsManager.getDocumentID())
            .collection("Addresses").document(addressID).delete().addOnSuccessListener {
                Log.i("DeleteAddress", "deleteAddress: DELETED")
            }.addOnFailureListener {
                Log.i("DeleteAddress", "deleteAddress: ${it.localizedMessage}")
            }
    }

    override fun saveAddress(address: Address) {
        val db = Firebase.firestore


        SettingsManager.fillAddress(address)


        db.collection("settings").document(SettingsManager.getDocumentID())
            .collection("Addresses").document(address.AddressID).set(address).addOnSuccessListener {
                Log.i("FireBassSuccess", "saveAddress: Data Saved")
            }.addOnFailureListener {
                Log.i("FireBassFailure", "saveAddress:${it.localizedMessage}")
            }
    }

    override fun saveCartProduct(cartModel: CartModel) {
        val db = Firebase.firestore
        db.collection("cart").document(SettingsManager.getDocumentID())
            .collection("CartProduct").document(cartModel.id.toString()).set(cartModel)
            .addOnSuccessListener {
                Log.i("FireBassSuccess", "saveProduct: Data Saved")
            }.addOnFailureListener {
                Log.i("FireBassFailure", "saveProduct:${it.message}")
            }
    }

    override fun deleteCartItem(cartID: String) {
        val db = Firebase.firestore
        db.collection("cart").document(SettingsManager.getDocumentID())
            .collection("CartProduct").document(cartID).delete().addOnSuccessListener {
                Log.i("DeleteCart", "deleteCart: DELETED")
            }.addOnFailureListener {
                Log.i("DeleteAddress", "deleteAddress: ${it.localizedMessage}")
            }
    }

    //-------------------------------------------------------------------------------
    override fun saveProductInOrder(orderModel: OrderModel) {
        val db = Firebase.firestore

        FirebaseAuth.getInstance().currentUser?.let {
            var collectionReference =
                db.collection("orders").document(it.email.toString()).collection("orderList")

            collectionReference.document(orderModel.orderID)
                .set(orderModel).addOnSuccessListener {
                    Log.i("zxcvb", "saveProduct: Data Saved")
                }.addOnFailureListener {
                    Log.i("zxcvb", "saveProduct:${it.localizedMessage}")
                }

        }
    }

    override suspend fun getAllOrders(): Flow<List<OrderModel>> = flow {
        FirebaseAuth.getInstance().currentUser?.let {
            val db =
                Firebase.firestore.collection("orders").document(it.email.toString())
                    .collection("orderList").get().await()

            val orderResponse: MutableList<OrderModel> = mutableListOf()
            for (items in db.documents) {
                val data = items.toObject<OrderModel>()
                /* if (data != null) {
                     orderResponse.add(OrderModel(
                         data.get("orderID") as String,
                         data.get("itemList") as List<CartModel>,
                         data.get("itemCount") as Int,
                         data.get("date") as String))
                 }*/
                orderResponse.add(data!!)
                Log.d("zxcv", "getAllOrders: " + data)
            }

            emit(orderResponse)
        }

    }
}

