package de.nitolia.event.database;

import de.nitolia.event.TCore;

public class DBEntity {

    public void save() {
        TCore.getInst().getDatabase().save(this);
    }

    public void update() {
        TCore.getInst().getDatabase().update(this);
    }

    public void delete() {
        TCore.getInst().getDatabase().delete(this);
    }
}