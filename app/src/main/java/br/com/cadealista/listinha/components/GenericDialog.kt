package br.com.cadealista.listinha.components

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog


class GenericDialog @JvmOverloads constructor(
    context: Context,
    private val title: String?,
    private val bodyMessage: String?,
    private val positiveButtonText: String?,
    private val negativeButtonText: String?,
    private val onPositiveButtonClick: (() -> Unit)?,
    private val onNegativeButtonClick: (() -> Unit)?,
    private val isDismissible: Boolean = true
) : AlertDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val alertDialogBuilder = AlertDialog.Builder(context)

        with(alertDialogBuilder) {
            setCancelable(isDismissible)
            title?.let { setTitle(it) }
            bodyMessage?.let { setMessage(it) }

            positiveButtonText?.let {
                setPositiveButton(it) { dialog, _ ->
                    onPositiveButtonClick?.invoke()
                    dialog.cancel()
                }
            }

            negativeButtonText?.let {
                setNegativeButton(it) { dialog, _ ->
                    onNegativeButtonClick?.invoke()
                    dialog.cancel()
                }
            }

            show()
        }
    }

    class Builder(private val context: Context) {
        private var title: String? = null
        private var bodyMessage: String? = null
        private var positiveButtonText: String? = null
        private var negativeButtonText: String? = null
        private var onPositiveButtonClick: (() -> Unit)? = null
        private var onNegativeButtonClick: (() -> Unit)? = null
        private var isDismissible: Boolean = true

        fun build(): GenericDialog {
            return GenericDialog(
                context,
                title,
                bodyMessage,
                positiveButtonText,
                negativeButtonText,
                onPositiveButtonClick,
                onNegativeButtonClick,
                isDismissible
            ).apply {
                create()
            }
        }

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setBodyMessage(bodyMessage: String): Builder {
            this.bodyMessage = bodyMessage
            return this
        }

        fun setPositiveButtonText(positiveButtonText: String): Builder {
            this.positiveButtonText = positiveButtonText
            return this
        }

        fun setNegativeButtonText(negativeButtonText: String): Builder {
            this.negativeButtonText = negativeButtonText
            return this
        }

        fun setOnPositiveButtonClickListener(onClick: () -> Unit): Builder {
            onPositiveButtonClick = onClick
            return this
        }

        fun setOnNegativeButtonClickListener(onClick: () -> Unit): Builder {
            onNegativeButtonClick = onClick
            return this
        }

        fun setDismissible(isDismissible: Boolean): Builder {
            this.isDismissible = isDismissible
            return this
        }
    }
}