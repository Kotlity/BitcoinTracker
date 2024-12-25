package com.kotlity.database.converters.di

import com.kotlity.database.converters.CategoryConverter
import com.kotlity.database.converters.Converter
import com.kotlity.domain.models.Category
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 *  Koin module for testing CategoryConverter
 */
val testCategoryConverterModule = module {
    factoryOf(::CategoryConverter) { bind<Converter<Category?, String?>>() }
}