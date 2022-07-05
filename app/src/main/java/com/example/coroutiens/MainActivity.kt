package com.example.coroutiens

import kotlinx.coroutines.*
import java.lang.Thread.sleep
import kotlin.system.*

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

// STRUCTURED CONCURRENCY

Coroutines follow a principle of **structured concurrency** which
means that new coroutines can be only launched in a specific
[CoroutineScope] which delimits the lifetime of the coroutine.

In a real application, you will be launching a lot of coroutines.
Structured concurrency ensures that they are not lost and do not leak.

>   An outer scope cannot complete until all its children coroutines complete.

Structured concurrency also ensures that
any errors in the code are properly reported and
are never lost.

// RESOURCES Managed By Operating System
//      Files, Threads, Processes, Sockets, Pipes, Semaphores, Mutexes etc...
    FILE fileDescriptor = fopen("FileName.txt", "r");
    // In Finally Block You Must Free Up Resources
    fclose();
    Thread thread = new Thread()

*/

//-------------------------------------------------------------------------
//-------------------------------------------------------------------------

// Created Two Concurrent Coroutines
// In this case main Is Just Function Call
//fun main() {

// main Assigned A Coroutine Returned By runBlocking Coroutine Builder
//     Hence main itself becomes Coroutine
//fun main() = runBlocking {
//    // launch Will Create A New Coroutine
//    println("runBlocking Created Coroutine!")
//
//    // launch Coroutine Builder Is An Extension Function On CoroutineScope
//    launch {
//        println("launch Created Coroutine!")
//        delay( 1000L )
//        println( "World!" )
//    }
//
////    delay( 1000L )
//    println("Hello!")
//    // It Will Be Blocked Till Other Coroutine Completes It!
//    //       Because That Is The Nature Of Coroutine
//    //       Created By runBlocking Coroutine Builder
//}

//-------------------------------------------------------------------------
//-------------------------------------------------------------------------

/*
When you perform "Extract function" refactoring on this code,
you get a new function with the `suspend` modifier.
Suspending functions can be used inside coroutines
just like regular functions, but their additional feature
is that they can, in turn, use other suspending functions
*/

//fun main() = runBlocking {
//    println("runBlocking Created Coroutine!")
//    launch {
//        doWorld()
//    }
//    println("Hello!")
//}
//
//suspend fun doWorld() {
//    println("launch Created Coroutine!")
//    delay(1000L)
//    println("World!")
//}

//-------------------------------------------------------------------------
//-------------------------------------------------------------------------

/*
// SCOPE BUILDER

In addition to the coroutine scope provided by different builders,
it is possible to declare your own scope using the [coroutineScope]
builder.

It creates a coroutine scope and does not complete until
all launched children complete.

[runBlocking] and coroutineScope builders may look similar
because they both wait for their body and all its children to complete.

The main difference is that the [runBlocking] method _blocks_ the
current thread for waiting, while [coroutineScope][_coroutineScope]
just suspends, releasing the underlying thread for other usages.

Because of that difference, [runBlocking] is a regular function and
[coroutineScope][_coroutineScope] is a suspending function.

You can use `coroutineScope` from any suspending function.
*/

//fun main() = runBlocking {
//    doWorld()
//}
//
//suspend fun doWorld() = coroutineScope {
//    launch {
//        delay(1000L )
//        println( "World!")
//    }
//    println("Hello")
//}


//-------------------------------------------------------------------------
//-------------------------------------------------------------------------

/*
A [coroutineScope] builder can be used inside any
suspending function to perform multiple concurrent operations.
*/
//
//fun main() = runBlocking {
//    doWorld()
//    println("Done...")
//}
//
//suspend fun doWorld() = coroutineScope {
//    launch {
//        delay( 2000L )
//        println( "World 2...")
//    }
//    launch {
//        delay( 3000L )
//        println( "World 1...")
//    }
//    println("World")
//}

/*
A coroutineScope in doWorld completes only after both coroutines completes
and so doWorld() returns.
*/

//-------------------------------------------------------------------------
//-------------------------------------------------------------------------

// Job Using Coroutine
/*
A [launch] coroutine builder returns a [Job] object
that is a handle to the launched coroutine and can be
used to explicitly wait for its completion.
*/

//fun main() = runBlocking {
//    val job = launch {
//        delay( 1000L )
//        println("World!")
//    }
//
//    println("Hello")
//    job.join() // Wait Until Child Coroutine Completes
//    println("Done")
//}

