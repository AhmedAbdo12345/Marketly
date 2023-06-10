package iti.mad.marketly

import android.content.Context
import iti.mad.marketly.data.repository.authRepository.AuthRepositoryImpl
import iti.mad.marketly.data.repository.brands.BrandsRepo
import iti.mad.marketly.data.repository.brands.BrandsRepoImpl
import iti.mad.marketly.data.repository.category.CategoryRepo
import iti.mad.marketly.data.repository.category.CategoryRepoImpl
import iti.mad.marketly.data.repository.categoryProduct.CategoryProductRepo
import iti.mad.marketly.data.repository.categoryProduct.CategoryProductRepoImpl
import iti.mad.marketly.data.repository.authRepository.IAuthRepository
import iti.mad.marketly.data.repository.favourite_repo.FavouriteRep
import iti.mad.marketly.data.repository.favourite_repo.IFavouriteRepo
import iti.mad.marketly.data.repository.productdetailsRepo.ProductDetailsRepository
import iti.mad.marketly.data.repository.productdetailsRepo.ProductDetailsRepositoryImpl
import iti.mad.marketly.data.source.remote.RemoteDataSource
import iti.mad.marketly.data.source.remote.retrofit.RetrofitInstance
import iti.mad.marketly.presentation.productdetails.viewmodel.ProductDetailsViewModel

object AppDependencies {
    lateinit var productDetailsRepository: ProductDetailsRepository
    lateinit var authRepository: AuthRepositoryImpl
    lateinit var categoryRepo: CategoryRepo
    lateinit var categoryProductRepo: CategoryProductRepo
    lateinit var brandsRepo: BrandsRepo
    lateinit var favouriteRep: IFavouriteRepo
    fun initialization() {
        val api = RetrofitInstance.api
        val remote = RemoteDataSource(api)
        authRepository = AuthRepositoryImpl(remote)
        productDetailsRepository = ProductDetailsRepositoryImpl(remote)
        categoryRepo = CategoryRepoImpl(api)
        categoryProductRepo = CategoryProductRepoImpl(api)
        brandsRepo = BrandsRepoImpl(api)
        favouriteRep= FavouriteRep(remote)
    }
}