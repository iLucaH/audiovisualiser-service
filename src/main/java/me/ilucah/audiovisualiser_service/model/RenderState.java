package me.ilucah.audiovisualiser_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "RenderState")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RenderState {

    @Id
    @GeneratedValue
    private int id;

    private String username;
    private String renderState;

    public RenderState(String username, String renderState) {
        this.username = username;
        this.renderState = renderState;

    }

}
