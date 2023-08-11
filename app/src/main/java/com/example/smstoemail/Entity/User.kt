package com.example.smstoemail.Entity

data class User(val googleId: String, val name: String, val email: String)

// Inside the /validateAndCreateUser route handler
//val user = User(googleId = validatedGoogleId, name = validatedName, email = validatedEmail)

// Store the user in your database or user management system
// Example: userDatabase.saveUser(user)