<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
    <xsl:output method="xml" version="1.0" omit-xml-declaration="no" indent="yes"/>
    <xsl:template match="Group">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master margin-right="1in"  margin-left="1in"
                                       margin-bottom="1in" margin-top="1in"
                                       page-width="8.5in"  page-height="11in"
                                       master-name="normal">
                    <fo:region-body margin="20mm 10mm" />
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="normal">
                <fo:flow flow-name="xsl-region-body">
                    <fo:table inline-progression-dimension="auto" table-layout="auto">
                        <fo:table-column column-width="proportional-column-width(1)"/>
                        <fo:table-column column-width="proportional-column-width(1)" border="solid 0.1mm black"/>
                        <fo:table-column column-width="proportional-column-width(1)"/>
                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell column-number="2" border="solid 0.1mm black" text-align="center">
                                    <fo:block>
                                        <xsl:text>Group </xsl:text>
                                        <xsl:value-of select="@groupNumber"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <xsl:for-each select="member">
                                <fo:table-row>
                                    <fo:table-cell column-number="2" text-align="center">
                                        <fo:block>
                                            <xsl:value-of select="@firstName"/>
                                            <xsl:text> </xsl:text>
                                            <xsl:value-of select="@lastName"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </xsl:for-each>
                        </fo:table-body>
                    </fo:table>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>