//fun main() {
//    val job = GlobalScope.launch {
//        delay( 1000L )
//        println("World!")
//    }
//
//    println("Hello")
////    job.join() // Wait Until Child Coroutine Completes
//    println("Done")
////    sleep( 2000L )
//}

//-------------------------------------------------------------------------
//-------------------------------------------------------------------------

//Coroutines are less resource-intensive than JVM threads. Code that exhausts the
//JVM's available memory when using threads can be expressed using coroutines
//without hitting resource limits

// Coroutines Are Cheap!
//fun main() = runBlocking {
//    repeat(1000_000 ) {
//        launch {
////            delay(1000L)
//            print(".")
//        }
//    }
//    println("Done With One Million Coroutines...")
//}

//fun main() = runBlocking {
//    repeat(1000_000 ) {
//        doSomething()
//    }
//    println("Done With One Million Coroutines...")
//}
//
//fun doSomething() {
////   delay(1000L)
//    val thread = Thread {
//        println("${Thread.currentThread()} has run.")
//        print(".")
//    }
//    thread.start()
//}

//-------------------------------------------------------------------------
//-------------------------------------------------------------------------

/*
// CANCELLING COROUTINES EXECUTION

In a long-running application you might need fine-grained control
on your background coroutines.
*/

//fun main() = runBlocking {
//    val job = launch {
//        repeat( 1000 ) { i ->
//            delay( 500L )
//            println("Job : Going To Sleep $i...")
//        }
//    }
//    delay( 1300L )
//    println("Main Coroutine Tired Of Waiting...")
//    job.cancel()
//    println("Main Waiting Coroutine To Join...")
//    job.join()
//    println("Main Coroutine Completing...")
//}

//-------------------------------------------------------------------------
//-------------------------------------------------------------------------

/*
## Cancellation is cooperative

Coroutine cancellation is _cooperative_.
A coroutine code has to cooperate to be cancellable.

> All the suspending functions in `kotlinx.coroutines` are _cancellable_.

They check for cancellation of  coroutine and throw [CancellationException]
when cancelled.

However, if a coroutine is working in
a computation and does not check for cancellation, then it cannot be cancelled
*/

//fun main() = runBlocking {
//    val startTime = System.currentTimeMillis()
//
//    val job = launch( Dispatchers.Default ) {
//        var nextPrintTime = startTime
//        var i = 0
//        while ( i < 5 ) {
//            if ( System.currentTimeMillis() >= nextPrintTime ) {
//                println("Job: Going For Sleep ${i++} ...")
//                nextPrintTime += 500L
//            }
//        }
//    }
//    println("Main: Tired Of Waiting...")
//    job.cancelAndJoin()
//    println("Main Coroutine Completing...")
//    delay( 500L )
//}

//-------------------------------------------------------------------------
//-------------------------------------------------------------------------

// Making Coroutine Cancellable
// isActive Is An Extension Property Of coroutineScope

// DESIGN PRINCIPLE
//      Explicitly Check Coroutine Is Marked For Cancellation
//          Don't Do Any Heavy Calculations/Processing When Coroutine Marked For Cancellation

//fun main() = runBlocking {
//    val startTime = System.currentTimeMillis()
//
//    val job = launch( Dispatchers.Default ) {
//        var nextPrintTime = startTime
//        var i = 0
//        while( isActive ) {
//            // Coroutine Have To Cooperate To Be Cancellable
//            // Coroutine Checking Marked For Cancellation.
//            //      Coroutine isActive Flag Set To False On Receive Of Cancellation Request
//            if ( System.currentTimeMillis() >= nextPrintTime ) {
//                println("Job: Going For Sleep ${i++} ...")
//                nextPrintTime += 500L
//            }
//        }
//    }
//    println("Main: Tired Of Waiting...")
//    job.cancelAndJoin()
//    println("Main Coroutine Completing...")
//    delay( 500L )
//}

//-------------------------------------------------------------------------
//-------------------------------------------------------------------------

// DESIGN PRINCIPLE

// PRIMARY DESIGN PRINCIPLE IN MODERN DESIGN
// Special cases aren't special enough to break the Design.
//      Corollary: Exceptions Are Not That Exceptional Such That
//          It Should Break Your Design

// SECONDARY DESIGN PRINCIPLE IN MODERN DESIGN
// Errors/Exceptions should never pass silently.
//      Unless explicitly silenced.

/* FINALLY, BLOCK BEST PRACTICES IN COROUTINE
Any attempt to use a suspending function in the finally block
causes [CancellationException], because the coroutine running this code is cancelled.

Usually, this is not a problem, since all well-behaving closing operations
(closing a file, cancelling a job, or closing any communication channel)
are usually non-blocking and do not involve any suspending functions.

However, in the rare case when you need to suspend in a cancelled coroutine
you can wrap the corresponding code in withContext(NonCancellable) {...}
using [withContext] function and [NonCancellable] context
*/

