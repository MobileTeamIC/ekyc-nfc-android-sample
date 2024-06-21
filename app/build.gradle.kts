plugins {
   alias(libs.plugins.android.application)
   alias(libs.plugins.jetbrains.kotlin.android)
   id("kotlin-parcelize")
}

android {
   namespace = "com.vnptit.ic.sanmple"
   compileSdk = 34

   defaultConfig {
      applicationId = "com.vnptit.ic.sanmple"
      minSdk = 24
      targetSdk = 34
      versionCode = 1
      versionName = "1.0"

      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
   }

   buildTypes {
      release {
         isMinifyEnabled = false
         proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
         )
      }
   }

   buildFeatures {
      viewBinding = true
   }

   androidResources {
      noCompress += "bic"
   }

   compileOptions {
      sourceCompatibility = JavaVersion.VERSION_1_8
      targetCompatibility = JavaVersion.VERSION_1_8
   }

   kotlinOptions {
      jvmTarget = "1.8"
   }
}

dependencies {
   implementation(libs.androidx.core.ktx)
   implementation(libs.androidx.appcompat)
   implementation(libs.material)

   implementation(project(":ekyc"))
   implementation(project(":nfc"))

   implementation("androidx.multidex:multidex:2.0.0")
   implementation("com.airbnb.android:lottie:6.3.0")
   implementation("com.google.code.gson:gson:2.8.2")
   implementation("com.squareup.okhttp3:okhttp:4.9.0")

   // ekyc
   implementation("androidx.exifinterface:exifinterface:1.0.0")
   implementation("com.intuit.sdp:sdp-android:1.0.6")
   implementation("de.hdodenhof:circleimageview:3.1.0")
   implementation("me.dm7.barcodescanner:zxing:1.9.13") {
      exclude(group = "com.android.support")
   }

   // nfc
   implementation("org.jmrtd:jmrtd:0.7.24")
   implementation("com.madgag.spongycastle:prov:1.58.0.0")
   implementation("net.sf.scuba:scuba-sc-android:0.0.23")
   implementation("org.ejbca.cvc:cert-cvc:1.4.6")
   implementation("org.bouncycastle:bcpkix-jdk15on:1.67")
   implementation("commons-io:commons-io:2.6")
   implementation("com.gemalto.jp2:jp2-android:1.0.3")
   implementation("com.github.mhshams:jnbis:2.0.2")

   // QRCode
   implementation("com.google.zxing:core:3.5.1")
   implementation("androidx.camera:camera-core:1.2.1")
   implementation("androidx.camera:camera-camera2:1.2.1")
   implementation("androidx.camera:camera-lifecycle:1.2.1")
   implementation("androidx.camera:camera-view:1.2.1")

   testImplementation(libs.junit)
   androidTestImplementation(libs.androidx.junit)
   androidTestImplementation(libs.androidx.espresso.core)
}