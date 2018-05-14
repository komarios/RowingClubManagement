<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/TR/WD-xsl">
	<!--XSL Stylesheet for generating Oracle script-->
	<xsl:template match="/">
DELETE FROM Oarset;
	<xsl:for-each select="//oarset-list/oarset">
INSERT INTO Oarset(oarset_id,oarset_name,oarset_size,oarset_type) values( clra_oarset.NEXTVAL, '<xsl:value-of select="oarset-name"></xsl:value-of>', '<xsl:value-of select="oarset-size"></xsl:value-of>', '<xsl:value-of select="oarset-type"></xsl:value-of>' );
	</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
