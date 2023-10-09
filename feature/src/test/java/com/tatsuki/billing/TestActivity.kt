package com.tatsuki.billing

import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity

class TestActivity : FragmentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(
      FrameLayout(this).apply {
        id = android.R.id.content
      }
    )
  }
}