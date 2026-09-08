package me.ilucah.audiovisualiser_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "render_state")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RenderState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String name;

    @Column(columnDefinition = "TEXT")
    private String renderState;

    public RenderState(String username, String name, String renderState) {
        this.username = username;
        this.name = name;
        this.renderState = renderState;
    }

}
