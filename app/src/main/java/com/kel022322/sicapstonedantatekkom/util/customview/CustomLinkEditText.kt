package com.kel022322.sicapstonedantatekkom.util.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.kel022322.sicapstonedantatekkom.R
import java.util.regex.Pattern

class CustomLinkEditText : TextInputEditText {

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
				val link = s?.toString() ?: ""
				val parentLayout = getParentTextInputLayout()
				val isValidLink = isValidURL(link)
				if (link.isNotEmpty() && isValidLink) {
					parentLayout?.error = null
					parentLayout?.isErrorEnabled = false
				} else {
					parentLayout?.error = if (link.isEmpty()) {
						"Kolom ini tidak boleh kosong!"
					} else {
						"Link tidak valid!"
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

	private fun isValidURL(url: String): Boolean {
		val pattern = Pattern.compile("^(https?|ftp)://.*\$", Pattern.CASE_INSENSITIVE)
		val matcher = pattern.matcher(url)
		return matcher.matches()
	}
}
