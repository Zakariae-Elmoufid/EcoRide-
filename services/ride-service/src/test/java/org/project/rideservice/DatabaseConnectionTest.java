package org.project.rideservice;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class DatabaseConnectionTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testDatabaseConnection() {
        log.info("====================================");
        log.info("🧪 TEST DE CONNEXION DATABASE");
        log.info("====================================");

        try {
            // Test simple de connexion
            String version = jdbcTemplate.queryForObject("SELECT version()", String.class);

            assertNotNull(version, "La version de PostgreSQL ne devrait pas être null");
            assertTrue(version.contains("PostgreSQL"), "La base devrait être PostgreSQL");

            log.info("✅ Connexion PostgreSQL réussie!");
            log.info("📊 Version: {}", version);

            // Vérifier que JdbcTemplate fonctionne
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            assertEquals(1, result, "La requête SELECT 1 devrait retourner 1");

            log.info("✅ JdbcTemplate fonctionne correctement");
            log.info("====================================");
            log.info("✅ TOUS LES TESTS PASSENT!");
            log.info("====================================");

        } catch (Exception e) {
            log.error("❌ Erreur de connexion: {}", e.getMessage());
            fail("La connexion à la base de données a échoué: " + e.getMessage());
        }
    }

    @Test
    void testDatabaseIsPostgres() {
        String dbName = jdbcTemplate.queryForObject(
            "SELECT current_database()",
            String.class
        );

        log.info("📊 Base de données connectée: {}", dbName);
        assertEquals("ride_db", dbName, "La base devrait être 'ride_db'");
    }
}

