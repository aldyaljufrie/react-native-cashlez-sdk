
# react-native-cashlez-sdk

## Getting started

`$ npm install react-native-cashlez-sdk --save`

### Mostly automatic installation

`$ react-native link react-native-cashlez-sdk`

### Manual installation


#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNCashlezSdkPackage;` to the imports at the top of the file
  - Add `new RNCashlezSdkPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-cashlez-sdk'
  	project(':react-native-cashlez-sdk').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-cashlez-sdk/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-cashlez-sdk')
  	```


## Usage
```javascript
import RNCashlezSdk from 'react-native-cashlez-sdk';

// TODO: What to do with the module?
RNCashlezSdk;
```
  