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
import iti.workshop.admin.data.remote.remoteDataSource.CouponAPICalls
import iti.workshop.admin.data.remote.remoteDataSource.InventoryAPICalls
import iti.workshop.admin.data.remote.remoteDataSource.ProductAPICalls
import iti.workshop.admin.data.repository.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): ILocalDataSource {
        return LocalDataSource(RoomDB.invoke(appContext).productDao())
    }

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
    fun provideProductRepository(productDao: ILocalDataSource):IProductRepository{
        return ImplProductRepository(ProductAPICalls(),productDao)
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