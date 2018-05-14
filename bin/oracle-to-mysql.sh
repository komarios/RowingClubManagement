echo
date
echo Converting Oracle scripts to mySQL...

awk -f oracle-to-mysql.awk ../etc/sql-oracle/clra_ddl.sql > ../etc/sql-mysql/clra_ddl.sql
awk -f oracle-to-mysql.awk ../etc/sql-oracle/clra-to-fake-member-data.sql > ../etc/sql-mysql/clra-to-fake-member-data.sql
awk -f oracle-to-mysql.awk ../etc/sql-oracle/test_boat.sql > ../etc/sql-mysql/test_boat.sql
awk -f oracle-to-mysql.awk ../etc/sql-oracle/test_memberrole.sql > ../etc/sql-mysql/test_memberrole.sql
awk -f oracle-to-mysql.awk ../etc/sql-oracle/test_member.sql > ../etc/sql-mysql/test_member.sql
awk -f oracle-to-mysql.awk ../etc/sql-oracle/test_oarset.sql > ../etc/sql-mysql/test_oarset.sql
awk -f oracle-to-mysql.awk ../etc/sql-oracle/view_boat.sql > ../etc/sql-mysql/view_boat.sql
awk -f oracle-to-mysql.awk ../etc/sql-oracle/view_member.sql > ../etc/sql-mysql/view_member.sql
awk -f oracle-to-mysql.awk ../etc/sql-oracle/view_oarset.sql > ../etc/sql-mysql/view_oarset.sql
awk -f oracle-to-mysql.awk ../etc/sql-oracle/view_rowingsession.sql > ../etc/sql-mysql/view_rowingsession.sql
awk -f oracle-to-mysql.awk ../etc/sql-oracle/view_testaccounts.sql > ../etc/sql-mysql/view_testaccounts.sql

rm ../etc/sql-oracle/*tmp?

echo ...Conversion complete
date
echo

