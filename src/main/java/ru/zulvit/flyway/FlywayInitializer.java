package ru.zulvit.flyway;

import org.jetbrains.annotations.NotNull;
import org.flywaydb.core.Flyway;

public final class FlywayInitializer {
    private static final @NotNull JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

    public static void initDb() {
        final Flyway flyway = Flyway.configure()
                .dataSource(
                        CREDS.url(),
                        CREDS.login(),
                        CREDS.password()
                )
                .cleanDisabled(false)
                .locations("db")
                .load();
        flyway.clean();
        flyway.migrate();
    }
}
