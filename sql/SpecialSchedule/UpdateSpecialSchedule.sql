LOCK TABLE SpecialSchedule WRITE
@
UPDATE SpecialSchedule SET (ScheduleName,ActuatorName,Year,Month,Day,Rule,ActuatorOn,Priority,Enabled)=(?,?,?,?,?,?,?,?,?) WHERE ScheduleName=?

@
COMMIT