package com.jeluchu.jchucomponents.smoke

import com.jeluchu.jchucomponents.room.JchuRoomCoroutineContext
import com.jeluchu.jchucomponents.room.sqlMigration

object RoomConsumerSmoke {
    val queryCoroutineContext = JchuRoomCoroutineContext
    val migration =
        sqlMigration(
            startVersion = 1,
            endVersion = 2,
            "ALTER TABLE smoke ADD COLUMN ready INTEGER NOT NULL DEFAULT 0"
        )
}
