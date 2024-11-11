// Data Class example
// In Java, this would require numerous lines of boilerplate code
data class Person(
    val name: String,
    val age: Int
) {
    // All equals(), hashCode(), toString() are automatically generated
}

// Null Safety - Kotlin's type system distinguishes between nullable and non-null types
var nonNullString: String = "Hello"
var nullableString: String? = null

// Safe call operator
val length = nullableString?.length // Returns null if nullableString is null

// Elvis operator, returns the default value defined (here 0) if the string is null
val lengthOrDefault = nullableString?.length ?: 0

// Example Lambda functions with the "it" keyword
// Applied to a collection to quickly process all collection members
val numbers = listOf(1, 2, 3, 4, 5)
val doubled = numbers.map { it * 2 }
val filtered = numbers.filter { it > 3 }

