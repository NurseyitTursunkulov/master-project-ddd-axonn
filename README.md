# Dieses Projekt demonstriert eine DDD-Anwendung mit Event Sourcing

Die implementierte Anwendung besteht aus mehreren Microservices, die zusammenarbeiten, um ein gemeinsames Ziel zu erreichen

## App Beschreibung

### Article Service

Dieser service bietet einem verifizierten Benutzer die Möglichkeit, einen Artikel zu erstellen und zu veröffentlichen.

### User Profile Service

Dieser service bietet die Möglichkeit, ein Benutzerprofil zu erstellen, das dann zum Erstellen und Veröffentlichen eines Artikels verwendet werden kann.

### Comment Service

Dieser service ermöglicht es einem verifizierten Benutzer, einen Kommentar unter einem veröffentlichten Artikel zu hinterlassen.

### Article Management Service

Dieser service ist für die Koordinierung der Kommunikation zwischen allen services zuständig.

Er bietet Sagas zur Koordinierung der Erstellung von Benutzerprofilen sowie der Erstellung von Artikeln und Kommentaren.

### Starting application with Docker

Vergewissern Sie sich, dass Sie sowohl maven als auch Docker installiert haben, und führen Sie dann die folgenden Befehle aus

```bash
    mvn clean package
    docker compose up --build -d
```