//fun main() = runBlocking {
//    val startTime = System.currentTimeMillis()
//    val job = launch( Dispatchers.Default ) {
//        var nextPrintTime = startTime
//        var i = 0
//        repeat(5 ) { i ->
//            try {
//                println("Job: Going For Sleep $i ...")
//                delay(500L)
////            } catch( e: Exception ) { // BAD PATTERN
////                // Catching TOPMOST "Exception" Is BAD PATTERN
////                //      It Will Catch All Exceptions
////                //      Always Catch Narrow Exception Rather Than Broad Exception Class
////                //          i.e. Start From Leaf Node In Exception Hierarchy
////                println( e )
////            }
//            } catch (e: CancellationException ) { // GOOD PATTERN
//                // Your Custom Handler...
//                // Catching TOPMOST "Exception" Is GOOD PATTERN
//                //      It Will Catch All Exceptions
//                //      Always Catch Narrow Exception Rather Than Broad Exception Class
//                //          i.e. Start From Leaf Node In Exception Hierarchy
//                println(e)
//            } finally {
//                // Deallocate All The Resources
//                // Make Sure It's Atomic Operation
//                withContext( NonCancellable ) {
//                    println("Finally Block:  Execution Started...")
//                    delay( 1000L )
//                    println("Finally Block Is Execution Completing...")
//                }
//            }
//        }
//        println( "To Remove Warnings: $nextPrintTime $i ")
//    }
//    delay( 1300L )
//    println("Main: Tired Of Waiting...")
////    job.cancelAndJoin()
//    println("Main Coroutine Completing...")
//    delay( 500L )
//}

//kotlinx.coroutines.JobCancellationException: StandaloneCoroutine was cancelled

//-------------------------------------------------------------------------
//-------------------------------------------------------------------------

// DESIGN PRINCIPLE
//      Design Towards Determinism Rather Than Non-Determinism
//      Corollary: Design Towards Hard Real-Time Rather Than Towards Soft-Real-Time

// Real-Time Systems???
// Hard Real-time Systems Vs. Soft Real-time Systems
//      1. Order Of Execution
//      2. Time Of Completion

/*
// Coroutine With Timeout
The most obvious practical reason to cancel execution of a coroutine is
because its execution time has exceeded some timeout.

/// DESIGN 1
While you can manually track the reference to the corresponding [Job]
and launch a separate coroutine to cancel the tracked one after delay,

/// DESIGN 2
there is a ready to use [withTimeout] function that does it.
*/

//fun main() = runBlocking {
//    // withTimeout Is Coroutine Builder
//    //      It Will Launch A Coroutine with Time As Hard Upper Bound
//
////  withTimeoutOrNull( 1300L ) {
//    val result = withTimeoutOrNull( 1300L ) {
//        repeat(1000) { i ->
//            println("Going For Sleep : $i")
//            delay( 500L )
//        }
//    }
//    println("Result : $result")
//}

/*
The TimeoutCancellationException that is thrown by [withTimeout]
is a subclass of [CancellationException].
We have not seen its stack trace printed on the console before.
That is because inside a cancelled coroutine CancellationException
is considered to be a normal reason for coroutine completion.
However, in this example we have used withTimeout right inside the main function.

Since cancellation is just an exception, all resources are closed in the usual way.
You can wrap the code with timeout in a
try {...} catch (e: TimeoutCancellationException) {...} block
if you need to do some additional action specifically on any kind of timeout
or use the [withTimeoutOrNull] function that is similar
to [withTimeout] but returns null on timeout instead of throwing an exception:
*/

//-------------------------------------------------------------------------
//-------------------------------------------------------------------------

/*
// Asynchronous timeout and resources
The timeout event in [withTimeout] is asynchronous with respect to the code running
in its block and may happen at any time, even right before
the return from inside of the timeout block.

>>>> Keep this in mind if you open or acquire
>>>> some resource inside the block that needs closing or release outside of the block

*/

//-------------------------------------------------------------------------
//-------------------------------------------------------------------------
//
//var acquired = 0
//
//class Resource {
//    init { acquired++ }
//    fun close() { acquired-- }
//}
//
//fun main() {
//    runBlocking {
//        repeat( 100_000 ) {
//            launch {
//                val result = withTimeout( 60 ) {
//                    delay( 30 )
//                    Resource()
//                }
//                result.close()
//            }
//        }
//    }
//    println( acquired )
//}

