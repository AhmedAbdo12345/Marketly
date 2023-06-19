package iti.workshop.admin.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import iti.workshop.admin.data.local.ILocalDataSource
import iti.workshop.admin.data.local.LocalDataSource
import iti.workshop.admin.data.local.room.RoomDB
import iti.workshop.admin.data.remote.retrofit.RetrofitInstance
import iti.workshop.admin.data.repository.*
import iti.workshop.admin.domain.product.ProductUseCases
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): ILocalDataSource {
        return LocalDataSource(RoomDB.invoke(appContext).productDao())
    }

    @Singleton
    @Provides
    fun provideAppRetrofit(@ApplicationContext appContext: Context): RetrofitInstance {
        return RetrofitInstance(appContext)
    }


    @Provides
    @Singleton
    fun provideCouponRepository(retrofitInstance: RetrofitInstance): ICouponRepository {
        return ImplCouponRepository(retrofitInstance.CouponAPICalls())
    }


    @Provides
    @Singleton
    fun provideInventoryRepository(retrofitInstance: RetrofitInstance): IInventoryRepository {
        return ImplInventoryRepository(retrofitInstance.InventoryAPICalls())
    }

    @Provides
    @Singleton
    fun provideProductRepository(retrofitInstance: RetrofitInstance,productDao: ILocalDataSource):IProductRepository{
        return ImplProductRepository(retrofitInstance.ProductAPICalls(),productDao)
    }
    @Provides
    @Singleton
    fun provideProductUsecase(productRepo: IProductRepository): ProductUseCases {
        return ProductUseCases(productRepo)
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