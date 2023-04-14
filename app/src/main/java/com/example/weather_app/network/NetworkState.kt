package com.example.weather_app.network

import com.example.weather_app.utils.*
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

class NetworkState(val status: Status, val msg: Any? = null, val data: Any? = null) {

    enum class Status {
        RUNNING, FAILED, SUCCESS
    }

    companion object {
        fun getLoaded(dataSuccess: Any?): NetworkState {
            return NetworkState(Status.SUCCESS, data = dataSuccess)
        }

        var LOADING: NetworkState = NetworkState(Status.RUNNING)

        fun getErrorMessage(throwable: Throwable): NetworkState {
            return when (throwable) {
                is IOException -> {
                    NetworkState(Status.FAILED, NoConnection())
                }
                is SocketTimeoutException -> {
                    NetworkState(Status.FAILED, TimeoutException())
                }
                is HttpException -> {
                    when {
                        throwable.code() == 401 ->
                            NetworkState(
                                Status.FAILED,
                                UnAuthorizedException()
                            )
                        throwable.code() == 400 -> NetworkState(Status.FAILED, BadRequest())
                        throwable.code() == 404 -> NetworkState(Status.FAILED, NotFound())
                        throwable.code() == 429 -> NetworkState(Status.FAILED, ManyRequest())
                        else ->
                            NetworkState(
                                Status.FAILED,
                                ServerErrorException()
                            )
                    }
                }
                else -> {
                    NetworkState(Status.FAILED, Exception())
                }
            }
        }
    }
} // class of NetworkStat
