package ru.starfarm.grpc.authorization

import ru.starfarm.grpc.authorization.credentials.BasicCredentials

fun basicCredentials(login: String, token: String) = BasicCredentials.of(login, token)!!

