LOCK TABLE SpecialSchedule WRITE
@
DELETE FROM SpecialSchedule WHERE ScheduleName=?

@
COMMIT