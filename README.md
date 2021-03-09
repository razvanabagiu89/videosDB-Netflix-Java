# videosDB/Netflix in Java

## About

https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/tema

Build a video database system in which users have multiple subscription types, they can give ratings and receive recommendations based on their behaviour on the platform.

## How to run the tests

- run Test class under the main package


## Structure

### For solving the tasks I created two parent classes:

- MyInput - located in myinput package

This class has a constructor which allocates new memory for the input so that
it won't affect the standard input. By copying the full input I have access to
all the data so that I can add or remove elements to solve more rapidly.

- MyUser - located in user package

This class is an extension to the UserInputData class from 'fileio', having
new fields for storing the ratings a user has given and new functions to
easily iterate through the fields.

### Packages

- In the 'sortfunctions' package I implemented 8 sort functions for sorting
ratings, number of views, number of awards, popularity of genres.

- In the 'utils' package I added two functions for initializing the input in
every action and to initialize all users from the database at the start of
every action.

- In the 'constants' package I added a constant for awards.

### Methods used

For storing the titles and ratings, number of views, number of occurences
and awards I used Hashmaps due to the unique key advantage.

In Main I used a for loop to cycle through all actions then I called the
following functions by checking the Action Type and its Type:

#### CommandView
 - updates every user's history then checks the current video

#### CommandFavourite
 - updates every user's history and favourite video lists,
then checks the current video

#### CommandRating
 - updates every user's history then checks all command ratings
to get only the valid ones and then checks the current video 

#### IsMovie
 - checks if a movie is in the database, used in IsMovieFilters
#### IsMovieFilters
 - checks if a movie with the given filters is in the
movie database, if the filters are null calls isMovie()
#### IsSerial
 - checks if a serial is in the database, used in isSerialFilters
#### isSerialFilters
 - checks if a serial with the given filters is in the
serial database, if the filters are null calls isSerial()

#### QueryActorsAverage
 - updates every user's history and calculates rating
of every actors for the videos they played in, then sorts.

#### QueryActorsAwards
 - checks if every actor has the requested awards, then
sorts.

#### QueryActorsFilterDescription
 - checks if every actor has the given keywords
in their description, then sorts.

#### QueryDuration
 - stores the duration for movies and sums the total duration
of serials, then sorts with the given filters.

#### QueryFavorite
 - updates every user's history and favourite video lists, then
sorts.

#### QueryMostViewed
 - updates every user's history, counts the occurences of videos
and sorts them.

#### QueryRating
 - updates every user's history and calculates rating for every video,
and sorts them.

#### QueryUser
 - updates every user's history and their ratings, then counts the
most active users and sorts them.

#### RecommendationBestUnseen
 - computes all ratings of videos, sorts them and
recommends the first unseen video to the user in the action.

#### RecommendationFavorite
 - updates every user's history and favourite list, counts
the most occurrences a video has in all user's favourite lists then outputs it.

#### RecommendationPopular
 - updates every user's history then calculates all the
popular genres by their number of views then outputs the first unseen video by
the user in the action.

#### RecommendationSearch
 - updates evey user's history then computes all ratings for
the given genre and outputs all unseen videos from the history of the user in the
action.

#### RecommendationStandard
 - outputs the first unseen video for the user in the action
from the video database.

## Mentions

This project uses Google checkstyle in order to maintain cleanliness of the code.

