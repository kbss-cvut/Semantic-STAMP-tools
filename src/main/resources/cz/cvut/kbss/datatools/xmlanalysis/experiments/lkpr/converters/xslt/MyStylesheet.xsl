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

            <xsl:value-of select="$uri"/>
                a :model;
                :id "<xsl:value-of select="attribute::id"/>";
                :modeltype "<xsl:value-of select="attribute::modeltype"/>";
                :name "<xsl:value-of select="attribute::name"/>".

            <xsl:call-template name="instances">
                <xsl:with-param name="modelUri" select="$uri"/>
            </xsl:call-template>

            <xsl:call-template name="connectors">
                <xsl:with-param name="modelUri" select="$uri"/>
            </xsl:call-template>

            :id "<xsl:value-of select="attribute::id"/>";
            :name "<xsl:value-of select="attribute::name"/>";
            :class "<xsl:value-of select="attribute::class"/>";

            <xsl:call-template name="attributes"/>.

        </xsl:for-each>
    </xsl:template>

    <!--INSTANCSE-->
    <xsl:template name="instances">
        <xsl:param name="modelUri"/>
        
        <xsl:for-each select="INSTANCE">
            <xsl:variable name="uri" select="xu:stringToURI(attribute::name)"/>

            <xsl:value-of select="$modelUri"/> :instance <xsl:value-of select="$uri"/>.
            <xsl:value-of select="$uri"/>
            :id "<xsl:value-of select="attribute::id"/>";
            :name "<xsl:value-of select="attribute::name"/>";
            :class "<xsl:value-of select="attribute::class"/>";

            <xsl:call-template name="attributes"/>.
        </xsl:for-each>
    </xsl:template>

    <!--CONNECTORS-->
    <xsl:template name="connectors">
        <xsl:param name="modelUri"/>

        <xsl:for-each select="CONNECTOR">
            <xsl:variable name="uri" select="xu:stringToURI(attribute::class, attribute::id)"/>

            <xsl:value-of select="$modelUri"/> :connector <xsl:value-of select="$uri"/>.
            <xsl:value-of select="$uri"/>
            :from [
                :instance "<xsl:value-of select="concat(':', xu:stringToURI(FROM/@instance))"/>;
                :class <xsl:value-of select="concat(':', xu:stringToURI(FROM/@class))"/>;
            ];
            :to [
                :instance <xsl:value-of select="concat(':', xu:stringToURI(TO/@instance))"/>;
                :class <xsl:value-of select="concat(':', xu:stringToURI(TO/@class))"/>;
            ].

        </xsl:for-each>
    </xsl:template>

    <!--<xsl:template name="connectorEnd">-->
        <!--<xsl:param name="connectorUri"/>-->
        <!--<xsl:param name="property"/>-->
        <!--<xsl:param name="connectorEnd"/>-->
        <!--<xsl:value-of select="$connectorUri"/> <xsl:value-of select="$property"/> [-->
            <!--:instance <xsl:value-of select="concat(':', je:encode(attribute::instance))"/>;-->
            <!--:class <xsl:value-of select="concat(':', je:encode(attribute::class))"/>;-->
        <!--].-->
    <!--</xsl:template>-->

    <!--ATTRIBUTES-->
    <xsl:template name="attributes">
        <xsl:for-each select="ATTRIBUTE">
            <xsl:value-of select="xu:stringToURI('attr', attribute::name)"/> [
              :name "<xsl:value-of select="attribute::name"/>";
              :type "<xsl:value-of select="attribute::type"/>";
              :value "<xsl:value-of select="."/>";
            ];
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>