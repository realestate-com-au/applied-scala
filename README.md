# applied-scala

[![Build Status](https://travis-ci.org/realestate-com-au/applied-scala.svg?branch=master)](https://travis-ci.org/github/realestate-com-au/applied-scala)

## Getting Started

Similar to [Intro to Scala](https://github.com/wjlow/intro-to-scala#pre-requisites)

1. Skip this step if you have already done Intro to Scala on your current machine. If you're going to use [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) (Community edition is fine), you need to install Java 11 even if you have a newer version of Java installed.

```
brew tap AdoptOpenJDK/openjdk
brew cask install adoptopenjdk11
```

2. Before the course, please run the following:

```
git clone git@github.com:zendesk/applied-scala.git
cd applied-scala
./auto/test
./auto/start-local
```

This should start the app. Note that the tests should be failing at this point.

3. Now test this out in a new tab.

```
curl http://localhost:9200/movies
```

You should get back `[{"name":"Titanic"}]`. Now press `ctrl+c` in the previous tab to shut down the app.

4. Open up the project in IntelliJ IDEA and make sure it all compiles. Now you're ready to go!

## Open up SBT

Using Docker
```
./auto/sbt
```

or

Using portable SBT
```
./sbt
```

## Run test

```
./auto/test
```

## How to start app

```
./auto/start-local
```

## Suggested Format

### Day 1

- IO Exercises
- Http4s overview + Endpoint 1: Hello World
- Circe Exercises
- [Code walkthrough: GET all movies (no exercises)](./src/main/scala/com/reagroup/appliedscala/urls/fetchallmovies/README.md) 
- [Endpoint 2: GET movie](./src/main/scala/com/reagroup/appliedscala/urls/fetchmovie/README.md)
- [Endpoint 3: GET movie?enriched=true](./src/main/scala/com/reagroup/appliedscala/urls/fetchenrichedmovie/README.md)

### Day 2

- Validated Exercises
- [Endpoint 4: POST movie](./src/main/scala/com/reagroup/appliedscala/urls/savemovie/README.md)
- [Endpoint 5: POST movie/id/review](./src/main/scala/com/reagroup/appliedscala/urls/savereview/README.md)
- Wrap up

## Further reading

- [FAQ](docs/faq.md)
- [Scala Refresher](docs/refresher.md)

## Test queries

Fetch all movies
```
$ curl http://localhost:9200/movies
```

Fetch movie
```
$ curl http://localhost:9200/movies/1
```

Fetch enriched movie

```
$ curl http://localhost:9200/movies/1?enriched=true
```

Save movie

1. Successful save
```
$ curl -H "Accept: application/json" -X POST -d "{\"name\": \"Cars 3\", \"synopsis\": \"Great movie about cars\"}" http://localhost:9200/movies
```

2. Validation errors
```
$ curl -H "Accept: application/json" -X POST -d "{\"name\": \"\", \"synopsis\": \"\"}" http://localhost:9200/movies
```

Save review

1. Successful save
```
$ curl -H "Accept: application/json"  -X POST -d "{\"author\": \"Jack\", \"comment\": \"Great movie huh\"}" http://localhost:9200/movies/1/reviews
```

2. Validation errors

```
$ curl -H "Accept: application/json"  -X POST -d "{\"author\": \"\", \"comment\": \"\"}" http://localhost:9200/movies/1/reviews
```
