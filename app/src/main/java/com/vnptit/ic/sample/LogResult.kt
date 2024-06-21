package com.vnptit.ic.sample

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LogResult(
   val title: String,
   val result: String?
) : Parcelable