//-------------------------------------------------------------------------
//-------------------------------------------------------------------------

/*
Note that incrementing and decrementing acquired counter here from 100K coroutines
is completely Thread safe, since it always happens from the same main thread.
More on that will be explained in the chapter on coroutine context.
*/

//var acquired = 0
//
//class Resource {
//    init { acquired++ }
//    fun close() { acquired-- }
//}
//
//fun main() {
//    runBlocking {
//        repeat( 100_000 ) {
//            launch {
//                // Better To Store Resource In Your Resource Reference
//                //      Rather Than Returning From withTimeout Block
//                //      And Close Explicitly In finally Block
//                var resource: Resource? = null
//                try {
//                    withTimeout(60) {
//                        delay(30)
//                        resource = Resource()
//                    }
//                } finally {
//                    resource?.close()
//                }
//            }
//        }
//    }
//    println( acquired )
//}


//-------------------------------------------------------------------------
//-------------------------------------------------------------------------

/*
Conceptually, [async] is just like [launch].
It starts a separate coroutine which is a light-weight thread
that works concurrently with all the other coroutines.

The difference is that launch returns a [Job] and does not carry any resulting value,
while async returns a [Deferred] — a light-weight non-blocking future
that represents a promise to provide a result later.

You can use .await() on a deferred value to get its eventual result,
but Deferred is also a Job, so you can cancel it if needed.
*/

//fun main() = runBlocking<Unit>{
//    val time = measureTimeMillis {
//        // async Launches Separate Coroutine And Returns Differed Object
//        val one = async { doSomethingUsefulOne() }
//        val two = async { doSomethingUsefulTwo() }
//        println("The Answer Is: ${ one.await() + two.await() }")
//    }
//    println("runBlock Completed In Time: $time ms")
//}
//
////The Answer Is: 42
////runBlock Completed In Time: 1023 ms
//suspend fun doSomethingUsefulOne() : Int {
//    println("Called: doSomethingUsefulOne")
//    delay( 500L )
//    return 13
//}
//
//suspend fun doSomethingUsefulTwo() : Int {
//    println("Called: doSomethingUsefulTwo")
//    delay( 1000L )
//    return 29
//}

//-------------------------------------------------------------------------
//-------------------------------------------------------------------------

/*
Optionally, [async] can be made lazy by setting its start parameter to
[CoroutineStart.LAZY]. In this mode it only starts the coroutine when
its result is required by await, or if its Job's start function is invoked.
*/

//fun main() = runBlocking<Unit>{
//    val time = measureTimeMillis {
//        // async Launches Separate Coroutine And Returns Differed Object
////        val one = async( start = CoroutineStart.LAZY ) { doSomethingUsefulOne() }
////        val two = async { doSomethingUsefulTwo() }
//        val one = async( start = CoroutineStart.LAZY ) { doSomethingUsefulOne() }
//        val two = async( start = CoroutineStart.LAZY ) { doSomethingUsefulTwo() }
//
//        one.start() // Starting On Demand
//        two.start() // Starting On Demand
//
//        println("The Answer Is: ${ one.await() + two.await() }")
//    }
//    println("runBlock Completed In Time: $time ms")
//}
//
////The Answer Is: 42
////runBlock Completed In Time: 1023 ms
//suspend fun doSomethingUsefulOne() : Int {
//    println("Called: doSomethingUsefulOne")
//    delay( 500L )
//    return 13
//}
//
//suspend fun doSomethingUsefulTwo() : Int {
//    println("Called: doSomethingUsefulTwo")
//    delay( 1000L )
//    return 29
//}

/*
Note that if we just call await in println without first calling start on
individual coroutines, this will lead to sequential behavior,
since await starts the coroutine execution and waits for its finish,
which is not the intended use-case for laziness.

The use-case for async(start = CoroutineStart.LAZY) is a replacement for
the standard lazy function in cases when computation of the value
involves suspending functions.
*/

//-------------------------------------------------------------------------
//-------------------------------------------------------------------------

/*
Consider what happens if between the val one = somethingUsefulOneAsync() line
and one.await() expression there is some logic error in the code, and
the program throws an exception, and the operation that was being performed by
the program aborts. Normally, a global error-handler could catch this exception,
log and report the error for developers, but the program could otherwise
continue doing other operations. However, here we have somethingUsefulOneAsync
still running in the background, even though the operation that initiated it
was aborted
*/

