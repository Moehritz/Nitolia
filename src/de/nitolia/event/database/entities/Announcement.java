package de.nitolia.event.database.entities;

import com.avaje.ebean.validation.NotNull;
import de.nitolia.event.database.DBEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "nit_announcements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Announcement extends DBEntity {

    @Id
    private int id;

    @NotNull
    private String msg;

    private int time;
}