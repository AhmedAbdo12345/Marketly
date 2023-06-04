package iti.workshop.admin.data.repository

data class Repository(
    val couponRepository: ICouponRepository,
    val inventoryRepository: IInventoryRepository,
    val productRepository: IProductRepository
)
