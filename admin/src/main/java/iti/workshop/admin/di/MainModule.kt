package iti.workshop.admin.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import iti.workshop.admin.data.remote.retrofit.CouponAPICalls
import iti.workshop.admin.data.remote.retrofit.InventoryAPICalls
import iti.workshop.admin.data.remote.retrofit.ProductAPICalls
import iti.workshop.admin.data.repository.*
import iti.workshop.admin.data.room.ProductDao
import iti.workshop.admin.data.room.RoomDB
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

//    @Singleton
//    @Provides
//    fun provideAppDatabase(@ApplicationContext appContext: Context): ProductDao {
//        return RoomDB.invoke(appContext).productDao()
//    }

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
    fun provideProductRepository(/*productDao: ProductDao*/):IProductRepository{
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