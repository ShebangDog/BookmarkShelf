package dog.shebang.data.firestore.entity

import dog.shebang.model.Category
import dog.shebang.model.Color

data class CategoryEntity(
    val value: String? = null,
    val color: Color? = null
) {

    fun modelOrNull(): Category? = try {
        Category(
            value!!,
            color!!
        )
    } catch (throwable: Throwable) {
        null
    }
}