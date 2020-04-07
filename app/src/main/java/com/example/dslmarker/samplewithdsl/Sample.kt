package com.example.dslmarker.samplewithdsl

import java.text.SimpleDateFormat
import java.util.*

/**
 * Dsl stands for Domain Specific Languages
 */
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
            name = "new name for person" // Now you get compiling error
            this@person.name = "new name for person" // This would work
        }
    }

    print(person.name)
}

@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
annotation class PersonDsl

private inline fun person(block: (@PersonDsl PersonBuilder).() -> Unit): Person = PersonBuilder().apply(block).build()

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

    fun address(block: (@PersonDsl AddressBuilder).() -> Unit) {
        addresses.add(AddressBuilder().apply(block).build())
    }

    fun build(): Person = Person(name, dob, addresses)

}