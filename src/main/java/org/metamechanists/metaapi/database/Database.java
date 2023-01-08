/*
 * Copyright (C) 2022 Idra - All Rights Reserved
 */

package org.metamechanists.metaapi.database;

import org.metamechanists.metaapi.MetaAPI;
import org.metamechanists.metaapi.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    private static final javax.sql.DataSource source = DataSource.getDataSource();

    public static void createTables() {
        try (Connection conn = source.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS resource_vaults (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY," +
                            "player VARCHAR(36) NOT NULL," +
                            "INDEX playerIdx (player))"
            );
            statement.execute();
        } catch (SQLException e) {
            Log.tableInitialiseError("");
            e.printStackTrace();
        }
        try (Connection conn = source.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS resource_vaults_upgrades (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, player VARCHAR(36) NOT NULL, tool INT, INDEX playerIdx (player)");
            statement.execute();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not initialise database table: resource_vaults_upgrades.", e);
        }
    }

    public static void dropTables() {
        try (Connection conn = source.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("DROP TABLE IF EXISTS slimefun_limiter");
            statement.execute();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not drop database table: slimefun_limiter.", e);
        }
    }

    public static void insertData(UUID player, String sf_id, long position, UUID world, long time) {
        try (Connection conn = source.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO slimefun_limiter (player, sf_id, position, world, time) VALUES (?, ?, ?, ?, ?);");
            statement.setString(1, player.toString());
            statement.setString(2, sf_id);
            statement.setLong(3, position);
            statement.setString(4, world.toString());
            statement.setLong(5, time);
            statement.execute();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Could not insert data.", e);
        }
    }

    public static void insertData(UUID player, String sf_id, SFLBlockPosition SFLBlockPosition, long time) {
        insertData(player, sf_id, SFLBlockPosition.getPosition(), SFLBlockPosition.getWorld().getUID(), time);
    }

    public static void deleteData(UUID player) {
        try (Connection conn = source.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM slimefun_limiter WHERE player = ?;");
            statement.setString(1, player.toString());
            statement.execute();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Could not delete data.", e);
        }
    }

    public static void deleteData(long position, UUID world) {
        try (Connection conn = source.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM slimefun_limiter WHERE position = ? AND world = ?;");
            statement.setLong(1, position);
            statement.setString(2, world.toString());
            statement.execute();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Could not delete data.", e);
        }
    }

    public static void deleteData(SFLBlockPosition SFLBlockPosition) {
        deleteData(SFLBlockPosition.getPosition(), SFLBlockPosition.getWorld().getUID());
    }

    public static Map<String, Integer> countData(UUID player) {
        try (Connection conn = source.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("SELECT sf_id,COUNT(*) as 'amount' FROM slimefun_limiter WHERE player = ? GROUP BY sf_id;");
            statement.setString(1, player.toString());
            ResultSet rs = statement.executeQuery();
            Map<String, Integer> counts = new HashMap<>();
            while (rs.next()) {
                counts.put(rs.getString("sf_id"), rs.getInt("amount"));
            }
            return counts;
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Could not count data.", e);
            return null;
        }
    }

    public static int countData(UUID player, String sf_id) {
        try (Connection conn = source.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("SELECT COUNT(*) as 'amount' FROM slimefun_limiter WHERE player = ? AND sf_id = ?;");
            statement.setString(1, player.toString());
            statement.setString(2, sf_id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("amount");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Could not count data.", e);
            return 0;
        }
    }

    public static Set<Pair<SFLBlockPosition, Long>> getPositions(UUID player, String sf_id) {
        try (Connection conn = source.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM slimefun_limiter WHERE player = ? AND sf_id = ? ORDER BY time DESC;");
            statement.setString(1, player.toString());
            statement.setString(2, sf_id);
            ResultSet rs = statement.executeQuery();
            Set<Pair<SFLBlockPosition, Long>> positions = new LinkedHashSet<>(rs.getFetchSize());
            while (rs.next()) {
                positions.add(
                        new Pair<>(
                                new SFLBlockPosition(Bukkit.getWorld(UUID.fromString(rs.getString("world"))), rs.getLong("position")),
                                rs.getLong("time")
                        )
                );
            }
            return positions;
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Could not get positions.", e);
            return null;
        }
    }

}
