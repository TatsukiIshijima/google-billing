package com.tatsuki.inappbilling.ui.compose.util

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ItemBackground(
  modifier: Modifier = Modifier,
  onClick: () -> Unit = {},
  content: @Composable () -> Unit = {},
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(16.dp)
      )
      .clickable { onClick() }
      .padding(16.dp)
  ) {
    content()
  }
}