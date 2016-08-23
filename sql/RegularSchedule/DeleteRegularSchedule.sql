LOCK TABLE RegularSchedule WRITE
@
DELETE FROM RegularSchedule WHERE ScheduleName=?

@
COMMIT