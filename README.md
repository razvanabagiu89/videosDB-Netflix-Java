# videosDB

Student: Abagiu Ioan-Razvan
Group: 321CD
Homework: VideoDB

===============================================================================

For solving the tasks I created two classes:

-MyInput - located in myinput package

This class has a constructor which allocates new memory for the input so that
it won't affect the standard input. By copying the full input I have access to
all the data so that I can add or remove elements to solve more rapidly.

-MyUser - located in user package

This class is an extension to the UserInputData class from 'fileio', having
new fields for storing the ratings a user has given and new functions to
easily iterate through the fields.

In the 'sortfunctions' package I implemented 8 sort functions for sorting
ratings, number of views, number of awards, popularity of genres.

In the 'utils' package I added two functions for initializing the input in
every action and to initialize all users from the database at the start of
every action.

For storing the titles and ratings, number of views, number of occurences
and awards I used Hashmaps due to the unique key advantage.

In Main I used a for loop to cycle through all actions then I called the
following functions by checking the Action Type and its Type:

-> CommandView
 - updates every user's history then checks the current video

-> CommandFavourite
 - updates every user's history and favourite video lists,
then checks the current video

-> CommandRating
 - updates every user's history then checks all command ratings
to get only the valid ones and then checks the current video 

-> IsMovie
 - checks if a movie is in the database, used in IsMovieFilters
-> IsMovieFilters
 - checks if a movie with the given filters is in the
movie database, if the filters are null calls isMovie()
-> IsSerial
 - checks if a serial is in the database, used in isSerialFilters
-> isSerialFilters
 - checks if a serial with the given filters is in the
serial database, if the filters are null calls isSerial()

-> QueryActorsAverage
 - updates every user's history and calculates rating
of every actors for the videos they played in, then sorts.

-> QueryActorsAwards
 - checks if every actor has the requested awards, then
sorts.

-> QueryActorsFilterDescription
 - checks if every actor has the given keywords
in their description, then sorts.

-> QueryDuration
 - stores the duration for movies and sums the total duration
of serials, then sorts with the given filters.

-> QueryFavorite
 - updates every user's history and favourite video lists, then
sorts.

-> QueryMostViewed
 - updates every user's history, counts the occurences of videos
and sorts them.

-> QueryRating
 - updates every user's history and calculates rating for every video,
and sorts them.

-> QueryUser
 - updates every user's history and their ratings, then counts the
most active users and sorts them.

-> RecommendationBestUnseen
 - computes all ratings of videos, sorts them and
recommends the first unseen video to the user in the action.

-> RecommendationFavorite
 - updates every user's history and favourite list, counts
the most occurrences a video has in all user's favourite lists then outputs it.

-> RecommendationPopular
 - updates every user's history then calculates all the
popular genres by their number of views then outputs the first unseen video by
the user in the action.

-> RecommendationSearch
 - updates evey user's history then computes all ratings for
the given genre and outputs all unseen videos from the history of the user in the
action.

-> RecommendationStandard
 - outputs the first unseen video for the user in the action
from the video database.

