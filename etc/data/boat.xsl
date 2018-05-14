<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/TR/WD-xsl">
	<!--XSL Stylesheet for generating Oracle script-->
	<xsl:template match="/">
DELETE FROM Boat;
	<xsl:for-each select="//boat-list/boat">
INSERT INTO Boat(boat-name,boat-size,boat-type) values( '<xsl:value-of select="boat-name"></xsl:value-of>', '<xsl:value-of select="boat-size"></xsl:value-of>', '<xsl:value-of select="boat-type"></xsl:value-of>' );
	</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
