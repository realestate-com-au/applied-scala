# Intro to Scala Refresher

You are expected to have completed [Intro to Scala](https://github.com/wjlow/intro-to-scala) in some form before attending this course.

This document serves as a refresher for the Intro to Scala material.

Check out the [cheat sheet in Intro to Scala for a more comprehensive rundown](https://github.com/wjlow/intro-to-scala/blob/master/cheat-sheet.md).

## Functions

[Definition](https://github.com/wjlow/intro-to-scala/blob/master/cheat-sheet.md#what-are-pure-functions)

### What isn't a function:

```scala
def updateRobot(robot: Robot, command: Command): Unit
```

This function presumably mutates the state of `robot` based on what `command` is. You can make this assumption based on the return type of `Unit`, which often indicates an unadvertised side-effect.

Calling this function multiple times on the same `robot` gives you a different program.

### What is a function:

```scala
def updateRobot(robot: Robot, command: Command): Robot
```

This function presumably creates a value of type `Robot` based on the value of `robot` and `command`. 

Calling this function multiple times on the same `robot` _always_ gives you the same program.

## Algebraic Data Types (ADTs)

```scala
// Scala 3
enum UserId {
  case SignedInUserId(value: String)
  case AnonymousUserId(value: String)
}

// Scala 2.13 and earlier
sealed trait UserId // data type 

case class SignedInUserId(value: String) extends UserId // data constructor

case class AnonymousUserId(value: String) extends UserId // data constructor
```

Here is an ADT that represents a valid `UserId`. 

We often create safe constructors because not all `String`s are valid `UserId`s.

```scala
def mkUserId(str: String): Option[UserId] =
  if (str.length == 10) Some(SignedInUserId(str))
  else if (str.length == 20) Some(AnonymousUserId(str))
  else None
```

### Pattern matching on ADTs

We pattern match on the _constructors_ of the ADTs.

```scala
def describeUserId(userId: UserId): String = 
  userId match {
    case SignedInUserId(value)  => s"Signed in user with id: $value" 
    case AnonymousUserId(value) => s"Anonymous user with id: $value"
  }
```

## Option

```scala
sealed trait Option[A]
case class  Some(a: A) extends Option[A]
case object None       extends Option[Nothing]
```

Pattern match on the _constructors_

```scala
maybeNumber match {
  case Some(num) => s"You got a number, and it is: $num"
  case None      => "You do not have a number"
}
```

We use `Option` to represent _non-existence_. Think of it as the functional equivalent of `null` (Java), `nil` (Ruby) or `undefined` (JavaScript).

## Either

```scala
sealed trait Either[E, A]
case class Right(a: A) extends Either[Nothing, A]
case class Left(e: E)  extends Either[E, Nothing]
```

Pattern match on the _constructors_

```scala
errorOrNumber match {
  case Right(num)  => s"You got a number, and it is: $num"
  case Left(err)   => s"You do not have a number, because of some error: ${err.getMessage}"
}
```

By convention, we use `Either` for handling errors. The `Left` type would have some sort of `Error` type and the `Right` type would have some sort of success value.

Think of `Either` as the functional equivalent of raising or throwing an exception. Instead of `throw SQLError`, we would return `Left(SqlError)`.

## map

For converting the "inner type":

```scala
F[A].map(A => B) // F[B]
```

For example:

```scala
val maybePerson: Option[Person] = findPerson(id)
maybePerson.map(person => person.firstName) // Option[FirstName]
```

```scala
val errorOrPerson: Either[Error, Person] = fetchPerson(id)
errorOrPerson.map(person => person.firstName) // Either[Error, FirstName]
```

[Further reading](https://github.com/wjlow/intro-to-scala/blob/master/cheat-sheet.md#1-functor)

## flatMap

For chaining multiple operations resulting in the same structure:

```scala
F[A].flatMap(A => F[B]) // F[B]
```

For example:

```scala
val maybePerson: Option[Person] = findPerson(id)
maybePerson.flatMap(person => fetchJob(person.jobId)) // Option[Job]
```

```scala
val errorOrUserId: Either[Error, UserId] = parseUserId("abc123") 
errorOrUserId.flatMap(userId => fetchPerson(userId)) // Either[Error, Person]
```

[Further reading](https://github.com/wjlow/intro-to-scala/blob/master/cheat-sheet.md#3-monad)

## for-comprehension

To chain multiple `flatMap`s and `map`s, a lot of people prefer using for-comprehension.

```scala
for {
  userId <- parseUserId("abc123")
  person <- fetchPerson(userId)
  job    <- fetchJob(person.jobId)
} yield job.description

// Either[Error, JobDescription]
```

This is equivalent to:

```scala
parseUserId("abc123")
  .flatMap(userId => fetchPerson(userId)
    .flatMap(person => fetchJob(person.jobId)
      .map(job => job.description)))
```

Every expression in the for-comprehension must return the same outer structure, e.g. `Option` or `Either[Error, ?]`. 

Every line but the final line is a `flatMap` and the final line is a `map`.
