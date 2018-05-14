echo
date
echo Generating mySQL scripts from Oracle scripts ...
./cleanup-oracle-to-mysql.sh
./oracle-to-mysql.sh
echo Executing setup.sql ...
TMPDIR=$PWD
cd ../etc/sql-mysql
$MYSQL_HOME/bin/mysql -u root -p clra < setup.sql
cd $TMPDIR
echo setup.sql complete
date
echo

