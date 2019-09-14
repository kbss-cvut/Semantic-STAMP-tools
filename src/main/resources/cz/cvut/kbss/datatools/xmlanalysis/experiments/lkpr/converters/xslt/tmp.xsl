<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xu="xalan://cz.cvut.kbss.datatools.xmlanalysis.experiments.lkpr.converters.xslt.TransformUtils">

    <xsl:output media-type="text/plain" indent="yes" omit-xml-declaration="yes"/>

    <xsl:template match="/">
        <xsl:value-of select="xu:test()"/>

        <xsl:value-of select="xu:hello('asdf')"/>

        <xsl:value-of select="xu:hello(//INSTANCE/@class)"/>
    </xsl:template>

</xsl:stylesheet>