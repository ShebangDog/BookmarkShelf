package dog.shebang.component.ext

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.setSpan(
    left: Int? = null,
    top: Int? = null,
    right: Int? = null,
    bottom: Int? = null
) {

    this.addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)

            outRect.apply {
                this.left = left ?: this.left
                this.top = top ?: this.top
                this.right = right ?: this.right
                this.bottom = bottom ?: this.bottom
            }
        }
    })

}

fun RecyclerView.setSpan(all: Int) {
    this.setSpan(all, all, all, all)
}