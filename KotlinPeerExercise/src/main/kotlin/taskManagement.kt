
// here is your main function. You can use it to call other functions or classes and test your implementation
fun main() {

    // example of creating a person and printing them using the built toString() method of the data class
    val someone = Person("Charlie", 38)
    println(someone)
    someOtherFunction(someone)
}

fun someOtherFunction(person: Person) {
    println("This person's name is ${person.name}.")
}
// enum classes given, not much of a difference to Java
enum class Priority { HIGH, MEDIUM, LOW }
enum class Status { NOT_STARTED, IN_PROGRESS, COMPLETED }

