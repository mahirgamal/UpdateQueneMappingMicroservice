package com.domain;

import com.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateQueueMapping {

    public boolean updateMapping(long publisherId, String oldConsumerQueueName, String oldEventType, String newConsumerQueueName, String newEventType) {
        String query = "UPDATE queue_mapping SET consumer_queuename = ?, event_type = ? WHERE publisher_id = ? AND consumer_queuename = ? AND event_type = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newConsumerQueueName);
            stmt.setString(2, newEventType);
            stmt.setLong(3, publisherId);
            stmt.setString(4, oldConsumerQueueName);
            stmt.setString(5, oldEventType);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
