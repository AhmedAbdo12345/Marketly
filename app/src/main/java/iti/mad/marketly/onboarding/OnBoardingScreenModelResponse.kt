package iti.mad.marketly.onboarding

import iti.mad.marketly.R


object OnBoardingScreenModelResponse {
    val onBoardingObjects: List<OnBoardingScreenModel>
        get() {
            val obj = ArrayList<OnBoardingScreenModel>()
            obj.add(
                OnBoardingScreenModel(
                    R.drawable.onboarding4, "Discover Trends",
                    """
               Now we one here to provide variety
                    of the best fashion
                """.trimIndent()
                )
            )
            obj.add(
                OnBoardingScreenModel(
                    R.drawable.onboarding5, "Lattest Outfit",
                    """
                Express your self through the art of 
                     the fashionism
                """.trimIndent()
                )
            )
            obj.add(
                OnBoardingScreenModel(
                    R.drawable.onboarding6, "Your Own Style",
                    """
                Smart & Fashionable
                Collection makes you Cool
                """.trimIndent()
                )
            )
            return obj
        }
}