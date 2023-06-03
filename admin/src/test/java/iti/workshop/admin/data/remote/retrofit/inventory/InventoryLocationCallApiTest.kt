package iti.workshop.admin.data.remote.retrofit.inventory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import iti.workshop.admin.data.remote.retrofit.RetrofitInstance
import iti.workshop.admin.data.remote.retrofit.product.ProductCallApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class InventoryLocationCallApiTest{
    private lateinit var inventoryLocationCallApi: InventoryLocationCallApi

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initRetrofit() {
        inventoryLocationCallApi = RetrofitInstance.inventoryLocationCallApi
    }
}