*Designed by Marius-Tudor Zaharia, 323CA, December 2023 - January 2024*

# GlobalWaves - stage 3

---

## Table of contents
1. [What is GlobalWaves?](#what-is-globalwaves)
2. [What is new?](#what-is-new)
3. [I/O and running](#io-and-running)
4. [Implementation details](#implementation-details)
   * [Design choices](#design-choices)
   * [Design patterns used](#design-patterns-used)
   * [Program flow](#program-flow)

---

## What is GlobalWaves?
* GlobalWaves is a music and podcast streaming service backend written in the
Java language.
* This is the third and final stage of the main project, extending the first
two parts, so the present will mostly focus on the new functionalities, while
more details on the previous stages can be found
[here](https://github.com/tudorz23/GlobalWaves-stage2.git).

---

## What is new?
* The current stage saw a good share of additions, mainly:
  * statistics called GlobalWaves Wrapped, regarding the most listened songs,
  albums, artists and more for every user;
  * monetization for artists, based on revenue coming from users that can now
  buy premium subscriptions, from ads for those that use the free version of
  the platform and from merch buying by the fans;
  * a notification system, which allows users to get messages when one of their
  playlists gets a new follower or when one of the content creators they
  subscribe to adds a new album/podcast, a new event/announcement or new pieces
  of merchandise;
  * recommendations for users, based on their listening habits;
  * the ability to navigate back and forward on the pages.

---

## I/O and Running
* It remains unchanged from stage 1, with the **Jackson** library still being
used for parsing the JSON format of input and output files.
* To run the tests, the `main()` method from the **Main** class should be run.

---

## Implementation Details
### Design choices
* To add support for the `Wrapped` command, classes of type `Analytics` were
added, which contain Maps that are constantly updated throughout the program,
so for the `Wrapped` command per se, it is only necessary to sort the Maps
according to their types and print out the results.
* Similarly, data for the `Monetization` command is being constantly updated,
with every artist that is getting a revenue being registered in the
`monetizedArtists` map, which contains specific details regarding his revenue.
* For handling the `Notification` functionality, a new `user` type abstract
class was added, `ContentCreator`, offering an easy way to store the list of
creators a user is subscribed to.
* A new utility class, `MapOperations`, was added, containing static methods
for operations applicable on maps. Map sorting is done using `streams`,
`Comparators` and `Collectors`, the sorted entries being stored in
`LinkedHashMaps` for a predictable order of iteration. Duplicate keys are
handled using `merge()` function, for example, to add the listens of
duplicate songs.
* **Generics** are used in the `MapOperations`, while **wildcards** are used
in the `HomePage` class, encouraging code reusability.
* Java `records` (package `database.records`) and `enums` (package
`utils.enums`) are still used, making the code more elegant.
* Like in the previous stages, `exceptions` have been used in many places
for a smoother error handling.
* Lists are sorted using `Comparator` and method reference operator.
* For advertisements, the time passing simulation system was updated, allowing
the introduction of the ad in the player while storing the previously playing
`Audio`, thus being able to reintroduce it after the ad ends.

---

### Design Patterns Used
#### Command Pattern
* Used for separating the implementations of different actions.
* Based on the usage of the `ICommand` interface, which exposes the `execute()`
method, thus abstracting the use of a command.
* The `AdminInteraction` class iterates through the commands given as input
and calls the `execute()` method of the `Invoker` class, which, in turn,
calls the `execute()` method of `ICommand` interface.
* The Invoker provides separation between the commands and the client code that
uses them, in this case, the `AdminInteraction`.
* It is very useful for adding new functionalities, as each one is implemented
separately, and it also becomes easier to solve possibly occurring problems.

#### Strategy Pattern
* Used for diverging in implementation between various ways of executing a
certain action.
* It also makes the code easier to maintain and debug, as different approaches
to a task are written in different classes.
* Used in multiple places:
  * For different ways of searching on the searchbar (song, album, podcast,
    playlist, artist, host). Based on `ISearchStrategy` interface with
    `search()` method.
  * For different ways of deleting a user (basic user, artist, host). Based on
    `IDeleteUserStrategy` interface with `deleteUser()` method.
  * For different recommendations heuristics (fans playlist, random playlist,
    random song). Based on `IRecommendationStrategy` interface with
    `recommend()` method.
  * For different types of wrapped statistics (artist, host, basic user). Based
    on `IWrappedStrategy` interface with `wrapped()` method.

#### Factory Pattern
* Used for generating concrete instances of an abstract type, based on a
request.
* Used factory methods for creating concrete:
  * `ICommand` instances in the `CommandFactory` class.
  * `IDeleteUserStrategy` instances in the `DeleteUserCommand` class.
  * `IRecommendationStrategy` instances in the `UpdateRecommendationsCommand`
    class.
  * `ISearchStrategy` instances in the `SearchCommand` class.
  * `IWrappedStrategy` instances in the `WrappedCommand` class.
  * `Page` instances in the `PageFactory` class.

#### Observer Pattern
* Based on two interfaces, `IObserver`, with the `update()` method, and
`IObservable`, with the `addObserver()`, `removeObserver()` and
`notifyObservers()` methods.
* Grants an easy to use and maintain mechanism of sharing notifications to
the *observers* when an event comes from the *observable*, as the `update()`
method is called automatically.
* Used for managing the `Notification` functionality.
* The *observer* is the `User` class, while the *observables* are `Playlist`
and `ContentCreator` (i.e. `Artist` and `Host` through inheritance) classes.
* The *observable* keeps a list of its *observers* and notifies each of them
in case of certain events happening.

---

### Program Flow
* Similarly to previous stages, `AdminInteraction` class is the main client,
iterating through the input commands and using the `CommandFactory` to generate
commands, while `Invoker` serves as an intermediary to call `execute()` method.
* Depending on the type of command, actions are performed, usually modifying
the state of the `Player` instance of one `User`, updating the analytics or
printing statistics.
* Before a new command is applied, almost always the time passing is simulated
using the polymorphic method `simulateTimePass()`.
* The output is then appended using specialized `Printer` objects.
* After handling every command, the monetization is updated for the users that
still have premium subscriptions, and the statistics regarding revenue are
printed.
