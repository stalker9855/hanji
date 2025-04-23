package com.dev.hanji.data.events

sealed interface UserEvent {
    data class SetUsername(val username: String): UserEvent
    data object LoadAvatar: UserEvent
    data class UpdateAvatar(val uri: String): UserEvent
    data object InsertUser: UserEvent
}