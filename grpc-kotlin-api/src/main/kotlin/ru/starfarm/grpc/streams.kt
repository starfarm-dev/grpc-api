package ru.starfarm.grpc.stream

import com.google.protobuf.*
import io.grpc.stub.StreamObserver
import ru.starfarm.grpc.stream.Streams

val Empty = com.google.protobuf.Empty.newBuilder().build()

fun StreamObserver<Empty>.write(close: Boolean = true) =
    write(Empty, close)

fun StreamObserver<BoolValue>.write(value: Boolean, close: Boolean = true) =
    write(BoolValue.newBuilder().setValue(value).build(), close)

fun StreamObserver<Int32Value>.write(value: Int, close: Boolean = true) =
    write(Int32Value.newBuilder().setValue(value).build(), close)

fun StreamObserver<Int64Value>.write(value: Long, close: Boolean = true) =
    write(Int64Value.newBuilder().setValue(value).build(), close)

fun StreamObserver<FloatValue>.write(value: Float, close: Boolean = true) =
    write(FloatValue.newBuilder().setValue(value).build(), close)

fun StreamObserver<DoubleValue>.write(value: Double, close: Boolean = true) =
    write(DoubleValue.newBuilder().setValue(value).build(), close)

fun StreamObserver<StringValue>.write(value: String, close: Boolean = true) =
    write(StringValue.newBuilder().setValue(value).build(), close)

fun <V> StreamObserver<V>.write(collection: Collection<V>, close: Boolean = true) =
    Streams.write(this, collection, close)

fun <V> StreamObserver<V>.write(value: V, close: Boolean = true) =
    Streams.write(this, value, close)

fun <V, C : Collection<V>> Iterator<V>.toCollection(collection: C) =
    Streams.read(this, collection)!!

fun <V> Iterator<V>.toList(): MutableList<V> =
    Streams.readList(this)

fun <V> Iterator<V>.toSet(): MutableSet<V> =
    Streams.readSet(this)

fun <V> forEachObserver(eachHandler: (V) -> Unit, errorHandler: ((Throwable) -> Unit)? = null) =
    Streams.forEach(eachHandler, errorHandler)!!

fun <V> collectObserver(
    handler: (Collection<V>) -> Unit,
    errorHandler: ((Collection<V>, Throwable) -> Unit)? = null
) = Streams.collect(handler, errorHandler)!!