# MVVMWeather

## :scroll: Description
Mini weather application that shows the weather of the current location. It also shows the hourly forecast of that location as well.
The goal of this project is to show how we can follow Model-view-viewmodel (MVVM) architecture.

<img src="/results/android_architect_image.png" width="360">

## :camera_flash: Screenshots
<img src="/results/current_weather_screen.png" width="260">&emsp;<img src="/results/hourly_forecast_screen.png" width="260">

## :scroll: (IMPORTANT) Add weather API Key

- Go to https://www.weatherapi.com/
- Sign up with free plan
- Create apikey.properties file, and add following lines

```
WEATHER_API_KEY="<Replace this with your own weather api key>"
WEATHER_BASE_URL="https://api.weatherapi.com/v1/"
```

## :bulb: Motivation
In this sample project, it focus on:
- Model–view–viewmodel (MVVM) architecture
- MVVM with network call only
- MVVM with network call and database (Room)
- Navigation with Android architecture component
- WorkManager
- DataStore
- Use kotlin Flow in MVVM

## Dependencies
- Navigation
- Coroutine
- Retrofit
- Moshi
- ViewModel
- LiveData
- Glide
- Room
- DataStore
- Flow

## Branches

| Branches      | What do we have?                                                                      |
| ------------- |:-------------------------------------------------------------------------------------:|
| `master`      | MVVM with network call and database                                                   |
| `remoteonly`  | MVVM with network call only                                                           |
| `datasource`  | MVVM with network call and database (Room)                                            |
| `workmanager` | Add a work manager to get the current weather with the saved location from DataStore  |
| `flow`        | Show how to use kotlin flow in MVVM                                                   |
| `di`          | Android Hilt and Kotlin full Flow integrated                                          |

## Recommended app architecture

Note: The recommendations and template only allow projects to scale, improve quality and robustness, and make them easier to test.
However, this is only just guidelines, and we should adapt them to our requirements as needed.

[Drive UI from data models](https://developer.android.com/topic/architecture)
The important principle is that you should drive your UI from data models, preferably persistent models (room).
Data models represent the data of an app. They're independent from the UI elements and other components in your app.
This means that they are not tied to the UI and app component lifecycle, but will still be destroyed when the OS decides to remove the app's process from memory.
Persistent models are ideal for the following reasons:
- Your users don't lose data if the Android OS destroys your app to free up resources.
- Your app continues to work in cases when a network connection is flaky or not available.
- If you base your app architecture on data model classes, you make your app more testable and robust.

<img src="/results/architecture.png" width="260">

There are three layers:
- UI Layer
- Domain Layer (optional)
- Data Layer

<img src="/results/ui_layer.png" width="260">

<img src="/results/data_layer.png" width="260">

## Model–view–viewmodel (MVVM) with network call only (TBD)

## Model–view–viewmodel (MVVM) with network call with database (TBD)

## Navigation with Android architecture component (TBD)

## WorkManager and DataStore (TBD)

## Kotlin Flow in Room and Repository (Still use LiveData in ViewModel and UI) (TBD)
- Make use of NetworkResult sealed class for LOADING, SUCCESS, ERROR states

## Kotlin full Flow integrated (TBD)

## Android Hilt integrated (TBD)
- Inject viewModel
- Inject repository
- Inject database
- Inject network (Retrofit)
- Inject RecyclerView adapter
- Hilt and WorkManager

```
Copyright 2022 The Android Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
