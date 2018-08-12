## Injektor: Minimalist Kotlin dependency injection library

```
compile 'com.github.lexer.injektor:injektor:0.0.1'
```


### Using Injektor

We’ll demonstrate dependency injection using our library by building a coffee maker. 
For complete sample code that you can compile and run, see 
[CoffeeMakerIntegrationTest](/injektor/src/test/kotlin/com/github/lexer/injektor/CoffeeMakerIntegrationTest.kt).

Let's start from defining coffee maker classes:

```kotlin
class CoffeeMaker(kontainer: Kontainer) : Kontainerized(kontainer) {

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

class ElectricHeater(kontainer: Kontainer) : Kontainerized(kontainer), Heater {
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

class Thermosiphon(kontainer: Kontainer) : Kontainerized(kontainer), Pump {
    private val heater: Heater by inject()
    private val logger: Logger by inject()

    override fun pump() {
        if (heater.isHot()) {
            logger.log("pump is pumping")
        }
    }
}
```

By default Injektor doesn't know how to resolve any dependency. 
Each dependency should be explicitly declared by implementing `Module` class.

```kotlin
class CoffeeMakerModule : Module() {
    // Since Injektor framework rely on property injection
    // all you need to pass your factory methods is kontainer object
    override fun configure(kontainer: Kontainer) {
        bind<Pump> { Thermosiphon(kontainer) }
        bind { CoffeeMaker(kontainer) }
    }
}

```

By default declared dependency will be resolved as a new instance. 
If you want the same instance of a specific class simply add `asSingleton` 
to the end of your declaration.

```kotlin
class HeaterModule : Module() {
    override fun configure(kontainer: Kontainer) {
        bind<Heater> { ElectricHeater(kontainer) }.asSingleton()
    }
}
```

You should think of `Modules` as "fancy factories". 
Actual dependency resolution is done by `Kontainer` classes.

`Kontainer` is responsible for dependency resolution and scoping of your singleton instances.

Let's create our root `Kontainer`. In our coffee maker example it will be responsible for 
resolving `Logger` singleton class by adding `LoggerModule`.

```kotlin
val rootContainer = Kontainer.create(modules = LoggerModule())
```

Each `Kontainer` can have single dependencies container and add multiple modules.
Our coffee maker `Kontainer` will extend root `Kontainer` and 
add `CoffeeMakerModule` and `HeaterModule` modules.

```kotlin
val coffeeContainer = Kontainer.create(rootContainer, CoffeeMakerModule(), HeaterModule())
```

Finally we can resolve our `CoffeMaker` dependency and brew some coffee.

There are two ways to resolve dependencies using `Kontainer`.

First is using `inject()` delegated property. To enable that property in your class it 
should implement `Injectable` interface or be inherited from `Kontainerized` super class.

Typically you should use `Injectable` interface for classes that should be inherited from
some other class like `Activity`. For your own classes you should prefer to use `Kontainerized` 
super class.

```kotlin
class CoffeeApp : Injectable {
    val logger: Logger by inject()
    val cofferMaker : CoffeeMaker by inject()

    lateinit var coffeeContainer: Kontainer

    fun run(): Logger {
        val rootContainer = Kontainer.create(modules = LoggerModule())
        coffeeContainer = Kontainer.create(rootContainer, CoffeeMakerModule(), HeaterModule())

        cofferMaker.brew()

        return logger
    }

    override fun kontainer(): Kontainer {
        return coffeeContainer
    }
}
```

The second way is directly call `get` method on your `Kontainer` instance

```Kotlin
val coffeeMaker = coffeeContainer.get(CoffeeMaker::class)
// or
val coffeeMaker2 : CoffeeMaker = coffeeContainer.get()
```

### Module validation

To validate module correctness create unit test and run `validateModule` helper function.

`validateModule` will recursively try to create dynamic graph based on your module dependencies list and resolve all declared classes.

Example:

```
class CoffeeMakerModuleTest {
    @Test
    fun isValidModule_coffeeMakerModule_noErrors() {
        assertThat(validateModule(CoffeeMakerModule())).isEmpty()
    }

    @Test
    fun isValidModule_invalidHeaterModule_singleUnresolvedDepedency() {
        val errors = validateModule(HeaterModuleWithoutCorrectDependency())
        assertThat(errors[0].unresolvedClass).isEqualTo(Heater::class)
    }
```

That's it folks! Happy ~~brewing~~injecting! 

🚀 🐑 🚢

### FAQ

1. Why property injection?

Typically it's always recommended to rely on constructor injection. 
However constructor injection requires unnecessary initialization of all 
dependencies during construction time. With property injection we can enforce lazy initialization 
of every individual dependency. 

*** You can avoid it with by passing construct arguments as `Lazy<>` but
that creates a lot of boilerplate code

2. Is it possible to write unit tests for classes with `injected` properties?

Yes. It is possible.

```kotlin
class UnitTestingExample {
    @Mock
    lateinit var dependency : Dependency

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun mooTest() {
        val mockContainer = MockContainer()
                .mock(dependency)
        val testedClass = CowClass(mockContainer)

        testedClass.sayMoo()

        Mockito.verify(dependency).moo()
    }

    class CowClass(kontainer: Kontainer) : Kontainerized(kontainer) {
        private val dependency : Dependency by inject()

        fun sayMoo() : String {
            return dependency.moo()
        }
    }

    interface Dependency {
        fun moo() : String
    }
}
```

### TODO

- More robust error messaging needed

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
