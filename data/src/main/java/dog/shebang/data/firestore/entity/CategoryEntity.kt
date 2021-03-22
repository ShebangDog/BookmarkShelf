package dog.shebang.data.firestore.entity

import dog.shebang.model.Category
import dog.shebang.model.Color

data class CategoryEntity(
    val name: String? = null,
    val color: Color? = null
) {

    fun modelOrNull(): Category? = try {
        Category(
            name!!,
            color!!
        )
    } catch (throwable: Throwable) {
        null
    }
}
