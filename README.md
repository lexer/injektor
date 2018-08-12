## Injektor: "Not" a dependency injection library [![Build Status](https://travis-ci.org/lexer/injektor.svg?branch=master)](https://travis-ci.org/lexer/injektor)

*Dependency injection is hard, scary and opinionated. So this is "not" a dependency injection library :)*

*Project is under active development. API is not stabilized*

```
compile 'com.github.lexer.injektor:injektor:0.0.3'
```

For unit tests

```
compile 'com.github.lexer.injektor:injektor-test:0.0.3'
```


### Get started

To demonstrate usage of this library lets do a `classic` coffee maker.
For complete sample code that you can compile and run, see 
[CoffeeMakerIntegrationTest](/coffee-app/src/test/kotlin/com/github/lexer/injektor/coffee/CoffeeMakerIntegrationTest.kt).

Let's start from defining coffee maker classes:

```kotlin
class CoffeeMaker(override val injector: Injector) : Injectable {

    private val heater: Heater by inject()
    private val pump: Pump by inject()
    private val logger: Logger by inject()

    fun brew() {
        heater.on()
        pump.pump()
        logger.log("coffee is brewed")
        heater.off()
    }
}

interface Heater {
    fun on()
    fun off()
    fun isHot(): Boolean
}

class ElectricHeater(override val injector: Injector) : Injectable, Heater {
    private val logger: Logger by inject()

    var isOn = false
    override fun on() {
        logger.log("heater is on")
        isOn = true
    }

    override fun off() {
        logger.log("heater is off")
        isOn = false
    }

    override fun isHot(): Boolean {
        return isOn
    }
}

interface Pump {
    fun pump()
}

class Thermosiphon(override val injector: Injector) : Injectable, Pump {
    private val heater: Heater by inject()
    private val logger: Logger by inject()

    override fun pump() {
        if (heater.isHot()) {
            logger.log("pump is pumping")
        }
    }
}
```

Injektor can't resolve undeclared dependencies.
Each dependency should be explicitly declared by implementing `Module` class.

```kotlin
class CoffeeMakerModule : Module() {
    // Since Injektor framework rely on property injection
    // all you need to pass your factory methods is injector object
    override fun configure(injector: Injector) {
        bind<Pump> { Thermosiphon(injector) }
        bind { CoffeeMaker(injector) }
    }
}

```

By default declared dependency will be resolved as a new instance. 
If you want the reuse instance of a specific class you should use scopes.
Deeper dive into scopes is provide in separate section.

```kotlin
class HeaterModule : Module() {
    override fun configure(injector: Injector) {
        bind<Heater> { ElectricHeater(injector) }.scope("coffee")
    }
}
```

You should think of `Modules` as "fancy factories". 
Actual dependency resolution is done by `Injector`s.

`Injector` is responsible for dependency resolution and scoping of your instances.

At the moment only single (god) `Injector` is supported.

*In future I'm planning to add child injectors for [on demand modules](https://developer.android.com/guide/app-bundle/playcore) support
It doesn't mean that you cannot use library for multi modular app.*

```kotlin
val injector = Injector.create(modules = listof(LoggerModule(), CoffeeMakerModule(), HeaterModule()))
```

Finally we can resolve our `CoffeMaker` dependency and brew some coffee.

There are two ways to resolve dependencies using `Injector`.

First is using `inject()` delegated property. To enable that property in your class it 
should implement `Injectable` interface 

```kotlin
class CoffeeApp : Fragnum, Injectable {
    val logger: Logger by inject()
    val cofferMaker : CoffeeMaker by inject()

    override val injector: Injector
            by lazy { Injector.create(modules = listOf(LoggerModule(), HeaterModule(), CoffeeMakerModule())) }

    fun run(): Logger {

        cofferMaker.brew()

        return logger
    }

    override fun injector(): Injector {
        return injector
    }
}
```

The second way is directly call `get` method on your `Injector` instance

