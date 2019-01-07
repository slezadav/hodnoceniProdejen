package cz.slezadav.hodnoceniProdejen.Util

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.widget.MultiAutoCompleteTextView

class NewLineTokenizer:MultiAutoCompleteTextView.Tokenizer{
    override fun findTokenStart(text: CharSequence, cursor: Int): Int {
        var i = cursor

        while (i > 0 && text[i - 1] != "\n".toCharArray()[0]) {
            i--
        }
        while (i < cursor && text[i] == ' ') {
            i++
        }

        return i
    }

    override fun findTokenEnd(text: CharSequence, cursor: Int): Int {
        var i = cursor
        val len = text.length

        while (i < len) {
            if (text[i] == "\n".toCharArray()[0]) {
                return i
            } else {
                i++
            }
        }

        return len
    }

    override fun terminateToken(text: CharSequence): CharSequence {
        var i = text.length

        while (i > 0 && text[i - 1] == ' ') {
            i--
        }

        if (i > 0 && text[i - 1] == "\n".toCharArray()[0]) {
            return text
        } else {
            if (text is Spanned) {
                val sp = SpannableString(text.toString() + "\n")
                TextUtils.copySpansFrom(
                    text, 0, text.length,
                    Any::class.java, sp, 0
                )
                return sp
            } else {
                return text.toString() + "\n"
            }
        }
    }
}