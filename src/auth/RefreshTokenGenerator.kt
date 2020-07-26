package no.echokarriere.auth

import java.security.SecureRandom
import java.util.Base64

fun ByteArray.encodeAsBase64(): String = Base64.getEncoder().encodeToString(this)

fun SecureRandom.generateByteArray(num: Int): ByteArray {
    val bytes = ByteArray(num)
    this.nextBytes(bytes)
    return bytes
}
