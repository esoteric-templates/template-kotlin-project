package org.example.project

class App {
    val greeting: String
        get() {
            return "Hello World!"
        }

    val farewell: String
        get() {
            return "Goodbye World!"
        }
}

fun main() {
    val app = App()

    val pair = Pair(app.greeting, app.farewell)

    println(pair.first)
    println(pair.second)
}
