package iti.mad.marketly

import android.content.Context
import iti.mad.marketly.data.repository.authRepository.AuthRepositoryImpl
import iti.mad.marketly.data.repository.brands.BrandsRepo
import iti.mad.marketly.data.repository.brands.BrandsRepoImpl
import iti.mad.marketly.data.repository.category.CategoryRepo
import iti.mad.marketly.data.repository.category.CategoryRepoImpl
import iti.mad.marketly.data.repository.categoryProduct.CategoryProductRepo
import iti.mad.marketly.data.repository.categoryProduct.CategoryProductRepoImpl
import iti.mad.marketly.data.source.remote.RemoteDataSource
import iti.mad.marketly.data.source.remote.retrofit.RetrofitInstance

object AppDependencies {
    lateinit var authRepository: AuthRepositoryImpl
    lateinit var categoryRepo: CategoryRepo
    lateinit var categoryProductRepo: CategoryProductRepo
    lateinit var  brandsRepo: BrandsRepo
    fun initialization(appContext: Context) {
        val api = RetrofitInstance.api
        val remote = RemoteDataSource(api)
        authRepository = AuthRepositoryImpl(remote)
        categoryRepo = CategoryRepoImpl(api)
        categoryProductRepo = CategoryProductRepoImpl(api)
         brandsRepo = BrandsRepoImpl(api)
    }
}