LOCK TABLE DayScheduleRule WRITE
@
DELETE FROM DayScheduleRule WHERE RuleName=?

@
COMMIT