package com.kel022322.sicapstonedantatekkom.util.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.kel022322.sicapstonedantatekkom.R

class CustomJudulTAEditText : TextInputEditText {

	constructor(context: Context) : super(context) {
		init()
	}

	constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
		init()
	}

	constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
		context,
		attrs,
		defStyleAttr
	) {
		init()
	}

	private fun init() {
		val parentLayout = getParentTextInputLayout()
		parentLayout?.error = null

		addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

			override fun afterTextChanged(s: Editable?) {
				val judulTA = s?.toString() ?: ""
				val parentLayout = getParentTextInputLayout()

				if (isValidJudulTA(judulTA)) {
					parentLayout?.error = null
					parentLayout?.isErrorEnabled = false
				} else {
					parentLayout?.error = if (judulTA.isEmpty()) {
						"Kolom ini tidak boleh kosong!"
					} else {
						"Judul TA maksimal 20 kata!"
					}
					setCustomErrorTypeface(parentLayout)
					parentLayout?.isErrorEnabled = true
				}
			}
		})
	}

	private fun getParentTextInputLayout(): TextInputLayout? {
		var parent = parent
		while (parent != null && parent !is TextInputLayout) {
			parent = parent.parent
		}
		return parent as? TextInputLayout
	}

	private fun setCustomErrorTypeface(textInputLayout: TextInputLayout?) {
		val typeface = ResourcesCompat.getFont(context, R.font.poppinsregular)
		textInputLayout?.typeface = typeface
		textInputLayout?.setErrorTextAppearance(R.style.ErrorTextAppearance)
	}

	private fun isValidJudulTA(judul: String): Boolean {
		val wordCount = judul.trim().split("\\s+".toRegex()).size
		return wordCount <= 20
	}
}
