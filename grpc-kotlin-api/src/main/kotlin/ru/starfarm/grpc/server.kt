package ru.starfarm.grpc.server

import io.grpc.BindableService
import io.grpc.ServerInterceptor
import ru.starfarm.grpc.authorization.credentials.Credentials
import ru.starfarm.grpc.server.GrpcServer

fun startGrpc(port: Int, services: List<BindableService>, interceptors: List<ServerInterceptor> = emptyList()) =
    GrpcServer.bind(port, services, interceptors)!!

fun startGrpc(port: Int, service: BindableService, interceptor: ServerInterceptor) =
    GrpcServer.bind(port, listOf(service), listOf(interceptor))!!

fun startGrpc(port: Int, service: BindableService) =
    GrpcServer.bind(port, listOf(service))!!

fun startGrpcWithBaseAuthorization(port: Int, service: BindableService, authenticator: (Credentials) -> Boolean) =
    GrpcServer.bindWithBaseAuthorization(port, service, authenticator)!!