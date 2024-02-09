package com.kel022322.sicapstonedantatekkom.util.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.kel022322.sicapstonedantatekkom.R

class CustomIPKEditText : TextInputEditText {

	constructor(context: Context) : super(context)
	constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
	constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
		context,
		attrs,
		defStyleAttr
	)

	init {
		addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
			}

			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
			}

			override fun afterTextChanged(s: Editable?) {
				val ipk = s.toString()
				val parentLayout = getParentTextInputLayout()
				if (isValidIPK(ipk)) {
					parentLayout?.error = null
					parentLayout?.isErrorEnabled = false
				} else {
					parentLayout?.error = "IPK tidak valid!"
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
	}

	fun isValidIPK(ipk: String): Boolean {
		return ipk.isNotEmpty() && try {
			val ipkValue = ipk.toDouble()
			val decimalCount = getDecimalCount(ipk)
			ipkValue in 0.00..4.00 && decimalCount <= 2
		} catch (e: NumberFormatException) {
			false
		}
	}

	private fun getDecimalCount(value: String): Int {
		val decimalIndex = value.indexOf('.')
		return if (decimalIndex == -1) {
			0
		} else {
			value.length - decimalIndex - 1
		}
	}

}
