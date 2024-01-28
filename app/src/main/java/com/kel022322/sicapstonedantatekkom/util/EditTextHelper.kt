package com.kel022322.sicapstonedantatekkom.util

import android.widget.EditText

class EditTextHelper {
	companion object {
		// Extension function for setting text or hint based on nullability
		fun EditText.setTextOrHint(text: String?, hint: Int) {
			if (!text.isNullOrBlank()) {
				this.setText(text)
			} else {
				this.setHint(hint)
			}
		}
	}
}