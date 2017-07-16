# ModbusTCP
jamodTCP库  
app测试demo为Android端Modbus从机模式，里面有Modbus4种寄存器的创建方法。  

## 使用方法  
### 1、添加依赖  
在工程的build.gradle添加  

    	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		 }
	  }
在app的build.gradle添加  

    dependencies {
	        compile 'com.github.hwx95:ModbusTCP:v1.1'
	}

### 2、添加网络权限

    <uses-permission android:name="android.permission.INTERNET" />  
    
### 3、创建方法具体见MainActivity

**注：Android端只能使用1024以上的端口**
