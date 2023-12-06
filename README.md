# Web-Service-based-P2P-file-sharing-client

## JavaFX

```shell
mvn clean javafx:run

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