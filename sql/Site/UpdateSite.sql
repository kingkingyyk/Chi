LOCK TABLE Site WRITE
@
UPDATE Site SET (SiteName,SiteMapURL)=(?,?) WHERE SiteName=?

@
COMMIT