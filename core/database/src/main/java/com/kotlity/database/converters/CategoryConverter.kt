package com.kotlity.database.converters

import androidx.room.TypeConverter
import com.kotlity.domain.models.Category

/**
 *  Class that converts enum Category to string and vice versa
 */
class CategoryConverter: Converter<Category?, String?> {

    @TypeConverter
    override fun from(value: Category?): String? {
        return converterHelper {
            value?.name?.lowercase()
        }
    }

    @TypeConverter
    override fun to(value: String?): Category? {
        return converterHelper {
            value?.let { Category.valueOf(it.uppercase()) }
        }
    }
}