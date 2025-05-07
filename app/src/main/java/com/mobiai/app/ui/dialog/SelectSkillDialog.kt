import android.content.Context
import com.mobiai.R
import com.mobiai.app.utils.setOnSafeClickListener
import com.mobiai.base.basecode.ui.dialog.BaseDialog
import com.mobiai.databinding.DialogPermisstionAlertBinding
import com.mobiai.databinding.DialogSelectSkillBinding

class SelectSkillDialog(
    context: Context,
    callback: (positionSkill:Int) -> Unit

) : BaseDialog(context) {

    private val binding: DialogSelectSkillBinding =
        DialogSelectSkillBinding.inflate(layoutInflater)

    init {
        setContentView(binding.root)
        setCanceledOnTouchOutside(true)

        binding.ctnReading.setOnSafeClickListener {
            callback(1)
            dismiss()
        }

        binding.ctnWriting.setOnSafeClickListener {
            callback(2)
            dismiss()
        }

        binding.ctnSpeaking.setOnSafeClickListener {
            callback(3)
            dismiss()
        }

        binding.ctnListening.setOnSafeClickListener {
            callback(4)
            dismiss()
        }
    }
}