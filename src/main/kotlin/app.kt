package me.jacobtread.cloaksplus

import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXCheckBox
import com.jfoenix.controls.JFXDialog
import com.jfoenix.controls.JFXDialogLayout
import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.stage.Stage
import java.awt.Desktop
import java.io.IOException
import java.io.InputStream
import java.net.URI
import java.net.URISyntaxException
import java.nio.file.AccessDeniedException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class App : Application() {

    companion object {
        const val WIDTH: Double = 450.0 // The window width
        const val HEIGHT: Double = 130.0 // The window height

        val WINDOWS_PATH: Path = Paths.get("C:/Windows/System32/drivers/etc/hosts") // The windows hosts file
        val LINUX_PATH: Path = Paths.get("/etc/hosts") // The linux hosts file

        const val DISCORD_LINK: String = "https://cloaks.plus/discord" // The cloaks+ discord page
        const val TWITTER_LINK: String = "https://cloaks.plus/twitter" // The cloaks+ twitter page
        const val GITHUB_LINK: String = "https://github.com/jacobtread/CloaksPlus" // The github link for this project

        val PRIMARY: Color = Color.web("#558CEF").darker() // The primary color

        const val REDIRECT_IP: String = "159.203.120.188" // The ip for the DNS record to resolve
        const val DOMAIN_NAME: String = "s.optifine.net" // The domain to set the DNS record for
        const val TAG: String = "# Added by Cloaks+" // A Unique identifier
    }

    private var root: StackPane = StackPane().apply {
        // Set the background to a gray background
        background = Background(
            BackgroundFill(
                Color(0.15, 0.15, 0.2, 1.0), // Dark gray color
                CornerRadii.EMPTY, // No rounded corners
                Insets.EMPTY // No insets
            )
        )
        prefWidth = WIDTH // Set the base width
        prefHeight = HEIGHT // Set the bsae height
    }

    override fun start(primaryStage: Stage) {
        // Get the hosts file path for this machine
        val hostsFile: Path = osPath()
        // Check if the file doesn't exist
        if (!Files.exists(hostsFile)) {
            // If it doesn't exist warn the user
            showDialog(
                "Failed to load",
                "Unable to detect hosts file. Supported operating systems are Windows and Linux. If your host file is missing please create it."
            )
            return // Cannot go any further
        }

        // Create a new scene
        val scene = Scene(root, WIDTH, HEIGHT)
        // Create an anchor pane for the content
        val content = AnchorPane()
        // Create an image view of the logo
        val logo = ImageView(javaClass.getResourceAsStream("/logo.png").image()).apply {
            x = 5.0 // Left
            y = 5.0 // Top
            // 120x120 px
            fitWidth = 120.0
            fitHeight = 120.0
        }
        // Create a new button for the Discord icon
        val discordButton: JFXButton = iconButton(
            javaClass.getResourceAsStream("/discord.png").image(),
            WIDTH - 47,
            5.0,
            32.0,
            28.0
        )
        // Set the button to open the url on clicked
        discordButton.setOnMouseClicked { openURL(DISCORD_LINK) }
        // Create a new button for the Twitter icon
        val twitterButton: JFXButton = iconButton(
            javaClass.getResourceAsStream("/twitter.png").image(),
            WIDTH - 47,
            44.5,
            32.0,
            26.5
        )
        // Set the button to open the url on clicked
        twitterButton.setOnMouseClicked { openURL(TWITTER_LINK) }
        // Create a new button for the GitHub icon
        val githubButton: JFXButton = iconButton(
            javaClass.getResourceAsStream("/github.png").image(),
            WIDTH - 47,
            83.0,
            32.0,
            32.0
        )
        // Set the button to open the url on clicked
        githubButton.setOnMouseClicked { openURL(GITHUB_LINK) }

        val buttonWidth: Double = WIDTH - 182
        val buttonHeight = 37.0
        val installButton = JFXButton("Install Cloaks+").apply {
            layoutX = 130.0
            layoutY = 5.0
            // Set the preferred width
            prefWidth = buttonWidth
            // Set the preferred height
            prefHeight = buttonHeight
            // Set the background color to the primary
            background = Background(BackgroundFill(PRIMARY, CornerRadii.EMPTY, Insets.EMPTY))
            // Set the text fill to white
            textFill = Color.WHITE
            // Set the button click action
            setOnMouseClicked {
                if (containsRedirect(hostsFile)) { // If we already have a redirect
                    // Show the already installed dialog
                    showDialog("Install Successful", "Cloaks+ is already installed on this machine")
                } else {
                    try { // Attempt to insert the redirect
                        insertRedirect(hostsFile)
                        showDialog("Install Successful", "Cloaks+ is now installed!")
                    } catch (e: AccessDeniedException) { // If we got denied permission to the file
                        // Let the user know admin rights are required
                        showDialog(
                            "Missing Permission",
                            "Cloaks+ doesn't have permission. please run it as administrator"
                        )
                    }
                }
            }
        }
        val uninstallButton = JFXButton("Uninstall")
        uninstallButton.apply {
            layoutX = 130.0
            layoutY = 47.0
            // Set the preferred width
            prefWidth = buttonWidth
            // Set the preferred height
            prefHeight = buttonHeight
            // Set the background color to the primary but darker
            background = Background(BackgroundFill(PRIMARY.darker(), CornerRadii.EMPTY, Insets.EMPTY))
            // Set the text fill to white
            textFill = Color.WHITE
            // Set the button click action
            setOnMouseClicked {
                if (containsRedirect(hostsFile)) { // If the hosts file contains a redirect
                    try { // Attempt to remove the redirect
                        removeRedirect(hostsFile)
                        // Show the success dialog
                        showDialog("Uninstall Successful", "Cloaks+ is no longer installed!")
                    } catch (e: AccessDeniedException) { // If we got denied permission to the file
                        // Let the user know admin rights are required
                        showDialog(
                            "Missing Permission",
                            "Cloaks+ doesn't have permission. please run it as administrator"
                        )
                    }
                } else { // If we dont have a redirect
                    // Show a not installed dialog
                    showDialog("Not Installed", "Cloaks+ is not installed on this machine")
                }
            }
        }
        val termsCheckbox = JFXCheckBox("I agree to the terms and conditions")
        termsCheckbox.apply {
            layoutX = 150.0
            layoutY = 80.0
            // Set the preferred width
            prefWidth = buttonWidth
            // Set the preferred height
            prefHeight = buttonHeight
            // Set the text fill to gray
            textFill = Color.GRAY
            // Set the checked color to the primary color
            checkedColor = PRIMARY
            // Set the checkbox to checked
            isSelected = true
            // Add a listener for when its value changes
            selectedProperty().addListener { _, _, newValue ->
                // Disable the install and uninstall buttons if the user
                // unchecks the checkbox
                installButton.isDisable = !newValue
                uninstallButton.isDisable = !newValue
            }
        }
        // Add all the components to the content pane
        content.children.addAll(
            logo,
            discordButton,
            twitterButton,
            githubButton,
            installButton,
            uninstallButton,
            termsCheckbox
        )
        // Add the content pane to the root
        root.children.add(content)

        // Setup the stage
        primaryStage.scene = scene // Set the scene on the stage
        primaryStage.title = "Cloaks+ Installer" // Set the stage title
        primaryStage.isResizable = false // Disable resizing
        primaryStage.sizeToScene() // Prevent the scene from using extra space
        // Add the logo as the window icon
        primaryStage.icons.add(javaClass.getResourceAsStream("/logo.png").image())
        primaryStage.show() // Show the stage
    }


    private fun showDialog(title: String, message: String) {
        val dialog = JFXDialog() // Create a new dialog
        // Set the dialog content
        dialog.content = JFXDialogLayout().apply {
            // Set the heading
            setHeading(
                // Create new text and set its color to white
                Text(title).apply { fill = Color.WHITE }
            )
            val body = VBox() // Create a vbox layout
            body.spacing = 15.0 // 15px spacing between items
            body.children.addAll(
                // Create new text and set its color to gray
                Text(message).apply { fill = Color.GRAY },
                // Create a button that closes the dialog
                JFXButton("Close").apply {
                    textFill = Color.WHITE
                    // When the mouse clicks the button close the dialog
                    setOnMouseClicked { dialog.close() }
                }
            )
            setBody(body)  // Set the body contents
            // Set the background
            background = Background(
                BackgroundFill(
                    Color(0.1, 0.1, 0.1, 1.0), // Dark gray color
                    CornerRadii.EMPTY, // No rounded corners
                    Insets.EMPTY // No insets
                )
            )
        }
        dialog.show(root) // Show the dialog
    }

    private fun osPath(): Path {
        val name = (System.getProperty("os.name") ?: "win").lowercase() // Get the os name (lowercase to ignore case
        return when {
            // If the name contains win then use the windows path
            name.contains("win") -> WINDOWS_PATH
            // If the name contains nix, nux, or aix use the linux path
            name.contains("nix") || name.contains("nux") || name.contains("aix") -> LINUX_PATH
            else -> WINDOWS_PATH // Fallback to the windows path if all else fails
        }
    }

}


fun openURL(url: String) {
    try {
        // Open the url in the default browser
        Desktop.getDesktop().browse(URI(url))
    } catch (e2: IOException) {
        e2.printStackTrace()
    } catch (e2: URISyntaxException) {
        e2.printStackTrace()
    }
}

/**
 *  JUST A WORK AROUND TO IGNORE NULLS
 */
fun InputStream?.image(): Image {
    this ?: throw IOException("Failed to load image")
    return Image(this)
}

fun iconButton(
    image: Image,
    x: Double,
    y: Double,
    width: Double,
    height: Double,
): JFXButton {
    // Create an image view
    val imageView = ImageView(image).apply {
        // Apply the width and height
        fitWidth = width
        fitHeight = height
    }
    // Create an new button with empty text and the image
    return JFXButton("", imageView).apply {
        // Set the position
        layoutX = x
        layoutY = y
        // Set the preferred size
        prefWidth = width
        prefHeight = height
        // Add a little bit of padding
        padding = Insets(5.0, 5.0, 5.0, 5.0)
        // Set a transparent background
        background = Background(BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY))
        // Set the ripple effect color to the primary color
        ripplerFill = App.PRIMARY
    }
}

fun main() {
    // Launch the application
    Application.launch(App::class.java)
}