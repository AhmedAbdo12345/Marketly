package iti.workshop.admin.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import iti.workshop.admin.data.remote.retrofit.CouponAPICalls
import iti.workshop.admin.data.remote.retrofit.InventoryAPICalls
import iti.workshop.admin.data.remote.retrofit.ProductAPICalls
import iti.workshop.admin.data.repository.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {


    @Provides
    @Singleton
    fun provideCouponRepository(): ICouponRepository {
        return ImplCouponRepository(CouponAPICalls())
    }

    @Provides
    @Singleton
    fun provideInventoryRepository(): IInventoryRepository {
        return ImplInventoryRepository(InventoryAPICalls())
    }

    @Provides
    @Singleton
    fun provideProductRepository():IProductRepository{
        return ImplProductRepository(ProductAPICalls())
    }

    @Provides
    @Singleton
    fun provideMainRepository(
        couponRepository: ICouponRepository,
        inventoryRepository: IInventoryRepository,
        productRepository: IProductRepository
    ): Repository {
        return Repository(
            couponRepository = couponRepository,
            inventoryRepository = inventoryRepository,
            productRepository = productRepository
        )
    }
}