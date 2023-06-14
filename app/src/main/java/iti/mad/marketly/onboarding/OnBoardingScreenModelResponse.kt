package iti.mad.marketly.onboarding

import iti.mad.marketly.R


object OnBoardingScreenModelResponse {
    val onBoardingObjects: List<OnBoardingScreenModel>
        get() {
            val obj = ArrayList<OnBoardingScreenModel>()
            obj.add(
                OnBoardingScreenModel(
                    R.drawable.onboarding1, "AAAAAAA",
                    """
                 Shopify App
                Online Store
                """.trimIndent()
                )
            )
            obj.add(
                OnBoardingScreenModel(
                    R.drawable.onboarding2, "BBB",
                    """
                Shopify App
                Online Store
                """.trimIndent()
                )
            )
            obj.add(
                OnBoardingScreenModel(
                    R.drawable.onboarding3, "CCC",
                    """
                Shopify App
                Online Store
                """.trimIndent()
                )
            )
            return obj
        }
}