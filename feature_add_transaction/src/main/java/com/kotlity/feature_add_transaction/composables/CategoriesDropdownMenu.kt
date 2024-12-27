package com.kotlity.feature_add_transaction.composables

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kotlity.domain.models.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesDropdownMenu(
    modifier: Modifier = Modifier,
    selectedCategory: Category,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onSelectedCategory: (Category) -> Unit,
    onDismiss: () -> Unit
) {

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        TextField(
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
            value = selectedCategory.name.lowercase(),
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss
        ) {
            Category.entries.map { it.name.lowercase() }.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(text = category)
                    },
                    onClick = {
                        val selected = Category.valueOf(category.uppercase())
                        onSelectedCategory(selected)
                        onDismiss()
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}