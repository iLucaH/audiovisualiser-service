package me.ilucah.audiovisualiser_service.database;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SQLProperties {

    private String url, username, password;

}
