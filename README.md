MenuAPI
=======

API for managing custom inventory menus (in-game GUI interface) using the Bukkt API.

Purely built for the purpose of being used in my plugins. Feel free to use at your own leisure.

Using MenuAPI
=============
I use Maven 3 to build MenuAPI. Simply run the `mvn` command in the project root to compile the code and run the unit tests. You'll get a complete JAR in the `target/` directory with the dependencies appropriately shaded for you.

You can depend on MenuAPI by adding the following to your `pom.xml`

If you're familiar with Maven, you'll be able to automatically download EchoPet as a dependency using the following setup.

```xml
<repositories>
    <repository>
        <id>hawk-repo</id>
        <url>http://ci.hawkfalcon.com/plugin/repository/everything/</url>
    </repository>
<!-- And so on... -->
</repositories>
```

```xml
<dependencies>
    <dependency>
        <groupId>com.dsh105<groupId>
        <artifactId>MenuAPI</artifactId>
        <version>1.0.0</version>
    </dependency>
<!-- And so on... -->
</dependencies>
```

Development Builds
=================

You can grab the latest build from [Jenkins](http://ci.hawkfalcon.com/view/DSH105/job/MenuAPI/).

JavaDoc
=======

JavaDocs are available on the [Jenkins](http://ci.hawkfalcon.com/view/DSH105/job/MenuAPI/javadoc/).
