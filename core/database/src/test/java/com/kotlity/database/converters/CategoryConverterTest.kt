package com.kotlity.database.converters

import com.google.common.truth.Truth.assertThat
import com.kotlity.database.converters.di.testCategoryConverterModule
import com.kotlity.domain.models.Category
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

/**
 *  Class that created for testing CategoryConverter, where:
 *  KoinTest is an interface that designates an object as a KoinComponent, which allows injecting dependencies from Koin modules.
 */
class CategoryConverterTest: KoinTest {

    private val testCategoryConverter by inject<Converter<Category?, String?>>()

    /**
     * A rule that automatically starts Koin before each test and stops it after each test
     */
    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger()
        modules(testCategoryConverterModule)
    }

    @Test
    fun `convert from Category to string returns non nullable string`() {
        val mockCategory = Category.TAXI
        val expectedResult = mockCategory.name.lowercase()

        val result = testCategoryConverter.from(mockCategory)
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `convert from string to Category returns non nullable Category`() {
        val mockString = Category.GROCERIES.name.lowercase()
        val expectedResult = Category.GROCERIES

        val result = testCategoryConverter.to(mockString)
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `convert from nullable Category to string returns nullable string`() {
        val mockNullableCategory: Category? = null
        val expectedResult: String? = null

        val result = testCategoryConverter.from(mockNullableCategory)
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `convert from nullable string to Category returns nullable Category`() {
        val mockNullableString: String? = null
        val expectedResult: Category? = null

        val result = testCategoryConverter.to(mockNullableString)
        assertThat(result).isEqualTo(expectedResult)
    }
}