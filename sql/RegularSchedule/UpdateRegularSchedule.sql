LOCK TABLE RegularSchedule WRITE
@
UPDATE RegularSchedule SET (ScheduleName,ActuatorName,DayMask,Rule,ActuatorOn,Priority,Enabled)=(?,?,?,?,?,?,?) WHERE ScheduleName=?

@
COMMIT