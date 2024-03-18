package ch.zli.m335.baumbro_android.database;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TreeDao {
    @Query("SELECT * FROM trees")
    List<Tree> getAll();

    @Query("SELECT * FROM trees\n" +
            "WHERE latitude > :latitudeMin AND latitude < :latitudeMax " +
            "AND longitude > :longitudeMin AND longitude < :longitudeMax")
    List<Tree> getNearbyTrees(float latitudeMin, float longitudeMin, float latitudeMax, float longitudeMax);

    @Query("SELECT * FROM trees LIMIT 1")
    Tree getFirst();

    @Query("SELECT * FROM trees\n" +
            "WHERE baumnummer=:treeNumber\n" +
            "LIMIT 1")
    Tree findByTreeNumber(String treeNumber);

    @Query("SELECT * FROM trees\n" +
            "WHERE ROUND(longitude, 1) = ROUND(:longitude, 1) \n" +
            "AND ROUND(latitude, 1) = ROUND(:latitude, 1)\n" +
            "LIMIT 1")
    Tree findByLocation(float longitude, float latitude);

}