```Kotlin
val coffeeMaker = injector.get(CoffeeMaker::class)
// or
val coffeeMaker2 : CoffeeMaker = injector.get()
```

### Configuration validation

To validate injector correctness create unit test and run `checkInjector` helper function.

`checkInjector` will try to instantiate all declared dependencies and eager load all lazy properties.

Example:

```
class CoffeeMakerModuleTest {
    @Test
    fun checkinjector_validinjectorWithCompleteGraph_noErrors() {
        val injector = Injector.create(modules = listOf(LoggerModule(), HeaterModule(), CoffeeMakerModule()))
        assertThat(checkInjector(injector)).isEmpty()
    }
```

### Scoping

Scoping is a mechanism to create singleton instances of your classes with limited lifetime.

To make injector cache instance you class you should use `scoped` method in your dependency declaration.

```kotlin
class UserSettingModule : Module() {
  override fun configure(injector: Injector) {
    bind<UserSettings> { UserSettings() }.scope("user")
  }
}
```

In your code you should explicitly start and stop your scopes.

```kotlin
fun onUserLoggedIn() {
  injector.startScope("user")
}

fun onUserLoggedOut() {
  injector.stopScope("user")
}
```

Here are few unit tests to illustrate behavior

```kotlin
@Test 
fun sameInstance() {
  injector.startScope("user")
  val settings1 = injector.get(UserSettings::class)
  val settings2 = injector.get(UserSettings::class)
  injector.stopScope("user")

  assertThat(settings1).isNotNull()
  assertThat(settings2).isNotNull()
  assertThat(settings1).isEqualTo(settings2)
}

@Test 
fun differentInstances() {
  injector.startScope("user")
  val settings1 = injector.get(UserSettings::class)
  injector.stopScope("user")
  injector.startScope("user")
  val settings2 = injector.get(UserSettings::class)
  injector.stopScope("user")

  assertThat(settings1).isNotNull()
  assertThat(settings2).isNotNull()
  assertThat(settings1).isNotEqualTo(settings2)
}
```

If you want to make your class aware of being destroyed you can implement optional `Scoped` interface

```kotlin
class UserSettings: Scoped {
  override fun onScopeDestroyed() {

  }
}
```

At this point resolving scoped classes from "not started" scopes is allowed. 
However it's a very bad behavior and you should avoid it all costs.

Injector logs these attempts as warnings. Please see `InjectorLogger` for details.

### Unit testing

```kotlin
class UnitTestingExample {
    @Mock
    lateinit var dependency: Dependency

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun mooTest() {
        val mockContainer = MockInjector()
                .mock(dependency)
        val testedClass = CowClass(mockContainer)

        testedClass.sayMoo()

        Mockito.verify(dependency).moo()
    }

    class CowClass(override val injector: Injector) : Injectable {
        private val dependency: Dependency by inject()

        fun sayMoo(): String {
            return dependency.moo()
        }
    }

    interface Dependency {
        fun moo(): String
    }
}
```

That's it folks! 🚀 🐑 🚢

### Deploy new version

Update version and run deploy script below.

```
$ ./gradlew clean build bintrayUpload -PbintrayUser=BINTRAY_USERNAME -PbintrayKey=BINTRAY_KEY -PdryRun=false
```

### FAQ

1. Why property injection?

Typically it's always recommended to rely on constructor injection. 
However constructor injection requires unnecessary initialization of all 
dependencies during construction time. With property injection we can enforce lazy initialization 
of every individual dependency. 

*** You can avoid it with by passing construct arguments as `Lazy<>` but
that creates a lot of boilerplate code

2. How does this library compare to other DI libraries/frameworks?

This is "not" a di library.

### TODO

- More robust error messaging needed
- Make sure instantiation is thread safe.

### Authors

- Aleksei Zakharov (github:lexer)

License
-------

    Copyright 2018 Aleksei Zakharov

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