## Project structure including tests
```bash
├── checkstyle.txt
├── libs
│   ├── jackson-annotations-2.9.3.jar
│   ├── jackson-core-2.9.3.jar
│   ├── jackson-databind-2.9.9.3.jar
│   ├── json-20140107.jar
│   ├── json-lib-2.4-jdk15.jar
│   └── org.json.simple-0.3-incubating.jar
├── out
│   └── production
│       └── Tema-POO-2020
│           ├── actor
│           │   └── ActorsAwards.class
│           ├── checker
│           │   ├── Checker$1.class
│           │   ├── Checker.class
│           │   ├── checkstyle-8.36.2-all.jar
│           │   ├── Checkstyle.class
│           │   ├── checkstyle.sh
│           │   ├── poo_checks.xml
│           │   └── QueryTest.class
│           ├── common
│           │   └── Constants.class
│           ├── entertainment
│           │   ├── Genre.class
│           │   └── Season.class
│           ├── fileio
│           │   ├── ActionInputData.class
│           │   ├── ActorInputData.class
│           │   ├── Input.class
│           │   ├── InputLoader.class
│           │   ├── MovieInputData.class
│           │   ├── SerialInputData.class
│           │   ├── ShowInput.class
│           │   ├── UserInputData.class
│           │   └── Writer.class
│           ├── main
│           │   ├── Main.class
│           │   └── Test.class
│           └── utils
│               └── Utils.class
├── out.txt
├── README.md
├── ref
│   ├── ref_favorite_fail_duplicate.json
│   ├── ref_favorite_fail_invalid.json
│   ├── ref_large_test_no_1.json
│   ├── ref_large_test_no_2.json
│   ├── ref_large_test_no_3.json
│   ├── ref_large_test_no_4.json
│   ├── ref_large_test_no_5.json
│   ├── ref_large_test_no_6.json
│   ├── ref_large_test_no_7.json
│   ├── ref_large_test_no_8.json
│   ├── ref_large_test_no_9.json
│   ├── ref_query_no_values.json
│   ├── ref_recommendation_invalid_user.json
│   ├── ref_recommendation_no_values.json
│   ├── ref_single_command_favorite.json
│   ├── ref_single_command_rating_movie.json
│   ├── ref_single_command_rating_show.json
│   ├── ref_single_command_view.json
│   ├── ref_single_query_average_actors.json
│   ├── ref_single_query_awards_actors.json
│   ├── ref_single_query_favorite_movie.json
│   ├── ref_single_query_favorite_show.json
│   ├── ref_single_query_filter_description_actors.json
│   ├── ref_single_query_longest_movie.json
│   ├── ref_single_query_longest_show.json
│   ├── ref_single_query_most_viewed_movie.json
│   ├── ref_single_query_most_viewed_show.json
│   ├── ref_single_query_rating_movie.json
│   ├── ref_single_query_rating_show.json
│   ├── ref_single_query_users.json
│   ├── ref_single_recommendation_best_unseen.json
│   ├── ref_single_recommendation_favorite.json
│   ├── ref_single_recommendation_popular.json
│   ├── ref_single_recommendation_search.json
│   └── ref_single_recommendation_standard.json
├── result
│   ├── out_favorite_fail_duplicate.json
│   ├── out_favorite_fail_invalid.json
│   ├── out_large_test_no_1.json
│   ├── out_large_test_no_2.json
│   ├── out_large_test_no_3.json
│   ├── out_large_test_no_4.json
│   ├── out_large_test_no_5.json
│   ├── out_large_test_no_6.json
│   ├── out_large_test_no_7.json
│   ├── out_large_test_no_8.json
│   ├── out_large_test_no_9.json
│   ├── out_query_no_values.json
│   ├── out_recommendation_invalid_user.json
│   ├── out_recommendation_no_values.json
│   ├── out_single_command_favorite.json
│   ├── out_single_command_rating_movie.json
│   ├── out_single_command_rating_show.json
│   ├── out_single_command_view.json
│   ├── out_single_query_average_actors.json
│   ├── out_single_query_awards_actors.json
│   ├── out_single_query_favorite_movie.json
│   ├── out_single_query_favorite_show.json
│   ├── out_single_query_filter_description_actors.json
│   ├── out_single_query_longest_movie.json
│   ├── out_single_query_longest_show.json
│   ├── out_single_query_most_viewed_movie.json
│   ├── out_single_query_most_viewed_show.json
│   ├── out_single_query_rating_movie.json
│   ├── out_single_query_rating_show.json
│   ├── out_single_query_users.json
│   ├── out_single_recommendation_best_unseen.json
│   ├── out_single_recommendation_favorite.json
│   ├── out_single_recommendation_popular.json
│   ├── out_single_recommendation_search.json
│   └── out_single_recommendation_standard.json
├── src
│   ├── actor
│   │   └── ActorsAwards.java
│   ├── checker
│   │   ├── Checker.java
│   │   ├── checkstyle-8.36.2-all.jar
│   │   ├── Checkstyle.java
│   │   ├── checkstyle.sh
│   │   ├── poo_checks.xml
│   │   └── QueryTest.java
│   ├── common
│   │   └── Constants.java
│   ├── entertainment
│   │   ├── Genre.java
│   │   └── Season.java
│   ├── fileio
│   │   ├── ActionInputData.java
│   │   ├── ActorInputData.java
│   │   ├── Input.java
│   │   ├── InputLoader.java
│   │   ├── MovieInputData.java
│   │   ├── SerialInputData.java
│   │   ├── ShowInput.java
│   │   ├── UserInputData.java
│   │   └── Writer.java
│   ├── functions
│   │   ├── CommandFavourite.java
│   │   ├── CommandRating.java
│   │   ├── CommandView.java
│   │   ├── IsMovieFilters.java
│   │   ├── IsMovie.java
│   │   ├── IsSerialFilters.java
│   │   ├── IsSerial.java
│   │   ├── QueryActorsAverage.java
│   │   ├── QueryActorsAwards.java
│   │   ├── QueryActorsFilterDescription.java
│   │   ├── QueryDuration.java
│   │   ├── QueryFavorite.java
│   │   ├── QueryMostViewed.java
│   │   ├── QueryRating.java
│   │   ├── QueryUser.java
│   │   ├── RecommendationBestUnseen.java
│   │   ├── RecommendationFavorite.java
│   │   ├── RecommendationPopular.java
│   │   ├── RecommendationSearch.java
│   │   └── RecommendationStandard.java
│   ├── main
│   │   ├── Main.java
│   │   └── Test.java
│   ├── myinput
│   │   └── MyInput.java
│   ├── sortfunctions
│   │   ├── MapDouble.java
│   │   ├── MapInteger.java
│   │   ├── QueryRatingSearch2.java
│   │   ├── QueryRatingSearch.java
│   │   └── SortSearch.java
│   ├── user
│   │   └── MyUser.java
│   └── utils
│       └── Utils.java
├── Tema-POO-2020.iml
└── test_db
    └── test_files
        ├── favorite_fail_duplicate.json
        ├── favorite_fail_invalid.json
        ├── large_test_no_1.json
        ├── large_test_no_2.json
        ├── large_test_no_3.json
        ├── large_test_no_4.json
        ├── large_test_no_5.json
        ├── large_test_no_6.json
        ├── large_test_no_7.json
        ├── large_test_no_8.json
        ├── large_test_no_9.json
        ├── query_no_values.json
        ├── recommendation_invalid_user.json
        ├── recommendation_no_values.json
        ├── single_command_favorite.json
        ├── single_command_rating_movie.json
        ├── single_command_rating_show.json
        ├── single_command_view.json
        ├── single_query_average_actors.json
        ├── single_query_awards_actors.json
        ├── single_query_favorite_movie.json
        ├── single_query_favorite_show.json
        ├── single_query_filter_description_actors.json
        ├── single_query_longest_movie.json
        ├── single_query_longest_show.json
        ├── single_query_most_viewed_movie.json
        ├── single_query_most_viewed_show.json
        ├── single_query_rating_movie.json
        ├── single_query_rating_show.json
        ├── single_query_users.json
        ├── single_recommendation_best_unseen.json
        ├── single_recommendation_favorite.json
        ├── single_recommendation_popular.json
        ├── single_recommendation_search.json
        └── single_recommendation_standard.json


```
