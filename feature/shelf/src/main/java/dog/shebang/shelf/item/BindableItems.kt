import com.xwray.groupie.viewbinding.BindableItem
import dog.shebang.model.Bookmark
import dog.shebang.shelf.item.OnItemClickListener

typealias BindableItemProvider = (Bookmark, OnItemClickListener) -> BindableItem<*>