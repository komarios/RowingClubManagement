echo
date
echo Executing setup.sql ...
time sqlplus -s muze/muz3 @../etc/sql-oracle/setup.sql
echo setup.sql complete
date
echo

