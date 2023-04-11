package ru.starfarm.grpc.client

import io.grpc.CallCredentials
import io.grpc.Channel
import io.grpc.stub.AbstractStub
import ru.starfarm.grpc.client.GrpcBindingStub
import ru.starfarm.grpc.client.GrpcClient
import kotlin.reflect.KProperty

fun connectGrpc(address: Pair<String, Int>, credentials: CallCredentials? = null) =
    GrpcClient.connect(address.first, address.second, credentials)!!

fun <S : AbstractStub<S>> GrpcBindingStub<S>.credentialDelegation() = GrpcBindingStubCredentialDelegate(this)

fun <S : AbstractStub<S>> GrpcClient.delegatingStub(stub: (Channel) -> S) = stub(stub).credentialDelegation()

class GrpcBindingStubCredentialDelegate<S : AbstractStub<S>>(private val stub: GrpcBindingStub<S>) {

    operator fun getValue(s: Any?, property: KProperty<*>) = stub.credential()!!

}