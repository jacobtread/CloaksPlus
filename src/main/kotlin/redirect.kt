package me.jacobtread.cloaksplus

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

fun containsRedirect(file: Path): Boolean {
    // Read the entire hosts file to a string
    val input = String(Files.readAllBytes(file), StandardCharsets.UTF_8)
    // The content to expect
    val content = "${App.REDIRECT_IP} ${App.DOMAIN_NAME} ${App.TAG}"
    // Return whether or not it contains the content
    return input.contains(content)
}

fun insertRedirect(file: Path) {
    // Create the redirect
    val content = "\n${App.REDIRECT_IP} ${App.DOMAIN_NAME} ${App.TAG}"
    // Append the content to the end of the file
    Files.write(file, content.toByteArray(StandardCharsets.UTF_8), StandardOpenOption.APPEND)
}

fun removeRedirect(file: Path) {
    // Read the entire hosts file to a string
    val input = String(Files.readAllBytes(file), StandardCharsets.UTF_8)
    // The content to expect
    val content = "${App.REDIRECT_IP} ${App.DOMAIN_NAME} ${App.TAG}"
    // Replace all matching content with nothing
    val output = input.replace(content, "")
    if (input.length != output.length) { // If we changed the data
        // Save the file
        Files.write(file, output.toByteArray(StandardCharsets.UTF_8))
    }
}