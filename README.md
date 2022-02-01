# Fragment factory
[![build](https://github.com/rwqwr/fragment-factory/actions/workflows/workflows.yml/badge.svg)](https://github.com/rwqwr/fragment-factory/actions/workflows/workflows.yml)

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
