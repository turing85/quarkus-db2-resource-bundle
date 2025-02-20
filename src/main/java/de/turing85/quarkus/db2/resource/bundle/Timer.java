package de.turing85.quarkus.db2.resource.bundle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.enterprise.context.ApplicationScoped;

import io.agroal.api.AgroalDataSource;
import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class Timer {
  private final AgroalDataSource dataSource;

  @Scheduled(every = "10s")
  public void poll() {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT 1=1 FROM sysibm.sysdummy1;");
        ResultSet rs = ps.executeQuery()) {
      if (rs.next()) {
        Log.infof("\"SELECT 1=1 FROM sysibm.sysdummy1;\" evaluates to %b", rs.getBoolean(1));
      } else {
        Log.info("nothing found :(");
      }
    } catch (SQLException e) {
      Log.error("Error while polling timer", e);
    }
  }
}
