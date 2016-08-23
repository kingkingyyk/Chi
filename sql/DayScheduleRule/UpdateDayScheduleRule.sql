LOCK TABLE DayScheduleRule WRITE
@
UPDATE DayScheduleRule SET (RuleName,StartHour,StartMinute,EndHour,EndMinute)=(?,?,?,?,?) WHERE RuleName=?

@
COMMIT