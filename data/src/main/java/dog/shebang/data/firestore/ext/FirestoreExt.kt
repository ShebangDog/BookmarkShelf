package dog.shebang.data.firestore.ext

import com.google.firebase.firestore.FirebaseFirestore

fun FirebaseFirestore.bookmarksRef(uid: String) = this
    .collection(FirestoreConstants.Users)
    .document(uid)
    .collection(FirestoreConstants.Bookmarks)
