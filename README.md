# Web-Service-based-P2P-file-sharing-client

## JavaFX

```shell
mvn clean javafx:run

```

```xml
 <!-- Maven JLink Plugin -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jlink-plugin</artifactId>
                <version>3.1.0</version>
                <executions>
                    <execution>
                        <id>create-jlink</id>
                        <phase>package</phase>
                        <goals>
                            <goal>jlink</goal>
                        </goals>
                        <configuration>
                            <launcher>
                                P2PClient=kun.FileShareClient
                            </launcher>
                            <modulePaths>
                                <!-- Add the path to your additional modules -->
                                <modulePath>${javafx.path}</modulePath>
                            </modulePaths>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
```
```xml
<!-- RESTEasy dependencies -->
        <dependency>
            <groupId>org.jboss.resteasy</groupId>
            <artifactId>resteasy-client</artifactId>
            <version>${resteasy.version}</version>
        </dependency>
        <dependency>
            <groupId>jakarta.activation</groupId>
            <artifactId>jakarta.activation-api</artifactId>
            <version>${jakarta.activation.version}</version>
        </dependency>

        <dependency>
            <groupId>jakarta.ws.rs</groupId>
            <artifactId>jakarta.ws.rs-api</artifactId>
            <version>${jakarta.ws.rs.version}</version>
        </dependency>
```
```xml
<!-- WildFly JAX-RS Client dependencies -->
        <dependency>
            <groupId>org.jboss.resteasy</groupId>
            <artifactId>resteasy-client</artifactId>
            <version>6.2.4.Final</version>
        </dependency>
        <dependency>
            <groupId>org.jboss.resteasy</groupId>
            <artifactId>resteasy-client-api</artifactId>
            <version>6.2.4.Final</version>
        </dependency>
        <dependency>
            <groupId>org.jboss.resteasy</groupId>
            <artifactId>resteasy-client-microprofile</artifactId>
            <version>4.7.9.Final</version>
        </dependency>
```
```xml
<build>
        <plugins>
            <!-- JavaFX Maven Plugin -->
            <plugin>
                <groupId>org.openjfx</groupId>
                <artifactId>javafx-maven-plugin</artifactId>
                <version>0.0.8</version>
                <executions>
                    <execution>
                        <id>create-jlink</id>
                        <phase>package</phase>
                        <goals>
                            <goal>jlink</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <mainClass>kun.FileShareClient</mainClass>
                    <options>
                        <option>--module-path</option>
                        <option>${javafx.path}</option>
                        <option>--add-modules</option>
                        <option>javafx.controls,javafx.fxml,kun,jakarta.ws.rs</option>
                    </options>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

```xml
<!-- Java compiler version -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>${maven.compiler.source}</source>
                    <target>${maven.compiler.target}</target>
                </configuration>
            </plugin>

            <!-- Assembly Plugin to create a JAR with dependencies -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-assembly-plugin</artifactId>
                <version>3.6.0</version>
                <executions>
                    <execution>
                        <id>create-executable-jar</id>
                        <phase>package</phase>
                        <goals>
                            <goal>single</goal>
                        </goals>
                        <configuration>
                            <archive>
                                <manifest>
                                    <mainClass>kun.FileShareClient</mainClass>
                                </manifest>
                            </archive>
                            <descriptorRefs>
                                <descriptorRef>jar-with-dependencies</descriptorRef>
                            </descriptorRefs>
                        </configuration>
                    </execution>
                </executions>
            </plugin>

            <!-- Surefire Plugin for running tests -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.2</version>
            </plugin>
```