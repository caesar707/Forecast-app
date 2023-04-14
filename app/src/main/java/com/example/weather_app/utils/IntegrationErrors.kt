package com.example.weather_app.utils

class ServerErrorException : Exception()
class UnAuthorizedException : Exception()
class ServerUnreachableException() : Exception()
class BadRequest : Exception()
class NotFound : Exception()
class ManyRequest : Exception()
class NoConnection() : Exception()