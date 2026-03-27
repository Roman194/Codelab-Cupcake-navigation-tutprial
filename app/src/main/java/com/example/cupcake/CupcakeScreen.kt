package com.example.cupcake

//Lingma 1st prompt 2nd attempt

/**
 * Код не запускается. В StartOrderScreen не найден аргумент quantityOptions, который ранее там был.
 * Верни его пожалуйста. Аналогично с SelectOptionChanged и аргументом subtotal. В общем перепроверь
 * соответствие аргументов и параметров вызываемых функций в CupcakeApp composable. При конфликтах
 * лучше ориентироваться на параметры функций, так как они выглядят правильнее чем их аргументы.
 * Также сверься с OrderViewModel на предмет названий вызываемых свойств. Допустим,
 * viewModel.pickupDates не найдено IDE. Вероятно оно называется во ViewModel как-то похоже, но иначе.
 * Не забудь про @Preview-функции в рамках файлов StartOptionScreen, SelectOptionScreen и SummaryScreen!
 * Они тоже должны иметь параметры, соответствующие аргументам соответствующих Composable-функций
 */

enum class CupcakeScreen {
    START,
    FLAVOR,
    PICKUP_DATE,
    SUMMARY
}