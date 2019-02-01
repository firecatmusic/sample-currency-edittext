package local.practice.samplelottie

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.lang.ref.WeakReference
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var etMoney: EditText
    lateinit var btnShow: Button
    lateinit var tvMoney: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etMoney = findViewById(R.id.etMoney)
        btnShow = findViewById(R.id.btnShow)
        tvMoney = findViewById(R.id.tvMoney)

        /**
         *  etMoney.setMaskingMoney("Rp. ")
         *  ini buat ngeset currencynya
         *  [setMaskingMoney]
         */
        etMoney.setMaskingMoney("Rp. ")
        Selection.setSelection(etMoney.text, etMoney.text!!.length)

        etMoney.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!s.toString().startsWith("Rp. ")) {
                    etMoney.setMaskingMoney("Rp. ")
                    Selection.setSelection(etMoney.text, etMoney.text!!.length)
                }

            }
        })

        btnShow.setOnClickListener {
            val cleanString = etMoney.text.toString().replace("[Rp,. ]".toRegex(), "")
            tvMoney.text = cleanString
        }
    }

    fun EditText.setMaskingMoney(currencyText: String) {
        this.addTextChangedListener(object: MyTextWatcher{
            val editTextWeakReference: WeakReference<EditText> = WeakReference<EditText>(this@setMaskingMoney)
            override fun afterTextChanged(editable: Editable?) {
                val editText = editTextWeakReference.get() ?: return
                val s = editable.toString()
                editText.removeTextChangedListener(this)
                val cleanString = s.replace("[Rp,. ]".toRegex(), "")
                val newval = currencyText + cleanString.monetize()

                editText.setText(newval)
                editText.setSelection(newval.length)
                editText.addTextChangedListener(this)
            }
        })
    }

    interface MyTextWatcher: TextWatcher {
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }

    fun String.monetize(): String = if (this.isEmpty()) "0"
    else DecimalFormat("#,###").format(this.replace("[^\\d]".toRegex(),"").toLong())
}
