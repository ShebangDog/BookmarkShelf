package dog.shebang.data.firestore.ext

import com.google.firebase.firestore.FirebaseFirestore

fun FirebaseFirestore.bookmarksRef(uid: String) = this
    .collection(FirestoreConstants.Users)
    .document(uid)
    .collection(FirestoreConstants.Bookmarks)

fun FirebaseFirestore.twitterBookmarksRef(uid: String) = this
    .bookmarksRef(uid)
    .document(uid)
    .collection(FirestoreConstants.Twitter)

fun FirebaseFirestore.defaultBookmarksRef(uid: String) = this
    .bookmarksRef(uid)
    .document(uid)
    .collection(FirestoreConstants.Default)

fun FirebaseFirestore.categoriesRef(uid: String) = this
    .collection(FirestoreConstants.Users)
    .document(uid)
    .collection(FirestoreConstants.Categories)
