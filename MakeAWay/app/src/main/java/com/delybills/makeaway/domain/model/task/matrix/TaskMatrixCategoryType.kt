package com.delybills.makeaway.domain.model.task.matrix

enum class TaskMatrixCategoryType(
    val title: String
) {
    Unknown(
        title = "Категория"
    ),
    UI(
        title = "Срочно | Важно"
    ),
    UNI(
        title = "Срочно | Неважно"
    ),
    NUI(
        title = "Несрочно | Важно"
    ),
    NUNI(
        title = "Несрочно | Неважно"
    )
}