 SELECT a.account_name, a.account_passwd, a.name_last, a.name_first, b.role FROM Member a, MemberRole b where a.account_name like 'test%' and a.member_id = b.member_id
 /
