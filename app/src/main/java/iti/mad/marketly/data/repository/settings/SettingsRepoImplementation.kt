package iti.mad.marketly.data.repository.settings

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import iti.mad.marketly.data.model.settings.Address
import iti.mad.marketly.data.model.settings.AddressData
import iti.mad.marketly.data.model.settings.CurrencyResponse
import iti.mad.marketly.data.source.remote.IRemoteDataSource
import iti.mad.marketly.utils.Constants
import iti.mad.marketly.utils.SettingsManager
import kotlinx.coroutines.flow.Flow

class SettingsRepoImplementation(val remote: IRemoteDataSource):SettingsRepoInterface {
    override suspend fun getExchangeRate(): Flow<CurrencyResponse> = remote.getExchangeRate()

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

    override suspend fun getAllAddresses(): Flow<List<Address>> = remote.getAllAddresses()
    override fun deleteAddress(addressID: String) {
        val db = Firebase.firestore
        db.collection("settings").document(SettingsManager.getDocumentID())
            .collection("Addresses").document(addressID).delete().addOnSuccessListener {
                Log.i("DeleteAddress", "deleteAddress: DELETED")
            }.addOnFailureListener {
                Log.i("DeleteAddress", "deleteAddress: ${it.localizedMessage}")
            }
    }

}