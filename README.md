# PhilipsFlickr

**Assignment:**<br />
Write an app for Android or iOS that works as a client app for Flickr. (flickr.com)

**Functional requirements:**<br />
1) The app must use the Flickr API (https://www.flickr.com/services/api/) to allow user searching for photos with specific words<br />
2) The app must show the results of the search in an infinite scroll list where each cell contains at least a photo<br />
3) When tapping on a cell the user of the app must see the full screen photo and its details<br />

**Non-functional requirements:**<br />
1) The deliverable must include the full project and a README.txt file describing how to build it and other relevant informations<br />
2) Any third party library can be used. The README.txt must contain a brief description of the library and the reason it is used.<br />
3) Android apps must be built using Java or Kotlin. iOS must be built using ObjectiveC or Swift<br />
4) Even though the app is not an exercise of design, the app just show common UI paradigms of the platform<br />
5) Within the requirements there’s complete freedom to be creative and add details<br />

**Steps to install:**<br />
1) Download application from github<br />
2) Import project in Android Studio<br />
3) You must have installed compileSdkVersion 23 buildToolsVersion "23.0.2"<br />
4) Run application and enjoy:).

**Third party libraries:**<br />
1) com.googlecode.flickrj-android:flickrj-android:2.1.0 - used to work with Flickr API<br />
2) com.octo.android.robospice:robospice-spring-android:1.4.14 - used to work with requests to server instead of AcyncTasks<br />
3) org.codehaus.jackson:jackson-mapper-asl:1.9.13' - json converter<br />
4)org.slf4j:slf4j-simple:1.7.18' - logger used in robospice <br />
5)com.squareup.picasso:picasso:2.5.2 - download images asynchronously <br />
