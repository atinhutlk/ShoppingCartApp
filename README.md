# ShoppingCartApp (Week 3 / AD)

Week 3 extension of the JavaFX shopping cart app with:
- database localization from `localization_strings`
- cart summary persistence in `cart_records`
- cart item persistence in `cart_items` (foreign key to `cart_records`)

## Prerequisites
- Java 21
- Maven 3.9+
- MySQL or MariaDB running locally

## Quick Start
```powershell
Set-Location "C:\Users\Nhut Vo\Desktop\Metropolia\2nd year\SWP_1 Assignments\ShoppingCartApp"
mvn test
mvn javafx:run
```

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
Connection is managed in `src/main/java/DatabaseConnection.java`.

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

## Build and Run
Build:

```powershell
mvn clean install
```

Run app:

```powershell
mvn javafx:run
```

Run tests:

```powershell
mvn test
```

Package JAR:

```powershell
mvn clean package
```

Main class: `Main`

## Docker
Build image:

```powershell
docker build -t shoppingcartapp .
```

Run container:

```powershell
docker run --rm shoppingcartapp
```

## Code Coverage (SonarQube)
Coverage metrics exclude `shoppingcartapp.Main` and `shoppingcartapp.ShoppingCartController`
via `sonar.coverage.exclusions` in `pom.xml`.

Reason: both classes are JavaFX bootstrap/UI flow classes and are not practical targets for stable
unit tests in this project setup. This keeps coverage focused on testable business/service logic.
