## Zoo Treasure Hunt

A native Android scavenger-hunt app for zoo visitors. Users locate and 
photograph animals around the zoo, with progress tracked via real-time 
GPS and a step counter, visualised on an integrated Google Maps view. 
Step-goal badges reward exploration.

**Tech stack:** Kotlin, Jetpack Compose, MVVM (ViewModel + StateFlow), 
Hilt (DI), Room + DataStore (persistence), WorkManager (background step 
tracking), Ktor (networking), Coil (image loading), Google Maps Compose 
SDK, FusedLocationProviderClient, native step-counter sensor API, 
in-app camera capture.

**Highlights:**
- Combined three device sensors (GPS, step counter, camera) into one 
  cohesive UX
- Clean MVVM architecture with dependency injection via Hilt
- Background step tracking that survives app closure (WorkManager)

Solo project, built end-to-end.
