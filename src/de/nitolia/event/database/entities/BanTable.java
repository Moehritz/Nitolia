package de.nitolia.event.database.entities;


import com.avaje.ebean.validation.NotNull;
import de.nitolia.event.Utils;
import de.nitolia.event.database.DBEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "nit_bans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BanTable extends DBEntity implements Comparable<BanTable> {

    @Id
    private int id;

    @NotNull
    private Timestamp start;

    private Timestamp end;

    @NotNull
    private UUID player;

    private UUID banner;

    private String reason;

    private boolean inactive;

    public boolean calcActive() {
        if (inactive) return false;
        if (end == null) return true;
        return end.after(Utils.currTimestamp());
    }

    @Override
    public int compareTo(BanTable banTable) {
        boolean first = calcActive();
        boolean second = banTable.calcActive();
        if (first == second) return 0;
        if (first) return 1;
        return -1;
    }
}