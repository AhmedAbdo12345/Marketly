
class BrandsViewModel: ViewModel() {
   private var brands : MutableStateFlow<BrandApiStatus> = MutableStateFlow(BrandApiStatus.Loading())
    val _brands: StateFlow<BrandApiStatus> = brands



    fun getAllBrands( brandsRepo: BrandsRepo){
        viewModelScope.launch{


                brandsRepo.getBrands().flowOn(Dispatchers.IO).catch {
                    brands.value =BrandApiStatus.Failed(it.localizedMessage)

                }.collect{
                    brands.value=BrandApiStatus.Success(it)
                }


        }
    }
}