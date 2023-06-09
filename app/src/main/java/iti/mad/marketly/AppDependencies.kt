package iti.mad.marketly

import android.content.Context
import iti.mad.marketly.data.repository.authRepository.AuthRepositoryImpl
import iti.mad.marketly.data.repository.authRepository.IAuthRepository
import iti.mad.marketly.data.repository.productdetailsRepo.ProductDetailsRepository
import iti.mad.marketly.data.repository.productdetailsRepo.ProductDetailsRepositoryImpl
import iti.mad.marketly.data.source.remote.RemoteDataSource
import iti.mad.marketly.data.source.remote.retrofit.RetrofitInstance
import iti.mad.marketly.presentation.productdetails.viewmodel.ProductDetailsViewModel

object AppDependencies {
    lateinit var authRepository: IAuthRepository
    lateinit var productDetailsRepository: ProductDetailsRepository
    fun initialization() {
        val api = RetrofitInstance.api
        val remote = RemoteDataSource(api)
        authRepository = AuthRepositoryImpl(remote)
        productDetailsRepository = ProductDetailsRepositoryImpl(remote)
    }
}