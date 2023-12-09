# Web-Service-based-P2P-file-sharing-client

## JavaFX

```shell
mvn clean javafx:run

```

- Big file create
```shell
dd if=/dev/zero bs=1G count=10 | tr '\0' 'A' > A.txt
```