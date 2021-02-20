package dog.shebang.data.firestore.entity

import dog.shebang.model.Category
import dog.shebang.model.Color

data class CategoryEntity(
    val value: String? = null,
    val color: Color? = null
) {

    fun modelOrNull(): Category? {
        val elemList = listOf(value, color)
        if (elemList.contains(null)) return null

        return Category(value!!, color!!)
    }
}