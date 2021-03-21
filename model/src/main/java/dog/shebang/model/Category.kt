package dog.shebang.model

import kotlin.random.Random

data class Category(val name: String, val color: Color) {
    init {
        require(name.length <= maxLength || name == defaultCategoryName)
    }

    companion object {
        const val maxLength = 10

        const val defaultCategoryName = "All"

        val defaultCategory = Category(defaultCategoryName, Color.defaultColor)
    }

    fun isDefault(): Boolean = this.name == defaultCategoryName
}

class Color private constructor(internalColor: android.graphics.Color) {
    constructor() : this(android.graphics.Color())

    val value = internalColor.toArgb()

    companion object {
        val defaultColor = valueOf(android.graphics.Color.WHITE)

        fun valueOf(rgb: Int): Color {
            return Color(android.graphics.Color.valueOf(rgb))
        }

        fun valueOf(rgb: Int?): Color {
            return if (rgb == null) defaultColor else valueOf(rgb)
        }

        private fun valueOf(red: Int, green: Int, blue: Int): Color {
            return Color(
                android.graphics.Color.valueOf(
                    red.toFloat(), green.toFloat(),
                    blue.toFloat()
                )
            )
        }

        fun parseColor(colorCode: String): Color {
            return valueOf(android.graphics.Color.parseColor(colorCode))
        }

        fun pickColor(): Color {
            fun randomElem() = Random.nextInt(256)

            return valueOf(randomElem(), randomElem(), randomElem())
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Color

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value
    }


}
