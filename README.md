# Harness
[![build](https://github.com/rwqwr/fragment-factory/actions/workflows/workflows.yml/badge.svg)](https://github.com/rwqwr/fragment-factory/actions/workflows/workflows.yml)

Dagger multi-binding utility.

## Problem

Reduce the amount of code for the [dagger multi-binding][dagger-multibindings] function.
Multi-binding dagger requires you to define each instance and implement it in a Dagger module, as in the example

```kotlin 
interface Foo

class A @Inject constructor() : Foo

class B @Inject constructor() : Foo

@Module
interface DIModule {
    @Binds
    @IntoSet
    fun provideA(instance: A): Foo
    
    @Binds
    @IntoSet
    fun provideB(instance: B): Foo
}
```

> For multi-binding with instance map, we need to declare @IntoMap annotation instead of @IntoSet. 
> For more information, see [Dagger documentations][dagger-multibindings].

Every time we need a new class, an extended parent class, we have to declare it in the module as well.
The idea of the library is to designate the class with the annotation, and that's it.

The resulting classes will look like this

```kotlin
interface Foo

@FooInstance
class A @Inject constructor() : Foo

@FooInstance
class B @Inject constructor() : Foo
```

## Installation 

Make sure your project has the [KSP plugin][ksp] applied, then add the dependencies to your project.

```kotlin
dependencies {
    implementation("io.github.rwqwr:harness_api:[version]")
    ksp("io.github.rwqwr:harness_compiler:[version]")
}
```

## Annotations

### @FactoryModule

Marker for the Dagger module that will provide bindings.

```kotlin
@FactoryModule
@Module(includ=[FragmentFactoryProvider::class])
interface FragmentFactory
```

Then, based on the marked annotated class, the compiler will generate a class named `[MarkedClassName]Provider`, which consists of all the components of `@ProvideToFactory`.
The generated class must be included in the module as shown above.

### @ProvideToFactory

The basic annotation of the library is `@ProvideToFactory`.
This annotation allows you to define all the logic of a multi-binding component.
You can add an annotation to the instance you want to add to the multi-binding and define all the required annotation fields.

```kotlin 
@ProvideToFactory(
    factoryClass = FragmentModule::class,
    supertype = Fragment::class,
    mapKey = FragmentMapKey::class,
)
class PrimaryFragment @Inject constructor() : Fragment(R.layout.fragment_primary)
```

`factoryClass` -  is the class annotated with `@FactoryModule` which should include this instance.

`supertype` - supertype of injected instances.

`mapKey` - definition of the key if you want to provide dependencies in map (if you want to provide in set, you need to set `mapKey = SetKey::class`). 
The `mapKey` class must contain an argument with the dependency class, e.g:

```kotlin
@Target(AnnotationTarget.FUNCTION)
@Retention
@MapKey
annotation class FragmentMapKey(val fragmentClass: KClass<out Fragment>)
```

## @FactoryKey

The `@ProvideToFactory` annotation requires several fields and it is not really convenient to define them for each instance.
To that end, the `@FactoryKey` annotation will be added to allow you to define all the parameters for `@ProvideToFactory` in one place and use only the new annotation afterwards.

The snippet below creates a new `@FragmentDefinition` annotation that defines all the rules for the `@ProvideToFactory` annotation.

```kotlin 
@Target(AnnotationTarget.CLASS)
@Retention
@FactoryKey
@ProvideToFactory(
    factoryClass = FragmentsModule::class,
    supertype = Fragment::class,
    mapKey = FragmentMapKey::class
)
annotation class FragmentDefinition
```

Now we can mark all the fragments we want with the `@FragmentDefinition` annotation and they will appear on the dagger graph.

## License

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[dagger-multibindings]: https://dagger.dev/dev-guide/multibindings.html
[ksp]: https://github.com/google/ksp