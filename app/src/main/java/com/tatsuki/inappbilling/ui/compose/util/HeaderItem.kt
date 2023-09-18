package com.tatsuki.inappbilling.ui.compose.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HeaderItem(
  modifier: Modifier = Modifier,
  title: String,
) {
  Text(
    modifier = modifier,
    text = title,
    fontSize = MaterialTheme.typography.titleLarge.fontSize,
    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
    color = MaterialTheme.colorScheme.onPrimaryContainer
  )
}