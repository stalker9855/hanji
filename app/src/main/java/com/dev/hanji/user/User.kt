package com.dev.hanji.user

import androidx.compose.ui.graphics.Color


interface UserInterface {
   val id: Int
   val username: String
   val email: String

   val greatAttempts: Int
   val goodAttempts: Int
   val badAttempts: Int
   val errorAttempts: Int
   val attempts: List<Pair<Int, Color>>
       get() = listOf(
           greatAttempts to Color.Green,
           goodAttempts to Color.Blue,
           badAttempts to Color.Red,
           errorAttempts to Color.Black
       )
}

class User : UserInterface {
    override val id: Int = 1

    override val username: String = "stalker9855"

    override val email: String = "bobross@mail.com"

    override val greatAttempts: Int = 155

    override val goodAttempts: Int  = 333

    override val badAttempts: Int  = 128

    override val errorAttempts: Int = 121

}
