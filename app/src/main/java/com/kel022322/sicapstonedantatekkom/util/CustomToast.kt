package com.kel022322.sicapstonedantatekkom.util

import android.content.Context
import android.widget.Toast

class CustomToast {

	private var toast: Toast? = null
	private var text: String? = null
	private var lastToast: Long = 0
	private val SAME_TOAST_DURATION_BEFORE_OVERLAP = 2000 // in ms

	fun showToast(context: Context, text: String, duration: Int) {
		if (toast == null) {
			toast = Toast.makeText(context, text, duration)
			this.text = text
		} else {
			if (this.text == text) {
				if (System.currentTimeMillis() - lastToast > SAME_TOAST_DURATION_BEFORE_OVERLAP) {
					toast!!.cancel()
				} else {
					return
				}
			} else {
				this.text = text
				toast!!.cancel()
				toast = Toast.makeText(context, this.text, duration)
			}
		}
		lastToast = System.currentTimeMillis()
		toast!!.show()
	}
}