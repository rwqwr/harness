# Fragment factory
The repository contains the ability to generate Dagger Fragments module and generate navigation stubs for a multi-module project.

## Annotations
The annotation `@ProvideToFactory` allows you to register a fragment with a fragment module.

```kotlin
@ProvideToFactory
internal class PrimaryFragment @Inject constructor(
    @Named("primary") private val text:String
) : Fragment(R.layout.fragment_primary)
```

Then you need create a Dagger `FragmentModule` module that will include the generated `FragmentProviderModule`:
```kotlin
@FragmentsModule
@Module(includes = [FragmentProviderModule::class])
internal class FragmentModule
```

## Navigation stub
Navigation components currently do not allow working with multi-module projects, but there is one way to do it.
https://itnext.io/android-multimodule-navigation-with-the-navigation-component-99f265de24
The repository contains the `navigation-core` plugin that creates tasks to create navigation graph stubs in a module.
You need to include this plugin in your core navigation module
```kotlin
plugins {
    id("navigation-core")
}
```