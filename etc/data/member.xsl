<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/TR/WD-xsl" xmlns:xslt="http://www.w3.org/1999/XSL/Transform">
	<!--XSL Stylesheet for generating Oracle script-->
	<xsl:template match="/">
DELETE FROM Member;
	<xsl:for-each select="//member-list/member">
INSERT INTO Member(member_id,account_name,account_passwd,clra_status,name_last,name_first,name_middle,name_suffix,email,telephone_evening,telephone_day,telephone_other,address_street1,address_street2,address_city,address_state,address_zip,clra_year,birthday) values(clra_member.NEXTVAL,'<xsl:value-of select="email"/>','123456','<xsl:value-of select="clra-date"/>','<xsl:value-of select="member-name/member-name-last"/>','<xsl:value-of select="member-name/member-name-first"/>','<xsl:value-of select="member-name/member-name-middle"/>','<xsl:value-of select="member-name/member-name-suffix"/>','<xsl:value-of select="email"/>','<xsl:value-of select="telephone/telephone-evening"/>','<xsl:value-of select="telephone/telephone-day"/>','<xsl:value-of select="telephone/telephone-other"/>','<xsl:value-of select="address/street1"/>','<xsl:value-of select="address/street2"/>','<xsl:value-of select="address/city"/>','<xsl:value-of select="address/state"/>','<xsl:value-of select="address/zip"/>','<xsl:value-of select="clra-date"/>',NULL);
	</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>

