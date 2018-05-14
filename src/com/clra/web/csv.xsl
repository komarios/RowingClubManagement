<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
version="1.0">
<xsl:output method="text" omit-xml-declaration="yes" /> 
<xsl:template match="/">
member_id, account_name, account_type, last_name, first_name, middle_name, name_suffix, address_street1, address_street2, address_city, address_state, address_zip, evening_phone, day_phone, other_phone, email
<xsl:apply-templates/> 
</xsl:template>
<xsl:template match="item">
&quot;<xsl:value-of select="id"/>&quot;, &quot;<xsl:value-of select="accountName"/>&quot;, &quot;<xsl:value-of select="accountType"/>&quot;, &quot;<xsl:value-of select="memberNameBean/lastName"/>&quot;, &quot;<xsl:value-of select="memberNameBean/firstName"/>&quot;, &quot;<xsl:value-of select="memberNameBean/middleName"/>&quot;, &quot;<xsl:value-of select="memberNameBean/suffix"/>&quot;, &quot;<xsl:value-of select="addressBean/street1"/>&quot;, &quot;<xsl:value-of select="addressBean/street2"/>&quot;, &quot;<xsl:value-of select="addressBean/city"/>&quot;, &quot;<xsl:value-of select="addressBean/zip"/>&quot;, &quot;<xsl:value-of select="eveningTelephone"/>&quot;, &quot;<xsl:value-of select="dayTelephone"/>&quot;, &quot;<xsl:value-of select="otherTelephone"/>&quot;, &quot;<xsl:value-of select="email"/>&quot;
</xsl:template>
</xsl:stylesheet>
