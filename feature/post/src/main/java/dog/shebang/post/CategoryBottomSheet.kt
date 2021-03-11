package dog.shebang.post

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StyleRes
import androidx.core.view.children
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import dog.shebang.core.R
import dog.shebang.core.databinding.LayoutCategoryBottomSheetBinding
import dog.shebang.model.Category
import dog.shebang.model.Color

@AndroidEntryPoint
class CategoryBottomSheet(
    private val onAddCategoryButtonClickListener: (Category) -> Unit,
    private val onChipClickListener: (Category) -> Unit,
) : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "dog.shebang.post.component.CategoryBottomSheet"
    }

    private lateinit var binding: LayoutCategoryBottomSheetBinding
    private val viewModel: CategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = LayoutCategoryBottomSheetBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel.categoryListLiveData.observe(viewLifecycleOwner) { loadState ->
                loadState.ifIsLoaded {
                    updateChipGroup(chipGroup, it)
                }
            }

            layoutCategoryInputField.apply {
                addCategoryTextFieldLayout.counterMaxLength = Category.maxLength

                addCategoryTextFieldLayout.setEndIconOnClickListener {
                    val color = Color.pickColor()

                    addCategoryTextFieldLayout.editText?.backgroundTintList =
                        ColorStateList.valueOf(color.value)
                }

                addCategoryTextFieldLayout.editText?.doOnTextChanged { text, _, _, _ ->

                    val contains = chipGroup.children
                        .map { if (it is Chip) it.text.toString() else null }
                        .filterNotNull()
                        .contains(text?.toString())

                    val isOver = text.toString().length > Category.maxLength

                    val context = binding.root.context
                    addCategoryButton.background.setTint(
                        if (contains || isOver) android.graphics.Color.GRAY else context.getColor(R.color.purple_700)
                    )

                    addCategoryButton.visibility =
                        if (text?.length == 0) View.GONE else View.VISIBLE

                    addCategoryButton.isClickable = !contains && !isOver
                }

                addCategoryButton.setOnClickListener {
                    val category = viewToCategory(binding)

                    onAddCategoryButtonClickListener(category)
                    dismiss()
                }
            }
        }
    }

    private fun updateChipGroup(chipGroup: ChipGroup, categoryList: List<Category>) {
        chipGroup.removeAllViews()
        categoryList.forEach {
            val chipView = createChip(
                chipGroup,
                R.style.Widget_MaterialComponents_Chip_Action,
                it,
                false,
            ) { _, category ->

                onChipClickListener(category)
                dismiss()
            }

            chipGroup.addView(chipView)
        }
    }

    private fun viewToCategory(binding: LayoutCategoryBottomSheetBinding): Category =
        binding.layoutCategoryInputField.let {
            val name = it.addCategoryTextField.text.toString()
            val color = Color.valueOf(
                it.addCategoryTextField.backgroundTintList?.defaultColor
            )

            Category(name, color)
        }

    private fun createChip(
        chipGroup: ChipGroup,
        @StyleRes style: Int,
        category: Category,
        isChipChecked: Boolean,
        onChipClick: (Chip, Category) -> Unit
    ): Chip {

        val chip = Chip(chipGroup.context)
        val chipDrawable = ChipDrawable.createFromAttributes(
            chipGroup.context,
            null,
            0,
            style
        )

        return chip.apply {
            setChipDrawable(chipDrawable)
            setOnClickListener { onChipClick(chip, category) }

            text = category.value
            isChecked = if (chipGroup.isSingleSelection) isChecked else isChipChecked

            chipBackgroundColor = ColorStateList.valueOf(category.color.value)
        }
    }

}