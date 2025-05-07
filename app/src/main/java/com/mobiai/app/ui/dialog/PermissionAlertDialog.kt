import android.content.Context
import com.mobiai.R
import com.mobiai.app.utils.setOnSafeClickListener
import com.mobiai.base.basecode.ui.dialog.BaseDialog
import com.mobiai.databinding.DialogPermisstionAlertBinding

class PermissionAlertDialog(
    context: Context,
    callback: () -> Unit

) : BaseDialog(context) {

    private val binding: DialogPermisstionAlertBinding =
        DialogPermisstionAlertBinding.inflate(layoutInflater)

    init {
        setContentView(binding.root)
        setCanceledOnTouchOutside(false)


        binding.tvSettting.setOnSafeClickListener {
            callback()
            dismiss()
        }

        binding.tvClose.setOnSafeClickListener {
            dismiss()

        }
    }



}