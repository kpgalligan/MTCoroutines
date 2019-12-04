## Kotlin Multithreaded Coroutines Sample

Some basic sample code, mostly used to produce slides
for my Kotlinconf talk. This will very quickly be 
outdated, but if you want to check it out today, take a 
look.

## Build MT Coroutines

You'll need to build/deploy coroutines locally first. Run the following in the terminal.

```
git clone -b native-mt https://github.com/Kotlin/kotlinx.coroutines.git
cd kotlinx.coroutines/
./gradlew build publishToMavenLocal
```

If any of that fails, you'll need to resolve that first.

After that, build locally, and look at the `Sample.kt` file in common.