package dog.shebang.data.entity

import dog.shebang.model.Category

data class CategoryEntity(
    val value: String? = null
) {

    fun modelOrNull(): Category? {
        val elemList = listOf(value)
        if (elemList.contains(null)) return null

        return Category(value!!)
    }
}