package iti.workshop.admin.data.remote

import iti.workshop.admin.data.remote.retrofit.CouponCallApi
import iti.workshop.admin.data.remote.retrofit.InventoryCallApi
import iti.workshop.admin.data.remote.retrofit.ProductCallApi

class RemoteDataSource(
    private val couponCallApi: CouponCallApi,
    private val productCallApi: ProductCallApi,
    private val inventoryCallApi: InventoryCallApi
) : IRemoteDataSource {

}