# ShoppingCartApp (Week 3 / AD)

Week 3 extension of the JavaFX shopping cart app with:
- database localization from `localization_strings`
- cart summary persistence in `cart_records`
- cart item persistence in `cart_items` (foreign key to `cart_records`)

## Prerequisites
- Java 21
- Maven 3.9+
- MySQL or MariaDB running locally
- SonarQube local at `http://localhost:9000`
- Docker Desktop

## Database Setup
Run the schema script:

```sql
SOURCE database/schema.sql;
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
- `DB_URL` (default: `jdbc:mysql://localhost:3306/shopping_cart_localization?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`)
- `DB_USER` (default: `root`)
- `DB_PASSWORD` (default: `1234`)

PowerShell example:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/shopping_cart_localization?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
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
- SonarQube server name: `SonarQube Server`
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

Push manually (if needed):

```powershell
docker push atinhutlk/shoppingcart-gui:latest
```
