package iti.mad.marketly.data.repository.productdetailsRepo

import iti.mad.marketly.data.model.product.Product
import iti.mad.marketly.data.model.productDetails.ProductDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeProductDetailsRepo : ProductDetailsRepository {
    val products = listOf<Product>(
        Product(
            id = 8391229473078,
            title = "MEN'S SHOES SMITH",
            body_html = "classic style. These kids' shoes preserve the iconic look of the original, made in leather with punched 3-Stripes, heel and tongue logos and lightweight step-in cushioning.",
            vendor = "SMITH"
        ),
        Product(
            id = 8391229473080,
            title = "ADIDAS | KID'S STAN SMITH",
            body_html = "The Stan Smith owned the tennis court in the '70s. Today it runs the streets with the same clean, classic style. These kids' shoes preserve the iconic look of the original, made in leather with punched 3-Stripes, heel and tongue logos and lightweight step-in cushioning.",
            vendor = "ADIDAS"
        ),
        Product(
            id = 8391229473081,
            title = "ADIDAS | AL AHLY T-shirt",
            body_html = "The t shirt of mohamed abu treika",
            vendor = "NIKE"
        ), Product(
            id = 8391229473079,
            title = "11 salah liverpool",
            body_html = "Mohamed salah teshirt",
            vendor = "LiverPOol"
        )

    )

    override suspend fun getProductDetails(id: Long): Flow<ProductDetails> = flow {
        val product = products.find {
            it.id == id
        }
        emit(ProductDetails(product))
    }
}