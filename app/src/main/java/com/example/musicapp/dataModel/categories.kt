package com.example.musicapp.dataModel

object categories {
    fun categoryList(): List<categoryClass> {
        return listOf(
            categoryClass(
                name = "Pop Hits",
                imageUrl = "https://yt3.googleusercontent.com/ytc/AIdro_nXaFbwWvBrEcHwW5nG1ciilEKFDFHI14fOYATLuuNy4w=s900-c-k-c0x00ffffff-no-rj"
            ),
            categoryClass(
                name = "English",
                imageUrl = "https://dev-resws.hungamatech.com/featured_content/f8900756fa64274e712175cac5c7a107_500x500.jpg"
            ),
            categoryClass(
                name = "Sad Songs",
                imageUrl = "https://i1.sndcdn.com/artworks-FTAnbGzayAD7eOke-IUzJGA-t500x500.jpg"
            ),
            categoryClass(
                name = "Mood Musics",
                imageUrl = "https://c.saavncdn.com/190/Mood-Music-for-Listening-and-Relaxation-English-2018-20180429180629-500x500.jpg"
            ),
            categoryClass(
                name = "Lofi Songs",
                imageUrl = "https://storage.googleapis.com/pai-images/1ad47c635c69472da70da50a44b22fe9.jpeg"
            ),
            categoryClass(
                name = "Hindi",
                imageUrl = "https://engage4more.com/blog/wp-content/uploads/2022/07/Arijit-Singh-1024x1024.jpg"
            ),
        )
    }
}