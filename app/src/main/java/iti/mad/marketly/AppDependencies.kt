package iti.mad.marketly

import android.content.Context
import iti.mad.marketly.data.repository.adsrepo.AdsRepoImplementation
import iti.mad.marketly.data.repository.authRepository.AuthRepositoryImpl
import iti.mad.marketly.data.repository.brands.BrandsRepo
import iti.mad.marketly.data.repository.brands.BrandsRepoImpl
import iti.mad.marketly.data.repository.category.CategoryRepo
import iti.mad.marketly.data.repository.category.CategoryRepoImpl
import iti.mad.marketly.data.repository.settings.SettingsRepoImplementation

import iti.mad.marketly.data.repository.authRepository.IAuthRepository
import iti.mad.marketly.data.repository.cart.CartRepoImplementation
import iti.mad.marketly.data.repository.productRepository.ProductRepo
import iti.mad.marketly.data.repository.productRepository.ProductRepoImpl
import iti.mad.marketly.data.repository.favourite_repo.FavouriteRep
import iti.mad.marketly.data.repository.favourite_repo.IFavouriteRepo
import iti.mad.marketly.data.repository.productdetailsRepo.ProductDetailsRepository
import iti.mad.marketly.data.repository.productdetailsRepo.ProductDetailsRepositoryImpl
import iti.mad.marketly.data.source.remote.RemoteDataSource
import iti.mad.marketly.data.source.remote.retrofit.RetrofitInstance
import iti.mad.marketly.presentation.brandProduct.BrandProductViewModel
import iti.mad.marketly.presentation.productdetails.viewmodel.ProductDetailsViewModel

object AppDependencies {
    lateinit var productDetailsRepository: ProductDetailsRepository
    lateinit var authRepository: AuthRepositoryImpl
    lateinit var categoryRepo: CategoryRepo
    lateinit var settingsRepo: SettingsRepoImplementation
    lateinit var brandsRepo: BrandsRepo
    lateinit var adsRepoImplementation: AdsRepoImplementation
    lateinit var favouriteRep: IFavouriteRepo
    lateinit var productRepo: ProductRepo
    lateinit var cartRepoImplementation: CartRepoImplementation
    fun initialization() {
        val api = RetrofitInstance.api
        val remote = RemoteDataSource(api)
        val currencyRetrofit= RetrofitInstance.currncyApi
        val remoteCurrency= RemoteDataSource(currencyRetrofit)

        settingsRepo = SettingsRepoImplementation(remoteCurrency)
        authRepository = AuthRepositoryImpl(remote)
        productDetailsRepository = ProductDetailsRepositoryImpl(remote)
        categoryRepo = CategoryRepoImpl(api)
        brandsRepo = BrandsRepoImpl(api)
        adsRepoImplementation=AdsRepoImplementation(api)
        favouriteRep= FavouriteRep(remote)
        productRepo= ProductRepoImpl(api)
        cartRepoImplementation= CartRepoImplementation(remote)
    }

}