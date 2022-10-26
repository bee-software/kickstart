package kickstart

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandler
import java.net.http.HttpResponse.BodyHandlers


fun HttpRequest.Builder.GET(uri: URI): HttpRequest.Builder = uri(uri).GET()


fun HttpClient.send(request: HttpRequest.Builder): HttpResponse<String> {
    return send(request, BodyHandlers.ofString())
}


fun <T> HttpClient.send(request: HttpRequest.Builder, handler: BodyHandler<T>): HttpResponse<T> {
    return send(request.build(), handler)
}
