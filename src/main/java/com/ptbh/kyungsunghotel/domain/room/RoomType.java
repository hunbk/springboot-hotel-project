package com.ptbh.kyungsunghotel.domain.room;

public enum RoomType {

    SINGLE_OCEAN("SINGLE ROOM, OCEAN VIEW"),
    SINGLE_MOUNTAIN("SINGLE ROOM, MOUNTAIN VIEW"),
    DOUBLE_OCEAN("DOUBLE ROOM, OCEAN VIEW"),
    DOUBLE_MOUNTAIN("DOUBLE ROOM, MOUNTAIN VIEW"),
    TWIN_OCEAN("TWIN ROOM, OCEAN VIEW"),
    TWIN_MOUNTAIN("TWIN ROOM, MOUNTAIN VIEW"),
    FAMILY_OCEAN("FAMILY ROOM, OCEAN VIEW"),
    FAMILY_MOUNTAIN("FAMILY ROOM, MOUNTAIN VIEW");

    private final String name;

    RoomType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
