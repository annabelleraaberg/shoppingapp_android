package com.example.examcode.screens.product_overview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.examcode.data.room.ProductEntity

@Composable
fun CategoryFilter(
    categories: List<String>,
    selectedCategory: List<ProductEntity>,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(categories) {category ->
            CategoryChip(
                text = category,
                isSelected = selectedCategory.any { it.category == category },
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
fun CategoryChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color.Gray else Color.White

    Surface(
        modifier = Modifier
            .padding(4.dp)
            .clickable { onClick() },
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(8.dp),
            color = if (isSelected) Color.White else Color.Black
        )
    }
}


@Preview
@Composable
fun PreviewCategoryFilter() {
    /* This part of the code is commented out because it caused issues when running the app,
     and since it is only a preview and not something that the app needs when running, I chose
     to comment it out. */
    /*
    val selectedCategory = listOf()
    CategoryFilter(
        categories = listOf("smartphones", "laptops", "fragrances", "skincare", "groceries", "home-decoration"),
        selectedCategory = selectedCategory
    ) { /* Handle category selection */ }

     */
}


