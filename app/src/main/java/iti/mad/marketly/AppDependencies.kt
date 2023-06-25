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
import iti.mad.marketly.data.repository.draftorderrepo.DraftOrderRepoImplementation
import iti.mad.marketly.data.repository.productRepository.ProductRepo
import iti.mad.marketly.data.repository.productRepository.ProductRepoImpl
import iti.mad.marketly.data.repository.favourite_repo.FavouriteRep
import iti.mad.marketly.data.repository.favourite_repo.IFavouriteRepo
import iti.mad.marketly.data.repository.order.OrderRepo
import iti.mad.marketly.data.repository.order.OrderRepoImpl
import iti.mad.marketly.data.repository.productdetailsRepo.ProductDetailsRepository
import iti.mad.marketly.data.repository.productdetailsRepo.ProductDetailsRepositoryImpl
import iti.mad.marketly.data.repository.search.SearchRepo
import iti.mad.marketly.data.repository.search.SearchRepoImpl
import iti.mad.marketly.data.repository.stripe.StripeRepoImplementation
import iti.mad.marketly.data.source.remote.RemoteDataSource
import iti.mad.marketly.data.source.remote.StripeRemoteDataSource
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
    lateinit var orderRepo: OrderRepo
    lateinit var searchRepo: SearchRepo
    lateinit var draftOrderRepo : DraftOrderRepoImplementation
    lateinit var stripeRepo:StripeRepoImplementation
    fun initialization() {
        val api = RetrofitInstance.api
        val remote = RemoteDataSource(api)
        val currencyRetrofit= RetrofitInstance.currncyApi
        val remoteCurrency= RemoteDataSource(currencyRetrofit)
        val stripeAPI = RetrofitInstance.stripeRetrofit
        val stripeDataSource = StripeRemoteDataSource(stripeAPI)
        settingsRepo = SettingsRepoImplementation(remoteCurrency)
        authRepository = AuthRepositoryImpl(remote)
        productDetailsRepository = ProductDetailsRepositoryImpl(remote)
        categoryRepo = CategoryRepoImpl(remote)
        brandsRepo = BrandsRepoImpl(remote)
        adsRepoImplementation=AdsRepoImplementation(remote)
        favouriteRep= FavouriteRep(remote)
        productRepo= ProductRepoImpl(remote)
        cartRepoImplementation= CartRepoImplementation(remote)
        orderRepo = OrderRepoImpl(remote)
        searchRepo=SearchRepoImpl(remote)
        draftOrderRepo = DraftOrderRepoImplementation(remote)
        stripeRepo = StripeRepoImplementation(stripeDataSource)
    }

}