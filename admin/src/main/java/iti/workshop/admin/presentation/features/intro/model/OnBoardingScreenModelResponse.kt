package iti.workshop.admin.presentation.features.intro.model

import iti.workshop.admin.R


object OnBoardingScreenModelResponse {
    val onBoardingObjects: List<OnBoardingScreenModel>
        get() {
            val obj = ArrayList<OnBoardingScreenModel>()
            obj.add(
                OnBoardingScreenModel(
                    R.drawable.onboarding1, "Easy Shopping",
                    """
                 allow users to search for products by keyword, category, or brand, and provide relevant results quickly.
                """.trimIndent()
                )
            )
            obj.add(
                OnBoardingScreenModel(
                    R.drawable.onboarding2, "Seamless integration",
                    """ integrate with other popular services such as payment gateways, or delivery services to offer a comprehensive shopping experience.
                """.trimIndent()
                )
            )
            obj.add(
                OnBoardingScreenModel(
                    R.drawable.onboarding3, "Coupon codes",
                    """an be applied at checkout to receive a percentage or fixed amount off the total purchase price.
                """.trimIndent()
                )
            )
            return obj
        }
}