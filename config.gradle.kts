mapOf(
        "kotlin_version" to "1.2.71",
        "kotlin_coroutine" to "0.22.5",
        "dokka" to "0.9.16",
        "okhttp" to "3.10.0",
        "gson" to "2.8.5",
        "jetty_server" to "9.4.12.v20180830",
        "junit" to "4.12"
).entries.forEach {
    project.extra.set(it.key, it.value)
}