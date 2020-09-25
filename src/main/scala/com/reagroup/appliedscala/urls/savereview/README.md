## POST movies/id/reviews

This one is very similar to `POST movie`, there are some differences however:

1. When saving a review, the client needs to provide the `MovieId` associated with the review.
1. If the `MovieId` is for a movie that does not exist, we want to return an error.
1. We want to collect all errors for the movie and the review in the response

This exercise is made up for three parts. Work through them in order:

### Part 1

1. Review the `ReviewValidationError` ADT (already implemented)
1. Review `ValidatedReview` (already implemented)
1. Review `NewReviewValidator` (already implemented)


### Part 2

1. Review `ReviewId` (already implemented)
1. Implement `SaveReviewService` and get the unit tests to pass


### Part 3
1. Review `SaveReviewController` (already implemented)
1. Wire everything up in `AppRuntime`and `AppRoutes`
1. Get `AppRoutesSpec` to pass
1. Run curls to verify the application has been wired correctly


Start the app using ./auto/start-local and use `curl` to test it out!

#### Invalid movieId, author and comment

```
curl -v -H "Accept: application/json"  -X POST -d "{\"author\": \"\", \"comment\": \"\"}" http://localhost:9200/movies/100/reviews | jq .
```

Should return all errors.

#### Invalid movieId with valid author and comment

```
curl -v -H "Accept: application/json"  -X POST -d "{\"author\": \"The Phantom Reviewer\", \"comment\": \"Boo\"}" http://localhost:9200/movies/100/reviews | jq .
```

Should return an error for movie id only.

#### Valid movieId, author and comment

```
curl -v -H "Accept: application/json"  -X POST -d "{\"author\": \"The Phantom Reviewer\", \"comment\": \"Boo\"}" http://localhost:9200/movies/1/reviews | jq .
```

Should succeed and return a 201 and a review id.

#### Verify that the movie comes back with the reviews

```
curl -v -H "Accept: application/json" http://localhost:9200/movies/1 | jq .
```

Should return the movie and the reviews just created
