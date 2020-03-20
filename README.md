# Android AppUpdate

android app update library from https://github.com/fccaikai/AppUpdate
as this library is no more maintained by the owner 
updated to androidx artifect

## Installation

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```bash
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Step 2. Add the dependency

Add it in your app build.gradle at the end of repositories:

```bash
dependencies {
         implementation 'com.github.vineetchuahan15:AppUpdate:1.0.0'
 }
```
## Usage

```python
UpdateWrapper updateWrapper = new UpdateWrapper.Builder(getApplicationContext())
    					//set interval Time
    	                .setTime(time)
    	                //set notification icon
    	                .setNotificationIcon(R.mipmap.ic_launcher_round)
    	                //set update file url
    	                .setUrl("you update json file url")
  						//set customs activity
  						.setCustomsActivity(cls)
  						 //set showToast. default is true
    	                .setIsShowToast(false)
  						//add callback ,return new version info
						.setCallback(new CheckUpdateTask.Callback() {
                                     @Override
                    public void callBack(VersionModel versionModel) {
                        Log.d(TAG,"new version :" +versionModel.getVersionName());

                    }

                })
                .build();

        updateWrapper.start();
```

## The update json format：
```{
  "versionCode":1,
  "versionName":"1.0.0",
  "content":"1.add something#2.add something",//use # to wrap
  "minSupport":1,	//min support version. while your app versionCode less than  minSupport,You must update app
  "url":"apk download url"
}
```
## License
[MIT](https://choosealicense.com/licenses/mit/)
