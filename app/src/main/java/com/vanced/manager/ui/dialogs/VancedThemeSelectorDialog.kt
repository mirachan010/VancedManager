package com.vanced.manager.ui.dialogs

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.edit
import com.google.android.material.radiobutton.MaterialRadioButton
import com.vanced.manager.R
import com.vanced.manager.databinding.DialogBottomRadioButtonBinding
import com.vanced.manager.ui.core.BindingBottomSheetDialogFragment
import com.vanced.manager.utils.Extensions.convertToAppTheme
import com.vanced.manager.utils.Extensions.getCheckedButtonTag
import com.vanced.manager.utils.Extensions.show
import com.vanced.manager.utils.InternetTools.vanced

class VancedThemeSelectorDialog : BindingBottomSheetDialogFragment<DialogBottomRadioButtonBinding>() {

    companion object {

        fun newInstance(): VancedThemeSelectorDialog = VancedThemeSelectorDialog().apply {
            arguments = Bundle()
        }
    }

    private val prefs by lazy { requireActivity().getSharedPreferences("installPrefs", Context.MODE_PRIVATE) }

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DialogBottomRadioButtonBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        bindData()
    }

    private fun bindData() {
        with(binding) {
            loadButtons()?.forEach { mrb ->
                dialogRadiogroup.addView(
                    mrb,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            dialogTitle.text = requireActivity().getString(R.string.theme)
            val tag = root.findViewWithTag<MaterialRadioButton>(prefs.getString("theme", "dark"))
            if (tag != null) {
                tag.isChecked = true
            }
            dialogSave.setOnClickListener {
                val checkedTag = binding.dialogRadiogroup.getCheckedButtonTag()
                if (checkedTag != null) {
                    prefs.edit { putString("theme", checkedTag) }
                }
                dismiss()
            }
        }
    }

    private fun loadButtons() = vanced.get()?.array<String>("themes")?.value?.map {theme ->
        MaterialRadioButton(requireActivity()).apply {
            text = theme.convertToAppTheme(requireActivity())
            tag = theme
            textSize = 18f
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        VancedPreferencesDialog().show(requireActivity())
    }
}