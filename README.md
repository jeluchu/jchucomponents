
![Cover Library Image](https://raw.githubusercontent.com/Jeluchu/jchucomponents-compose/develop/images/cover.png)
# Jchucomponents for Android

##  Introduction

This library is intended to facilitate the work of developers so that they can make applications in a faster way and with less code. That is why in addition to having design components (Compose), it also includes various functions and extensions with which to accelerate the development of applications from scratch, or current, as well as including components for the implementation of architectures (MVVM) or perform various common functions.


##  Use Library

In the repository it is not only the library project but also the example project "sample_client";

thanks to it you will learn how to use the library.

*There are different ways of adding this library to your code*

###  Gradle / Maven dependency

At the moment we do not have a publishing mechanism to a maven repository so the easiest way to add the library to your app is via a JitPack Dependency [![](https://jitpack.io/v/jeluchu/jchucomponents.svg)](https://jitpack.io/#jeluchu/jchucomponents)

>
      allprojects {
	  repositories {
	      ...
	      maven { url 'https://jitpack.io' }
    	  }
      }

Add the **jchucomponents** dependencies, it include jchucomponents-core library and its library dependencies.

>
    // build.gradle

    dependencies {
       implementation 'com.github.jeluchu.jchucomponents:jchucomponents-core:1.x.x'
    }
    
or

>
    // build.gradle.kts

    dependencies {
       implementation("com.github.jeluchu.jchucomponents:jchucomponents-core:1.x.x")
    }

If you want to get the design components in Jetpack Compose, you will need to include jchucomponents-ui library and its library dependencies

>
    // build.gradle
    
    dependencies {
       implementation 'com.github.jeluchu.jchucomponents:jchucomponents-ui:1.x.x'
    }
    
or

>
    // build.gradle.kts

    dependencies {
       implementation("com.github.jeluchu.jchucomponents:jchucomponents-ui:1.x.x")
    }
	  
	  
If you want to get the extensions, you will need to include jchucomponents-ktx library and its library dependencies

>
    // build.gradle
    
    dependencies {
       implementation 'com.github.jeluchu.jchucomponents:jchucomponents-ktx:1.x.x'
    }
        
or

>
    // build.gradle.kts

    dependencies {
       implementation("com.github.jeluchu.jchucomponents:jchucomponents-ktx:1.x.x")
    }
	  
	
##  Versions prior to v1

Versions released prior to v1.x.x can still be used although it is not recommended due to the lack of optimizations and the lack of documentation for those versions, in case you want to use it, remember that the implementation is not the same as the current one

### Old implementation


**For Gradle:**

Add the maven repository:
>
      allprojects {
		    repositories {
			    ...
			    maven { url 'https://jitpack.io' }
    	  }
      }


Add the **jchucomponents-compose** dependencies

***Gradle:***
>
    dependencies {
          implementation 'com.github.Jeluchu:jchucomponents-compose:0.10.0'
    }

**For Kotlin DSL:**

Add the maven repository:

    allprojects {  
      repositories {  
		    ...
            maven("https://jitpack.io")  
        }  
    }

Add the **jchucomponents-compose** dependencies

>
    dependencies {
          implementation("com.github.Jeluchu:jchucomponents-compose:0.10.0")
    }



These versions used `JavaVersion.VERSION_11` please take this into account to adjust your project with the compatible versions as it may cause problems when compiling your project
```
sourceCompatibility JavaVersion.VERSION_11
targetCompatibility JavaVersion.VERSION_11
```

###  As a git submodule

Basically get this code and compile it having it integrated via a git submodule:

1. go into your own apps directory on the command line and add this lib as a submodule: ```git submodule add https://github.com/jeluchu/jchucomponents jeluchu-jchucomponents```

2. Import/Open your app in Android Studio

##  Development process

For the development of new features by the developer community, the following steps will be followed, the review of the issues will be in charge of the official contributors of the library

* Create an **issue with feature request**
* **Review** of the feature request or issue and analyze it
* Development of new features
* Documentation of new features
* Create [pull request](https://github.com/jeluchu/jchucomponents/pulls)

##  Compatibility

JchuComponents is compatible with newer versions of Android Studio, and includes support for `JavaVersion.VERSION_11` and **recent targets for the latest versions of Android**

## Contributors ✨

Here are the **main contributors to the Android library**,

<table>
  <tr>
    <td align="center"><a href="https://github.com/Jeluchu"><img src="https://avatars.githubusercontent.com/u/32357592?v=4" width="100px;" alt=""/><br /><sub><b>Jéluchu</b></sub></a><br/><a href="https://about.jeluchu.com/" title="About Jelu">🌍</a> <a href="https://twitter.com/Jeluchu" title="Twitter">📢</a><a href="https://www.linkedin.com/in/jesusmariacalderon/" title="LinkedIn">🔍</a></td></tr></table>
