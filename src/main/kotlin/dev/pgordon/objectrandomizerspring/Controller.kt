package dev.pgordon.objectrandomizerspring

import com.google.gson.*
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.awt.SystemColor.text
import kotlin.random.Random
import com.google.gson.GsonBuilder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


@RestController
class GreetingController {
    @PostMapping("/json/randomize")
    fun greeting(@RequestBody requestBody: String): String {

       val requestBody1 = URLDecoder.decode(requestBody, StandardCharsets.UTF_8.toString())
           .removeSuffix("=")//todo wtf?
        val gson = GsonBuilder()
//            .disableHtmlEscaping()
            .setLenient()
            .create()
        print(requestBody1)
        val json = gson.fromJson(requestBody1, JsonElement::class.java)
        return traverseAndRandomize(json).toString()
    }
}


fun traverseAndRandomize(json: JsonElement): JsonElement {
    return when {
        json.isJsonNull -> json
        json.isJsonArray -> json.asJsonArray.randomize()
        json.isJsonPrimitive -> json.asJsonPrimitive.randomize()
        json.isJsonObject -> {
            val newJson = JsonObject()
            for ((k, v) in json.asJsonObject.entrySet()) {
                newJson.add(k, traverseAndRandomize(v))
            }
            return newJson

        }
        else -> JsonPrimitive("NOT_SUPPORTED")
    }
}

private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

fun JsonArray.randomize() = JsonArray().also { newArray ->
    for (item in this)
        newArray.add(traverseAndRandomize(item))

}

fun JsonPrimitive.randomize(): JsonPrimitive = when {
    this.isString -> JsonPrimitive(Random.nextString())
    this.isBoolean -> JsonPrimitive(Random.nextBoolean())
    this.isNumber -> JsonPrimitive(Random.nextInt())
    else -> JsonPrimitive("NOT_SUPPORTED")
}

fun Random.nextString(length: Int = Random.nextInt(100)) = (0..length)
    .map { Random.nextInt(0, charPool.size) }
    .map(charPool::get)
    .joinToString("");

