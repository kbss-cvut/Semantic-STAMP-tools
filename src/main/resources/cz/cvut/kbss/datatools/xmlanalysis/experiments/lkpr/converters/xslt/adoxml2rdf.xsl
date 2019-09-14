<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:je="xalan://java.net.URLEncoder"
                xmlns:xu="xalan://cz.cvut.kbss.datatools.xmlanalysis.experiments.lkpr.converters.xslt.TransformUtils"
                xmlns:rdft="xalan://cz.cvut.kbss.datatools.xmlanalysis.experiments.lkpr.converters.xslt.RDFTransform">
    <!--xmlns:xsl="http://www.w3.org/1999/XSL/Transform"-->
    <!--xmlns:xalan="http://xml.apache.org/xalan"-->
    <!--xmlns:custom="http://youdomain.ext/custom"-->

    <!--<xalan:component functions="uriencode" prefix="custom">-->
    <!--<xalan:script lang="javascript">-->
    <!--function uriencode(string) {-->
    <!--return encodeURIComponent(string);-->
    <!--}-->
    <!--</xalan:script>-->
    <!--</xalan:component>-->

    <xsl:output media-type="text/plain" indent="yes" omit-xml-declaration="yes"/>


    <xsl:template match="/">
        <xsl:text>
        </xsl:text>
        <xsl:call-template name="models"/>
    </xsl:template>


    <!--MODELS-->
    <xsl:template name="models">
        <xsl:for-each select="//MODELS/MODEL">
            <xsl:variable name="uri" select="xu:stringToURI(attribute::name)"/>
            <xsl:value-of select="rdft:reset()"/>

            <xsl:value-of select="rdft:uri('rdf',$uri)"/>
            <xsl:value-of select="rdft:str(rdft:trickyString())"/>
            <!--<xsl:value-of select="rdft:ns(xsl:)"/>-->
            <!--<xsl:value-of select="rdft:add(rdft:uri($uri), 'a', rdft:uri(':model'))"/>-->
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>