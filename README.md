![alt text](https://raw.githubusercontent.com/dwtalk/JHawtCode/gh-pages/apple-touch-icon-152x152.png "JHawtCode - Enable Spring God Mode")

## JHawtCode

A simple Quake like console for your Spring based site. Write and execute server side Java from your browser.

#### LICENSE

By using the JHawtCode product, you confirm that you have read and agreed to the license agreement, LICENSE, available at: https://github.com/dwtalk/JHawtCode/blob/master/LICENSE . Non-commerical usage will be bound by the Creative Commons Attribution-NonCommercial 3.0 License. Commercial usage is permitted, under terms, as defined in the license through the purchase of a commercial license.

#### DEMO

http://demo.jhawtcode.com

![alt text](https://raw.githubusercontent.com/dwtalk/JHawtCode/gh-pages/browsershot.png "JHawtCode - Demo")

#### INSTALL

##### Step 1:
Setup your dependency to include the jhawtcode jar. Example maven configuration below.

Maven Configuration
```
<dependency>
	<groupId>com.ddubyat.develop</groupId>
	<artifactId>jhawtcode</artifactId>
	<version>0.0.3</version>
</dependency>
```

##### Step 2:
Add component scanning to pick up the beans in the jar.

Spring Java Config
```
@ComponentScan(basePackages = {"your.package.here", "com.ddubyat.develop.jhawtcode"})
```

Spring XML Config
```
<context:component-scan base-package="com.ddubyat.develop.jhawtcode"/>
```

##### Step 3:
Configure JHawtCode Application Context import.

Spring Java Config
```
@ImportResource("classpath*:jhawtcode-applicationContext.xml")
```

Spring XML Config (In web.xml where your dispatcher servlet is defined add the additional file below)
```
classpath*:jhawtcode-applicationContext.xml
```

##### Step 4:
Add the includes to the css and js files required to run JHawtCode and appear on your development / QA site

```
<script src="/jhawtcode/jhc.js"></script>
<link href="/jhawtcode/jhc.css" rel="stylesheet" />
```

##### Step 5:
Add a system property to ensure that JHawtCode is only run in a development environment and not in a production environment. There are very serious ramifications of running in production, and your entire system could be compromised; This is a safeguard.

```
-Djhawtcode.enabled=ICERTIFYTHISISNOTPROD
```

#### PREREQUISITES

- Java >= 1.6
- Spring >= 3.0 OR Spring Boot >= 1.0.1
- A J2EE Container:  Tomcat or Jetty

#### USAGE

JHawtCode operates of a vim-like command stricture.

When opening the console, you are immediatly in development mode. From here your are able to begin writing code or executing commands.

Entering code is much like writing the inners of a java class. The exception here is that there is no class definition. You are able to write methods, declare globals, import packages, and wite code all from here.

To complete the code, submit it for compliation and execution, and see the results, the command ```:w``` must be entered to finalize the java chunk.

An example of some acceptable input would be:
```
import java.lang.Math;

private int squared;

jhc.println(getSquare(2));

private int getSquare(int src) {
	if(scr > 1) {
		return Math.pow(src,2);
	} else {
		return 0;
	}
}
:w
```

The above code will return 4 to the console, and clear the code from the window. The variable jhc is configured as a print stream and can be used similar to System.out to return data to the console.

```
jhc.println("");
```

Additionally, a few helper methods and global variables are build into the code that allow for quicker operations in Spring. These are:
```
WebApplicationContext webApplicationContext;
ApplicationContext applicationContext;
HttpServletRequest request;
HttpServletResponse response;
Object getBean(String beanName);
HttpSession getSession();
Cookie getCookie(String cookieName);
```

JHawtCode is not limited to these helper objects, and your own code can be used to supplement this. To enable this, simply create a local file with imports, global variables, and methods. Then you will need to specify the location to this file with a system property such as: ```-Djhawtcode.appendCodeFile=/opt/myCode.txt```. The only other additional system property that JHawtCode uses, is a variable to control the system height: ```-Djhawtcode.console.height=500```.


With JHawtCode System Properties can be updated dynamically as well. If you want to change, for example, the height of the console, you could simply issue the set property command. The command would be issued on a single line as: ```:sp jhawtcode.console.height 50```.

One additional feature of JHawtCode is the ability to load a jar at runtime into the classpath of the running JVM. We've all probably received the dreaded ClassDefNotFoundError for some random jar, and this aims to be able to fix that while the app is running. The jar must be local, and would be loaded with the command example: ```:rr file:/opt/fixmissing.jar```


#### EXAMPLES
Below are three simple example of usage. They do not indicate the complexity possible but offer some example of simple invocation.

```
//Print the Google Analytics Tracking ID from cookie
jhc.println(getCookie("_ga").getValue());
:w
```
```
//Do some simple math
jhc.println(java.lang.Math.pow(3,3));
:w
```
```
//Convert and int to hex
jhc.println(Integer.toHexString(1234));
:w
```

#### PROJECT HOME

http://jhawtcode.com

#### GIT REPOSITORY

https://github.com/dwtalk/JHawtCode

#### ISSUES

https://github.com/dwtalk/JHawtCode/issues

#### DEVELOPER DOCUMENTATION

TBD
