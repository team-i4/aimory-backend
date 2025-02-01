package com.aimory.enums

enum class Role(val value: String) {
    PARENT("ROLE_PARENT"),
    TEACHER("ROLE_TEACHER"),
    ;

    companion object {
        fun getRole(value: String?): Role? {
            return entries.find { it.value == value }
        }
    }
}
