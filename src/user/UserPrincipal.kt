package no.echokarriere.user

import io.ktor.auth.Principal

class UserPrincipal(val user: UserEntity) : Principal
