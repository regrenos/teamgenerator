<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="Student">
        <name><xsl:value-of select="firstName"/> <xsl:value-of select="lastName"/></name>
    </xsl:template>
</xsl:stylesheet>