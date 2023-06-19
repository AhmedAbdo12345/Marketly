package iti.workshop.admin.domain.product

import iti.workshop.admin.data.repository.IProductRepository

class ProductUseCases(private val _repo: IProductRepository){
    // Product
    val addEditProduct:AddEditProduct by lazy {  AddEditProduct(_repo) }
    val deleteProduct:DeleteProduct by lazy { DeleteProduct(_repo) }
    val getProduct:GetProduct by lazy { GetProduct(_repo) }
    val getCountProduct:GetCountProduct by lazy { GetCountProduct(_repo) }

    // Variant
    val getVariantsProduct:GetVariantsProduct by lazy { GetVariantsProduct(_repo) }
    val deleteVariantProduct:DeleteVariantProduct by lazy {  DeleteVariantProduct(_repo) }
    val addEditVariant:AddEditVariant by lazy { AddEditVariant(_repo) }

    // Image
    val getImagesProduct:GetImagesProduct by lazy { GetImagesProduct(_repo) }
    val deleteImageProduct:DeleteImageProduct by lazy { DeleteImageProduct(_repo) }
    val addEditImage:AddEditImage by lazy { AddEditImage(_repo) }
}