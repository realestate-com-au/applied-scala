# Cats

## Should only cats-effects be used from the cats libraries? Are others considered good/bad?

Most libraries under the [Typelevel](https://typelevel.org/) family of libraries work well with Cats and are well written. Cats Effect is one of them. It is good for doing effectful programming (working with side-effects, etc.). Monix Task is another good one but it has a lot more features that we do not need in this course.

# IO

## Should Logging be in IO?

100% yes. Otherwise you are going to log while you're constructing your program, instead of logging when the program runs. 

## What is the difference between `IO.pure` and `IO.apply`?

Short answer: If you have an expression that can throw exceptions (all side-effecting calls, e.g. `println`), use `IO.apply`. For everything else, use `IO.pure` (e.g. `IO.pure(123)`).

Long answer: Scala is a strict language, which means that the arguments are evaluated before they are passed into the functions. If you use `IO.pure` for an expression that may throw an exception, e.g. `IO.pure(println("abc"))`, the `println` will be run before it is passed into the `IO`. This means it can also throw an exception outside of the `IO` and terminate your program! You need to use `IO.apply` to "suspend/delay" the expression.

So shouldn't we just use `IO.apply` for everything? If you did - you'd be fine. But they both come from different abstractions and understanding the difference will allow you to take your FP knowledge to the next level (not covered in this course, sorry!).

`pure` is a function that is defined on all data types that have an `Applicative` instance. `delay` function that is defined on the `Sync` instance, which is a much more powerful abstraction than `Applicative`. The `Sync` instance of `IO` uses `IO.apply` to implement `delay`.

When you start programming to typeclasses instead of concrete data types, knowing the difference allows you to write code that can be tested more easily. 

This blog post by Ken Scambler briefly touches upon this: [The worst thing in our Scala code: Futures](https://www.rea-group.com/blog/the-worst-thing-in-our-scala-code-futures/)

# http4s

## Does http4s follow a reactive programming model or does it block?

http4s is built on top of the fs2 ("functional streams for Scala") library. fs2 uses Java's NIO2 library for TCP/IP connections, which has AsynchronousSocketChannel and AsynchronousServerSocketChannel classes which have methods that can accept a callback or return a Future.

So it's not reactive in the same sense as ReactiveX or similar libraries, but it does use non-blocking I/O, and working with fs2's Stream type is similar to working with Observables.

# Misc

## What's wrong with mocks?

[Ken Scambler - To Kill a Mockingtest](https://www.rea-group.com/blog/to-kill-a-mockingtest/)

[Ken Scambler - Mocks and Stubs from MelbJVM](https://www.youtube.com/watch?v=EaxDl5NPuCA)

## Why don't we use a dependency injection framework instead of having to create everything manually?

In short, we don't want our app to take a few minutes to start up, and then fail because it cannot find a dependency! We want these problems detected at compile time.

This talk by Ken Scambler may give you some more insight.

[Ken Scambler - Responsible DI](https://www.youtube.com/watch?v=YMII3Lki9uo)

## When should we use implicits?

There are two good usages of implicits.

1. When creating a typeclass instance.

```
implicit val personEncoder: Encoder[Person] = ...
```

2. When creating extension methods. You rarely have to do so unless you're writing a library. Extension methods add methods to pre-existing types, for instance being able to do `.asJson` or a `String`.

## What is a `Kleisli`?

`Kleisli` is a data type that takes 3 type parameters, `F[_], A, B`. It is a function `A => F[B]`.

Some concrete examples?

`Kleisli[IO, UserId, User]` is the same as `UserId => IO[User]`
`Kleisli[Option, PropertyId, Property]` is the same as `PropertyId => Option[Property]`

Why does it even exist? It's because there are useful combinators/functions that has been implemented on Kleisli.

## What are Functor, Applicative and Monad?

[Read here](https://github.com/wjlow/intro-to-scala/blob/master/cheat-sheet.md#what-are-functor-applicative-and-monad)

## Implicits

Use implicit parameters only for typeclasses.

Example:

- Monad
- Functor
- Encoder
- Decoder

Don't use implicits because you're too lazy to pass an argument in explicitly.​

[Further reading](https://docs.scala-lang.org/tutorials/FAQ/finding-implicits.html)

## Monoids - Addition / Subtraction of numbers 

Subtraction of numbers is NOT a monoid
`1 - 0` != `0 - 1`

Addition of negative numbers IS still a monoid!

`-1 + 0` = `0 + -1` = `-1`

`-1 + (-2 + -3)` = `(-1 + -2) + -3`
`-1 + -5` = `-3 + -3`
`-6` = `-6`

# IO

## Http4s types 

_What is IO[Response[IO]]?_

The inner `Response[IO]` contains the response `body` which has the type `EntityBody[IO]` which is an alias for `Stream[IO, Byte]` (`Stream` here belongs to a library called `fs2`). This `Stream` can then be converted to an `IO[...]`, which is a pure value _describing_ a potential side-effect. Therefore, the `IO` in `Response[IO]` allows the body to converted to an instance of `IO`.

As for the outer `IO` wrapping `Response[IO]`, the reason given is it allows the many routes to be  more efficiently combined into "one big route" (via a typeclass called `SemigroupK`). This "one big route" functions similarly to the many routes we defined, in that an incoming request will try to be matched to the correct handler.

You're writing a function that takes a request and returns a response. The function can perform effects (like talking to PostgreSQL and the OMDB API), so we wrap the return type in `IO` so we can represent those effects as values.
But when you're reading the request body, or writing the response body, you might need to perform effects too.

The example I like to use is the "give me all of the buy listings" from the Listing API. We don't load all the listings into memory and then turn them into bytes. We write one listing at a time, interleaving reading a row from the database with writing its JSON representation into the response. _http4s and fs2 make this very easy._ Because we're still using the database, we need our `IO` type in the `Stream[IO, Byte]`; and then, because the `Response` class contains that `Stream`, `Response` needs `IO` too.

TL;DR: "outer" `IO`: effects to decide how to respond. "inner" `IO`: effects to produce the response stream.

# Either

## Using .sequence on a List[Either[Error, Int]]

Say you have List[Either[E, Int]], for example it will be like `[Right(3), Left(error1), Left(error2)]`, when you do sequence you will get Either[E, List[Int]], so what happens to all the errors in the list, are they squashed in some kind way?

If you open up `./auto/sbt console`, you can test this out:

```scala
scala> import cats.implicits._
import cats.implicits._

scala> val list: List[Either[String,Int]] = List(Right(1), Left("error1"), Right(2), Left("error2"))
list: List[Either[String,Int]] = List(Right(1), Left(error1), Right(2))

scala> list.sequence
res0: Either[String,List[Int]] = Left(error1)
```

also see: [Further reading](https://typelevel.org/cats/typeclasses/traverse.html#a-note-on-sequencing)

## What responsibilities does a Service have? [Repo, Controller]

_FetchMovieService for example, seems to only serve as a wire-up between controller and repository. What other responsibilities usually go inside a Service?_

You may fetch a Listing from a DB, and then fetch the images from an API. The definition of these 2 calls would be in the Service. For the rest of the course, we'll see more code in the Services.
