package com.example.dslmarker.sample1

import java.text.SimpleDateFormat
import java.util.*

fun main() {
    val person: Person = person {
        name = "person"
        dateOfBirth = "1980-12-01"

        address {
            street = "Main Street"
            number = 12
            city = "London"
        }

        address {
            street = "Main Street"
            number = 12
            city = "London"
            name = "new name for person" // Name should not be accessible from here. How to narrow the scope? See sample 2
        }
    }

    print(person.name)
}

private inline fun person(block: PersonBuilder.() -> Unit): Person = PersonBuilder().apply(block).build()

private data class Person(val name: String,
                          val dateOfBirth: Date,
                          var addresses: List<Address>)

private data class Address(val street: String,
                           val number: Int,
                           val city: String)

private class AddressBuilder {
    var street: String = ""
    var number: Int = 0
    var city: String = ""

    fun build() : Address = Address(street, number, city)

}

private class PersonBuilder {

    var name: String = ""

    private var dob: Date = Date()
    var dateOfBirth: String = ""
        set(value) {
            dob = SimpleDateFormat("yyyy-MM-dd").parse(value)
        }

    private val addresses = mutableListOf<Address>()

    fun address(block: AddressBuilder.() -> Unit) {
        addresses.add(AddressBuilder().apply(block).build())
    }

    fun build(): Person = Person(name, dob, addresses)

}