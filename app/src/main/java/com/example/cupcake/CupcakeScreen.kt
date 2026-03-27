package com.example.cupcake


//Lingma 1st prompt 3rd attempt
/**
 * Код всё ещё не запускается! При вызове SelectOptionScreen в параметре options ты используешь
 * DataSource.flavors, а он идёт как List<int>, а не List<string>. Аналогичная проблема есть в
 * SelectOptionScreen.kt в SelectOptionPreview. Также в CupakeApp.kt при вызове onOptionSelected во
 * viewModel не найден метод setPrice. В SummaryScreen содержится ряд ссылок на несуществующие ресурсы:
 * R.string.summary_description и R.string.confirm, а также при вызове orderViewModel.uiState.value
 * возникает следующая ошибка: StateFlow.value should not be called within composition. В
 * OrderViewModel.kt в pickupOptions есть ссылка на несущестующую переменную: DataSource.pickupDates
 */
enum class CupcakeScreen {
    START,
    FLAVOR,
    PICKUP_DATE,
    SUMMARY
}