SHOW schemas;
USE dat375;

SELECT * FROM dat375.boroughdat375;

DESCRIBE dat375.stormcrimes;
SELECT * FROM dat375.stormcrimes;

SELECT * FROM dat375.stormcrimes
WHERE CrimeEventID <> 0
AND StormEventID <>0;

SELECT Date, CrimeActivity, StormActivity, Zone, City FROM dat375.stormcrimes;

SELECT Date, CrimeActivity, StormActivity, Zone, City FROM dat375.stormcrimes
WHERE CrimeEventID <> 0 AND StormEventID <>0;

SELECT count(*) FROM dat375.stormcrimes
WHERE CrimeEventID = 0
OR StormEventID = 0;

SELECT * FROM dat375.stormcrimes
WHERE CrimeEventID = 0
OR StormEventID = 0;

SELECT * FROM dat375.stormcrimes
WHERE CrimeEventID = 0;

SELECT * FROM dat375.stormcrimes
WHERE StormEventID = 0;

SELECT distinct(StormActivity), count(*) 
AS NumberOfStorms FROM dat375.stormcrimes
WHERE CrimeEventID !=0 AND StormEventID !=0
GROUP BY StormActivity
ORDER BY NumberOfStorms DESC;

SELECT distinct(CrimeActivity), count(*)
AS NumberOfCrimes FROM dat375.stormcrimes
WHERE CrimeEventID <> 0 AND StormEventID <>0
GROUP BY CrimeActivity
ORDER BY NumberOfCrimes DESC;

SELECT City, count(*) AS NumberOfCrimes
FROM dat375.stormcrimes -- Select the City and the count
WHERE CrimeEventID <> 0 AND StormEventID <> 0
GROUP BY City -- Group the rows by City before counting
ORDER BY NumberOfCrimes DESC; -- Order the final result by City

SELECT distinct(ZoneCityID), count(*) AS NumberOfCrimes FROM dat375.stormcrimes
WHERE CrimeEventID <> 0 AND StormEventID <>0
GROUP BY ZoneCityID
ORDER BY NumberOfCrimes DESC;