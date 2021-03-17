package dog.shebang.shelf.item

import com.xwray.groupie.viewbinding.BindableItem
import dog.shebang.model.Bookmark

typealias BindableItemProvider = (Bookmark, OnItemClickListener) -> BindableItem<*>