package com.adobe.orderapp.metrics;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component("products-database")
@RequiredArgsConstructor
public class DatabaseHealthContributor implements HealthContributor, HealthIndicator {
    private final DataSource ds;

    @Override
    public Health health() {
        try(Connection con = ds.getConnection()) {
            Statement statement = con.createStatement();
            statement.executeQuery("select * from products");
        } catch (SQLException ex) {
            return Health.outOfService().withException(ex).build();
        }
        return Health.up().build();
    }
}
