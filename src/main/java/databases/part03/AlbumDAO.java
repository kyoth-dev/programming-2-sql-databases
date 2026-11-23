
package databases.part03;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import databases.part02.Artist;

/**
 * Data Access Object for the Album table in the Chinook database.
 */
public class AlbumDAO {

    private final String connectionString;

    public AlbumDAO(String jdbcConnection) {
        this.connectionString = jdbcConnection;
    }

    /**
     * Returns all albums belonging to the specified artist.
     */
    public List<Album> getAlbumsByArtist(Artist artist) {

        List<Album> albums = new ArrayList<>();

        String sql = "SELECT AlbumId, Title, ArtistId FROM Album "
                   + "WHERE ArtistId = ? ORDER BY AlbumId ASC";

        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, artist.getId());

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    long id = resultSet.getLong("AlbumId");
                    String title = resultSet.getString("Title");
                    long artistId = resultSet.getLong("ArtistId");

                    Album album = new Album(id, title, artistId);

                    albums.add(album);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return albums;
    }

    /**
     * Adds a new album to the database.
     */
    public boolean addAlbum(Album album) {

        String sql = "INSERT INTO Album (Title, ArtistId) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, album.getTitle());
            statement.setLong(2, album.getArtistId());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Updates an existing album.
     */
    public boolean updateAlbum(Album album) {

        String sql = "UPDATE Album SET Title = ?, ArtistId = ? WHERE AlbumId = ?";

        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, album.getTitle());
            statement.setLong(2, album.getArtistId());
            statement.setLong(3, album.getId());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Deletes an album from the database.
     */
    public boolean deleteAlbum(Album album) {

        String sql = "DELETE FROM Album WHERE AlbumId = ?";

        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, album.getId());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
