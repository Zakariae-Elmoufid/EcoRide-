# 🚗 Ride Service - EcoRide Microservices

Service de gestion des trajets pour l'application EcoRide.

---

## 🎯 Fonctionnalités

✅ **POST /api/rides** - Créer un trajet (DRIVER)  
✅ **GET /api/rides** - Rechercher des trajets  
✅ **GET /api/rides/{id}** - Détails d'un trajet  
✅ **PUT /api/rides/{id}/seats** - Décrémenter les places  
✅ **PUT /api/rides/{id}/complete** - Terminer un trajet  
✅ **GET /api/rides/driver/{id}** - Trajets d'un conducteur

---

## 🚀 Démarrage rapide

### Prérequis
- Java 17+
- Docker Desktop
- Maven (inclus via mvnw)

### 1. Démarrer la base de données
```powershell
docker-compose up -d
```

### 2. Démarrer l'application
```powershell
.\mvnw.cmd spring-boot:run
```

Ou utilisez le script tout-en-un :
```powershell
.\start-service.ps1
```

### 3. Tester l'API
```powershell
.\test-api.ps1
```

---

## 📊 Ports et URLs

| Service | Port | URL |
|---------|------|-----|
| Ride Service | 8082 | http://localhost:8082 |
| PostgreSQL | 5433 | localhost:5433 |
| pgAdmin | 5050 | http://localhost:5050 |
| Actuator | 8082 | http://localhost:8082/actuator/health |

---

## 📚 Documentation

- **API Documentation**: Voir [API-DOCUMENTATION.md](API-DOCUMENTATION.md)
- **Résumé complet**: Voir [SUCCESS-SUMMARY.md](SUCCESS-SUMMARY.md)
- **Base de données**: Voir [README-DATABASE.md](README-DATABASE.md)
- **Enum RideStatus**: Voir [ENUM-RIDESTATUS.md](ENUM-RIDESTATUS.md)

---

## 🗄️ Base de données

**PostgreSQL** configuré via Docker Compose :
- Database: `ride_db`
- User: `ride_user`
- Password: `ride_password`
- Port: `5433`

**pgAdmin** disponible sur http://localhost:5050
- Email: `admin@ecoride.com`
- Password: `admin123`

---

## 🏗️ Architecture

```
Controller → Service → Repository → PostgreSQL
              ↓
         Feign Client → User Service
```

### Packages
```
org.project.rideservice/
├── model/          # Entités JPA (Ride, RideStatus)
├── dto/            # DTOs de requête/réponse
├── repository/     # Repositories JPA
├── mapper/         # Mappers Entity ↔ DTO
├── service/        # Logique métier
├── controller/     # Endpoints REST
├── exception/      # Gestion d'erreurs
├── client/         # Clients Feign
└── config/         # Configuration (Security, DB)
```

---

## 🧪 Tests

### Test de connexion DB
```powershell
.\mvnw.cmd test -Dtest=DatabaseConnectionTest
```

### Test complet de l'API
```powershell
.\test-api.ps1
```

### Compilation
```powershell
.\mvnw.cmd clean compile
```

---

## 🔗 Intégrations

### User Service (Oussama)
- **Endpoint**: `GET /api/users/{userId}`
- **Usage**: Récupération des infos du conducteur
- **Client**: `UserServiceClient` (Feign)

### Booking Service (Jarir)
- **Endpoints fournis**:
  - `GET /api/rides/{id}` - Détails pour réservation
  - `PUT /api/rides/{id}/seats` - Décrémenter les places

### Security (Yassin)
- **Config**: `SecurityConfig.java` (base créée)
- **TODOs**: Sécuriser avec JWT et rôles

---

## 📝 Exemples d'utilisation

### Créer un trajet
```bash
curl -X POST http://localhost:8082/api/rides \
  -H "Content-Type: application/json" \
  -d '{
    "driverId": 1,
    "vehicleId": 5,
    "departureTime": "2026-03-15T14:30:00",
    "departureCity": "Casablanca",
    "arrivalCity": "Rabat",
    "availableSeats": 3,
    "pricePerSeat": 50.00
  }'
```

### Rechercher des trajets
```bash
curl "http://localhost:8082/api/rides?from=Casablanca&to=Rabat"
```

### Obtenir un trajet
```bash
curl http://localhost:8082/api/rides/1
```

---

## 🔄 Statuts de trajet

- **OPEN** - Trajet ouvert, places disponibles
- **FULL** - Toutes les places réservées
- **COMPLETED** - Trajet terminé
- **CANCELLED** - Trajet annulé

---

## 📦 Technologies utilisées

- Spring Boot 4.0.3
- Spring Data JPA
- Spring Cloud Netflix Eureka
- Spring Cloud OpenFeign
- PostgreSQL
- Lombok
- Maven

---

## 🛑 Arrêter les services

### Arrêter l'application
`Ctrl + C` dans le terminal

### Arrêter PostgreSQL
```powershell
docker-compose down
```

### Arrêter et supprimer les données
```powershell
docker-compose down -v
```

---

## ✅ Statut

**Implémentation**: ✅ COMPLÈTE  
**Compilation**: ✅ SUCCESS  
**Tests DB**: ✅ PASS  
**Documentation**: ✅ COMPLÈTE

---

## 👥 Équipe

- **Ride Service**: Toi
- **User Service**: Oussama
- **Booking Service**: Jarir
- **Security**: Yassin

---

## 📞 Support

Pour toute question, consultez :
1. [API-DOCUMENTATION.md](API-DOCUMENTATION.md) - Documentation complète
2. [SUCCESS-SUMMARY.md](SUCCESS-SUMMARY.md) - Récapitulatif des tâches
3. Les scripts PowerShell dans le dossier racine

---

**🎉 Ride Service prêt à l'emploi !**

