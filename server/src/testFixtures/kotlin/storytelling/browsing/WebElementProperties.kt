package kickstart.storytelling.actions

import com.vtence.mario.WebElementDriver
import org.openqa.selenium.WebElement


val text = WebElement::getText


val selectionState = WebElement::isSelected


fun textOf(element: WebElementDriver): String = element.query("text", text)


fun isSelected(element: WebElementDriver): Boolean = element.query("selection state", selectionState)