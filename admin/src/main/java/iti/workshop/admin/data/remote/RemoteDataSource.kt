package iti.workshop.admin.data.remote

import iti.workshop.admin.data.remote.retrofit.APICalls
import iti.workshop.admin.data.remote.retrofit.coupon.DiscountCodeCallApi
import iti.workshop.admin.data.remote.retrofit.inventory.InventoryItemCallApi
import iti.workshop.admin.data.remote.retrofit.product.ProductCallApi

class RemoteDataSource(
    private val api: APICalls = APICalls()
) : IRemoteDataSource {
}