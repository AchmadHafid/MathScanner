package io.github.achmadhafid.mathscanner.home

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognizer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MathScanner @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val textRecognizer: TextRecognizer
) {

    suspend fun scan(uri: Uri): Pair<String, Int?> {
        val inputImage = InputImage.fromFilePath(context, uri)
        val text = textRecognizer.process(inputImage).await()
        Log.d("MathScanner", text.text)
        return text.textBlocks.firstNotNullOfOrNull { it.getScanResult() } ?: (text.text to null)
    }

    private fun Text.TextBlock.getScanResult(): Pair<String, Int>? =
        lines.firstNotNullOfOrNull { it.getScanResult() }

    private fun Text.Line.getScanResult(): Pair<String, Int>? {
        if (elements.size < 3) return null

        var firstNumber: Int? = null
        var secondNumber: Int? = null
        var operator: MathOperator? = null

        fun reset() {
            firstNumber = null
            secondNumber = null
            operator = null
        }

        elements.forEach { element ->
            element.getNumberOrNull()?.let {
                if (firstNumber != null && operator != null) {
                    secondNumber = it
                    return@forEach
                } else {
                    reset()
                    firstNumber = it
                }
            } ?: element.getMathOperatorOrNull()?.let {
                if (firstNumber != null && operator == null) {
                    operator = it
                } else reset()
            } ?: reset()
        }

        return operator?.let { opr ->
            firstNumber?.let { n1 ->
                secondNumber?.let { n2 ->
                    "$n1 ${opr()} $n2" to opr.calculate(n1, n2)
                }
            }
        }
    }

    private fun Text.Element.getNumberOrNull(): Int? =
        runCatching { text.toInt() }.getOrNull()

    private fun Text.Element.getMathOperatorOrNull(): MathOperator? =
        MathOperator.values().firstOrNull { it.symbol.contains(text) }

    private enum class MathOperator(vararg val symbol: String) {
        Add("+"),
        Subtract("-"),
        Multiply("*", "x", "X"),
        Divide(":", "/");

        operator fun invoke(): String = symbol.first()

        fun calculate(arg1: Int, arg2: Int): Int =
            when (this) {
                Add -> arg1 + arg2
                Subtract -> arg1 - arg2
                Multiply -> arg1 * arg2
                Divide -> arg1 / arg2
            }
    }

}
