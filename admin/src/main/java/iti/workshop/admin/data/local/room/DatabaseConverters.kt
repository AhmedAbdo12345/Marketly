package iti.workshop.admin.data.local.room

import androidx.room.TypeConverter
import iti.workshop.admin.data.dto.AddImage
import iti.workshop.admin.data.dto.Image
import iti.workshop.admin.data.dto.Variant


const val SPLITTER = "$$"

class ImageListConverters {
    @TypeConverter
    fun fromSrcToImageList(data: String?): List<Image>? {
        return data?.split(SPLITTER)?.map {
            Image(src = data)
        }

    }

    @TypeConverter
    fun fromImageListToString(data: List<Image>?): String? {
        return data?.map { it.src }?.run {
            if(isNotEmpty())
                reduce { src1: String?, src2: String? -> src1 + SPLITTER + src2 }
            else ""
        }

    }
}

class ImageConverters {
    @TypeConverter
    fun fromSrcToImage(data: String?): AddImage {
        return AddImage(src = data)
    }

    @TypeConverter
    fun fromImageListToString(data: AddImage?): String? {
        return data?.src
    }
}

class VariantListConverters {
    @TypeConverter
    fun fromSrcToVariantList(data: String?): List<Variant>? {
        return data?.split(SPLITTER)?.map {
            Variant(price = data)
        }
    }


    @TypeConverter
    fun fromVariantListToString(data: List<Variant>?): String? {
        return data?.map { it.price }?.run {
            if (isNotEmpty())
                reduce { src1: String?, src2: String? -> src1 + SPLITTER + src2 }
            else ""
        }
    }
}