//fun main() {
//    val time = measureTimeMillis {
//        // Initiated async Actions Outside of Coroutine
//        val one = somethingUsefulOneAsync()
//        val two = somethingUsefulTwoAsync()
//
//        // waiting for a result must involve either suspending or blocking.
//        // here we use `runBlocking { ... }` to block the main thread
//        // while waiting for the result
//        runBlocking {
//            println("The Answer Is: ${ one.await() + two.await() }")
//        }
//    }
//    println("runBlock Completed In Time: $time ms")
//}
//

// // BAD PATTERN
//@OptIn(DelicateCoroutinesApi::class)
//fun somethingUsefulOneAsync() = GlobalScope.async {
//    doSomethingUsefulOne()
//}
//
//@OptIn(DelicateCoroutinesApi::class)
//fun somethingUsefulTwoAsync() = GlobalScope.async {
//    doSomethingUsefulTwo()
//}
//
////The Answer Is: 42
////runBlock Completed In Time: 1023 ms
//suspend fun doSomethingUsefulOne() : Int {
//    println("Called: doSomethingUsefulOne")
//    delay( 1000L )
//    return 13
//}
//
//suspend fun doSomethingUsefulTwo() : Int {
//    println("Called: doSomethingUsefulTwo")
//    delay( 1000L )
//    return 29
//}


//-------------------------------------------------------------------------
//-------------------------------------------------------------------------

//fun main() = runBlocking {
//    val time = measureTimeMillis {
//            println("The Answer Is: ${ concurrentSum() }")
//    }
//    println("runBlock Completed In Time: $time ms")
//}
//
//// GOOD PATTERN
//suspend fun concurrentSum() : Int = coroutineScope {
//    val one = async { doSomethingUsefulOne() }
//    val two = async { doSomethingUsefulTwo() }
//    one.await() + two.await()
//}
//
//suspend fun doSomethingUsefulOne() : Int {
//    println("Called: doSomethingUsefulOne")
//    delay( 1000L )
//    return 13
//}
//
//suspend fun doSomethingUsefulTwo() : Int {
//    println("Called: doSomethingUsefulTwo")
//    delay( 1000L )
//    return 29
//}

//-------------------------------------------------------------------------
//-------------------------------------------------------------------------

//fun main() = runBlocking {
//    val time = measureTimeMillis {
//        try { failedConcurrentSum() }
//        catch( e: ArithmeticException ) {
//            println("Caught Exception ArithmeticException ")
//        }
//    }
//    println("runBlock Completed In Time: $time ms")
//}
//
//// GOOD PATTERN
//suspend fun failedConcurrentSum() : Int = coroutineScope {
//    val one = async<Int> {
//        println("UsefulOne Called...")
//        try {
//            delay( Long.MAX_VALUE )
//            println("UsefulOne After Delay ...")
//            42
//        } finally {
//            println("UsefulOne Cancelling It...")
//        }
//    }
//
//    val two = async<Int> {
//        println("UsefulTwo Called : Throwing Exception...")
////        doSomethingUsefulTwo()
//        // Simulation Of Exception Happened In This Code
//        throw ArithmeticException()
//    }
//    one.await() + two.await()
//}

/*
Note how both the first async and the awaiting parent are cancelled
on failure of one of the children (namely, two):
*/

//-------------------------------------------------------------------------
//-------------------------------------------------------------------------

//COROUTINE DISPATCHER AND THREADS

/*
Coroutines always execute in some context represented by a value of the
CoroutineContext type, defined in the Kotlin standard library.

The coroutine context includes a coroutine dispatcher (see [CoroutineDispatcher])
that determines what thread or threads the corresponding coroutine uses
for its execution.

The coroutine dispatcher can confine coroutine execution to a specific thread,
dispatch it to a thread pool, or let it run unconfined.

All coroutine builders like [launch] and [async] accept an optional CoroutineContext
parameter that can be used to explicitly specify the dispatcher for the
new coroutine and other context elements.
*/

fun main() = runBlocking<Unit> {
    launch {
        println("Launch runBlocking ${Thread.currentThread().name} ")
    }

    launch( Dispatchers.Unconfined ) {
        println("Unconfined ${Thread.currentThread().name} ")
    }

    launch( Dispatchers.Default ) {
        println("Default ${Thread.currentThread().name} ")
    }

    launch( newSingleThreadContext("MyOwnThread") ) {
        println("newSingleThreadContext ${Thread.currentThread().name} ")
    }

    launch( Dispatchers.IO ) {
        println("IO ${Thread.currentThread().name} ")
    }

    println("Main runBlocking ${Thread.currentThread().name} ")
}

