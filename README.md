## Injektor: Minimalist injection library


### Using Injektor

We’ll demonstrate dependency injection using our library by building a coffee maker. 
For complete sample code that you can compile and run, see 
[CoffeeMakerIntegrationTest](/injektor/src/test/kotlin/com/github/lexer/injektor/CoffeeMakerIntegrationTest.kt).

Let's start from defining coffee maker classes:

```kotlin
class CoffeeMaker(val heater: Heater, val pump: Pump, val logger: Logger) {
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

class ElectricHeater(val logger: Logger) : Heater {
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

class Thermosiphon(val heater: Heater, val logger: Logger) : Pump {
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
    // Think of "resolve()" as a promise that dependency
    // will be satisfied when module will be integrated
    override fun configure() {
        bind<Pump> { Thermosiphon(heater = resolve(), logger = resolve()) }
        bind { CoffeeMaker(heater = resolve(), pump = resolve(), logger = resolve()) }
    }
}

```

By default declared dependency will be resolved as a new instance. 
If you want the same instance of a specific class simply add `asSingleton` 
to the end of your declaration.

```kotlin
class HeaterModule : Module() {
    override fun configure() {
        bind<Heater> { ElectricHeater(logger = resolve()) }.asSingleton()
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

Each `Kontainer` can have single parent container and add multiple modules.
Our coffee maker `Kontainer` will extend root `Kontainer` and 
add `CoffeeMakerModule` and `HeaterModule` modules.

```kotlin
val coffeeContainer = Kontainer.create(rootContainer, CoffeeMakerModule(), HeaterModule())
```

Finally we can resolve our `CoffeMaker` dependency and brew some coffee.

There are two ways to resolve dependencies using `Kontainer`.

First is using `inject()` delegated property. To enable that property in your class it 
should implement `Injectable` interface.

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

That's it folks! Happy ~~brewing~~injecting! 

🚀 🐑 🚢

### TODO

- Publish maven artifact
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
