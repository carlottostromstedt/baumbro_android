package ch.zli.m335.baumbro_android.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "trees")
public class Tree {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "longitude")
    private float longitude;

    @NonNull
    @ColumnInfo(name = "latitude")
    private float latitude;

    @ColumnInfo(name = "kategorie")
    private String kategorie;

    @ColumnInfo(name = "baumartlat")
    private String baumartlat;

    @ColumnInfo(name = "baumgattunglat")
    private String baumgattunglat;

    @ColumnInfo(name = "baumnamedeu")
    private String baumnamedeu;

    @ColumnInfo(name = "baumnamelat")
    private String baumnamelat;

    @ColumnInfo(name = "baumnummer")
    private String baumnummer;

    @ColumnInfo(name = "baumtyptext")
    private String baumtyptext;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public String getKategorie() {
        return kategorie;
    }

    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }

    public String getBaumartlat() {
        return baumartlat;
    }

    public void setBaumartlat(String baumartlat) {
        this.baumartlat = baumartlat;
    }

    public String getBaumgattunglat() {
        return baumgattunglat;
    }

    public void setBaumgattunglat(String baumgattunglat) {
        this.baumgattunglat = baumgattunglat;
    }

    public String getBaumnamedeu() {
        return baumnamedeu;
    }

    public void setBaumnamedeu(String baumnamedeu) {
        this.baumnamedeu = baumnamedeu;
    }

    public String getBaumnamelat() {
        return baumnamelat;
    }

    public void setBaumnamelat(String baumnamelat) {
        this.baumnamelat = baumnamelat;
    }

    public String getBaumnummer() {
        return baumnummer;
    }

    public void setBaumnummer(String baumnummer) {
        this.baumnummer = baumnummer;
    }

    public String getBaumtyptext() {
        return baumtyptext;
    }

    public void setBaumtyptext(String baumtyptext) {
        this.baumtyptext = baumtyptext;
    }
}