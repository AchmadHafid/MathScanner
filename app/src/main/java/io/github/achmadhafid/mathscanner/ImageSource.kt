package io.github.achmadhafid.mathscanner

enum class ImageSource(private val source: String) {
    Filesystem("filesystem"), Camera("camera");

    operator fun invoke() = source
}
