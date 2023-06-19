package iti.workshop.admin.data.remote.remoteDataSource

import iti.workshop.admin.data.remote.retrofit.RetrofitInstance
import iti.workshop.admin.data.remote.retrofit.product.ProductCallApi
import iti.workshop.admin.data.remote.retrofit.product.ProductImageCallApi
import iti.workshop.admin.data.remote.retrofit.product.ProductVariantCallApi

data class ProductAPICalls(
    // Product
    val productCallApi: ProductCallApi = RetrofitInstance.productCallApi,
    val productImageCallApi: ProductImageCallApi = RetrofitInstance.productImageCallApi,
    val productVariantCallApi: ProductVariantCallApi = RetrofitInstance.productVariantCallApi
)