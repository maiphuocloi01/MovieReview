<h1 align="center">VieWie: The Movie Review App</h1>

<p align="center">  
VieWie is an application that helps to look for information about movies and users can write reviews of their favorite movies.<br>
</p>
</br>

<p align="center">
<img src="/previews/screenshot.png"/>
</p>

## Download
Go to the [Releases](https://github.com/maiphuocloi01/MovieReview/releases) to download the latest APK.

<img src="/previews/preview.gif" align="right" width="32%"/>

## Tech stack & Open-source libraries
- Minimum SDK level 24
- Java + [RxJava3](https://github.com/ReactiveX/RxJava) for asynchronous.
- JetPack
  - Lifecycle - dispose of observing data when lifecycle state changes.
  - ViewModel - UI related data holder, lifecycle aware.
  - Paging3 - helps you load and display pages of data from a larger dataset from local storage or over network.
- Architecture
  - MVVM Architecture (View - ViewModel - Model)
  - Repository pattern
  - [Hilt - Dagger](https://dagger.dev/hilt/) - dependency injection.
- [Retrofit2 & Gson](https://github.com/square/retrofit) - construct the REST APIs.
- [OkHttp3](https://github.com/square/okhttp) - implementing interceptor, logging and mocking web server.
- [Glide](https://github.com/bumptech/glide) - loading images.
- [Lottie](https://github.com/airbnb/lottie-android) - Render After Effects animations natively.
- [YouTube Player](https://github.com/PierfrancescoSoffritti/android-youtube-player) - a simple View that can be easily integrated in every Activity/Fragment.
- [ZoomLayout](https://github.com/natario1/ZoomLayout) - 2D zoom and pan behavior for View hierarchies, images, video streams, and much more.
- [Material-Components](https://github.com/material-components/material-components-android) - Material design components like animation, cardView.

## Features

Features include:
* Login, register, forgot password
* Watch movie infomation, image, trailer,...
* Write your review about your favorite movie
* Watch other reviews of the movie
* Save your favorite movie to your list
* ...

## Backend API
- VieWie using the [API](https://www.themoviedb.org/) for constructing RESTful API and .
- And using this API [Source](https://github.com/maiphuocloi01/api-movie-review-new) - for manage account and review movie.

## License
```xml
Designed and developed by maiphuocloi01

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
