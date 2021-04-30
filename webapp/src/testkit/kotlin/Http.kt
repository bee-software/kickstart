package kickstart

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

fun HttpRequest.Builder.GET(uri: URI): HttpRequest.Builder = this.uri(uri).GET()

fun HttpClient.send(request: HttpRequest.Builder) = this.send(request, HttpResponse.BodyHandlers.ofString())

fun <T> HttpClient.send(request: HttpRequest.Builder, handler: HttpResponse.BodyHandler<T>): HttpResponse<T> =
    this.send(request.build(), handler)
