jpackage \
    --type dmg \
    --name P2PFileShareClient \
    --app-version 1.0 \
    --input target/ \
    --main-jar Web-Service-based-P2P-file-sharing-client-1.0-SNAPSHOT-jar-with-dependencies.jar \
    --main-class kun.FileShareClient \
    --mac-package-identifier kun \
    --mac-package-name P2PClient