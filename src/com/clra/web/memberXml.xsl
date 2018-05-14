<?xml version="1.0"?> 
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
version="1.0">
<xsl:template match="/">
<members>
<xsl:apply-templates/> 
</members>
</xsl:template>
<xsl:template match="item">
<member>
  <accountId><xsl:value-of select="id"/></accountId>
  <accountName><xsl:value-of select="accountName"/></accountName>
  <accountType><xsl:value-of select="accountType"/></accountType>
  <memberNameLast><xsl:value-of select="memberNameBean/lastName"/></memberNameLast>
  <memberNameFirst><xsl:value-of select="memberNameBean/firstName"/></memberNameFirst>
  <memberNameMiddle><xsl:value-of select="memberNameBean/middleName"/></memberNameMiddle>
  <memberNameSuffix><xsl:value-of select="memberNameBean/suffix"/></memberNameSuffix>
  <addressStreet1><xsl:value-of select="addressBean/street1"/></addressStreet1>
  <addressStreet2><xsl:value-of select="addressBean/street2"/></addressStreet2>
  <addressCity><xsl:value-of select="addressBean/city"/></addressCity>
  <addressState><xsl:value-of select="addressBean/state"/></addressState>
  <addressZip><xsl:value-of select="addressBean/zip"/></addressZip>
  <telephoneEvening><xsl:value-of select="eveningTelephone"/></telephoneEvening>
  <telephoneDay><xsl:value-of select="dayTelephone"/></telephoneDay>
  <telephoneOther><xsl:value-of select="otherTelephone"/></telephoneOther>
  <email><xsl:value-of select="email"/></email>
</member>
</xsl:template>
</xsl:stylesheet>
