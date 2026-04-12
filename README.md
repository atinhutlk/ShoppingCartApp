# ShoppingCartApp 



## Prerequisites
- Java 21
- Maven 3.9+
- MariaDB running locally
- SonarQube local at `http://localhost:9000`
- Docker Desktop

## Database Setup
Run the schema script with MariaDB/MySQL client tools:

```sql
SOURCE database/schema.sql;
```

On Windows PowerShell, you can also run:

```powershell
mariadb -u root -p < database/schema.sql
```

This creates:
- `shopping_cart_localization`
- `cart_records`
- `cart_items`
- `localization_strings`

Seed translations included:
- `en_US`, `fi_FI`, `sv_SE`, `ja_JP`, `ar_SA`

## Database Configuration
Connection is managed in `src/main/java/shoppingcartapp/DatabaseConnection.java`.

Environment variables (optional override):
- `DB_URL` (default: `jdbc:mariadb://localhost:3306/shopping_cart_localization`)
- `DB_USER` (default: `root`)
- `DB_PASSWORD` (default: `1234`)

No `.env` file is required; you can set these variables directly in the shell if you want to override the defaults.

PowerShell example:

```powershell
$env:DB_URL="jdbc:mariadb://localhost:3306/shopping_cart_localization"
$env:DB_USER="root"
$env:DB_PASSWORD="1234"
```

## Build, Test, Coverage
```powershell
mvn -B clean verify
```

Current quality gate setup in `pom.xml`:
- JUnit 5 enabled
- JaCoCo report generation enabled
- JaCoCo `check` enforces `LINE` coverage `>= 80%` on testable classes
- Sonar properties point to local server and JaCoCo XML report

> Note: `Main` and `ShoppingCartController` are excluded from coverage gates because they are JavaFX bootstrap/UI classes.

## Run Application
Make sure MariaDB is running and `database/schema.sql` has been applied first.

```powershell
mvn javafx:run
```

## SonarQube Local Scan
```powershell
mvn -B clean verify sonar:sonar "-Dsonar.host.url=http://localhost:9000"
```

## Jenkins Pipeline
`Jenkinsfile` includes these stages:
- Checkout
- Build
- Test
- SonarQube Scan
- Build Docker Image
- Push to Docker Hub

Jenkins assumptions:
- Maven tool name: `Maven3`
- SonarQube server name: `SonarQubeServer`
- Docker Hub credential id: `Docker-Hub`

## Docker
Build image:

```powershell
docker build -t atinhutlk/shoppingcart-gui:latest .
```

Run container locally:

```powershell
docker run --rm atinhutlk/shoppingcart-gui:latest
```

> Note: the Docker image only packages the JavaFX app. MariaDB must still be running separately, and if the database is on your host machine you may need to override `DB_URL` so the container can reach it.

Push manually (if needed):

```powershell
docker push atinhutlk/shoppingcart-gui:latest
```
