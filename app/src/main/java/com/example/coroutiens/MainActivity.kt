package learnCoroutines

import kotlinx.coroutines.*

// DESIGN PRINCIPLE
//      DESIGN TOWARDS CONCURRENCY RATHER THAN PARALLELISM
//      i.e. Model System At Abstract Level
//      As A Set Of Concurrent Tasks i.e Coroutines
/*

> A _coroutine_ is an instance of suspendable computation.

It is conceptually similar to a thread, in the sense that it
takes a block of code to run that works concurrently with
the rest of the code.

However, a coroutine is not bound to any particular thread.
It may suspend its execution in one thread and resume in another one.

Coroutines can be thought of as light-weight threads,
but there is a number of important differences that make
their real-life usage very different from threads.

# COROUTINE BUILDER

[launch] is a _coroutine builder_.
It launches a new coroutine concurrently with
the rest of the code, which continues to work independently

[runBlocking] is also a coroutine builder
that bridges the non-coroutine world of a regular `fun main()` and
the code with coroutines inside of `runBlocking { ... }` curly braces.

[delay] is a special _suspending function_.
It _suspends_ the coroutine for a specific time.

>   Suspending a coroutine does not _block_ the underlying thread, but allows
>   other coroutines to run and use the underlying thread for their code.

The name of `runBlocking` means that the thread that runs it
(in this case the main thread) gets _blocked_ for
the duration of the call, until all the
coroutines inside `runBlocking { ... }` complete their execution.

// BEST PRACTICE
`runBlocking` used like that at the very top-level of the application
 and quite rarely inside the real code, as threads are expensive resources
 and blocking them is inefficient and is often not desired.
*/

// Created Two Concurrent Coroutines
// In this case main Is Just Function Call
//fun main() {

// main Assigned A Coroutine Returned By runBlocking Coroutine Builder
//     Hence main itself becomes Coroutine
fun main() = runBlocking {
    // launch Will Create A New Coroutine
    println("runBlocking Created Coroutine!")

    // launch Coroutine Builder Is An Extension Function On CoroutineScope
    launch {
        println("launch Created Coroutine!")
        delay( 1000L )
        println( "World!" )
    }

//    delay( 1000L )
    println("Hello!")
    // It Will Be Blocked Till Other Coroutine Completes It!
    //       Because That Is The Nature Of Coroutine
    //       Created By runBlocking Coroutine Builder
}


