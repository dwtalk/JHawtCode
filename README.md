![alt text](https://raw.githubusercontent.com/dwtalk/JHawtCode/gh-pages/apple-touch-icon-152x152.png "JHawtCode - Enable Spring God Mode")

## JHawtCode
=====

A simple Quake like console for your Spring based site. Write and execute server side Java from your browser.

#### LICENSE

By using the JHawtCode product, you confirm that you have read and agreed to the license agreement, LICENSE, available at: https://github.com/dwtalk/JHawtCode/blob/master/LICENSE
Non-commerical usage will be bound by the Creative Commons Attribution-NonCommercial 3.0 License.
Commercial usage is permitted, under terms, as defined in the license through the purchase of a commercial license.

#### DEMO

http://demo.jhawtcode.com

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

- Spring >= 3.0
- Spring Boot >= 1.0.1

- A J2EE Container:  Tomcat or Jetty

#### ARGUMENTS


#### EXAMPLES
Below are three simple example of usage. They do not indicate the complexity possible but offer some example of simple invocation.

```
//Print the Google Analytics Tracking ID from cookie
jhc.println(getCookie("_ga").getValue());
:w


//Do some simple math
jhc.println(java.lang.Math.pow(3,3));
:w


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
