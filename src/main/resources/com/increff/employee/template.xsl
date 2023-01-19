<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
    <xsl:template match="order">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4" page-height="29.7cm" page-width="21cm" margin-top="1cm"
                                       margin-bottom="2cm" margin-left="2cm" margin-right="2cm">
                    <fo:region-body/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="A4">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block>
                    <fo:inline>
                        <fo:external-graphic src="url(C:\Users\Shreyansh\Desktop\increff\employee-spring-full\src\main\resources\com\increff\employee\increff-logo1.png)" content-width="5.45cm" content-height="2.11cm"/>
                    </fo:inline>                         
                    </fo:block>
                    <fo:block margin="1cm" text-align="center">
                        <fo:inline font-size="20pt" font-weight="bold">
                            Invoice
                        </fo:inline>
                    </fo:block>
                    <fo:block font-size="12pt" font-weight="bold" text-align-last="justify">Invoice No. :
                    <fo:inline space-after="5mm">
                        <xsl:value-of select="orderId"/>
                    </fo:inline>
                    <fo:leader leader-pattern="space" />
                    <fo:inline space-after="5mm">Date :
                        <xsl:value-of select="orderDate"/>
                    </fo:inline>
                    </fo:block>
                    <fo:block font-size="11pt" margin-top="1cm">
                        <fo:table table-layout="fixed" width="100%" border-style="ridge" border-width="2pt">
                            <fo:table-column border-style="solid" border-width="1pt" column-width="40%"/>
                            <fo:table-column border-style="solid" border-width="1pt" column-width="20%"/>
                            <fo:table-column border-style="solid" border-width="1pt" column-width="20%"/>
                            <fo:table-column border-style="solid" border-width="1pt" column-width="20%"/>
                            <fo:table-header>
                                <fo:table-row border-style="double">
                                <fo:table-cell padding="2mm">
                                    <fo:block font-weight="bold">Product Name</fo:block>
                                </fo:table-cell>
                                <fo:table-cell text-align="center" padding="2mm">
                                    <fo:block font-weight="bold">Quantity</fo:block>
                                </fo:table-cell>
                                <fo:table-cell text-align="center" padding="2mm">
                                    <fo:block font-weight="bold">Price per unit</fo:block>
                                </fo:table-cell>
                                <fo:table-cell text-align="center" padding="2mm">
                                    <fo:block font-weight="bold">Amount</fo:block>
                                </fo:table-cell>
                                </fo:table-row>
                            </fo:table-header>
                            <fo:table-footer>
                                <fo:table-row border-style="double">
                                    <fo:table-cell padding="2mm" number-columns-spanned="3">
                                        <fo:block font-weight="bold">Total</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell padding="2mm">
                                        <fo:block font-weight="bold">
                                            <xsl:value-of select="total"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-footer>
                            <fo:table-body>
                                <xsl:apply-templates select="items"/>
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    <xsl:template match="items">
        <fo:table-row border-style="solid" border-width="1pt">
            <fo:table-cell padding="1.5mm">
                <fo:block>
                    <xsl:value-of select="itemName"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell text-align="center" padding="1.5mm">
                <fo:block>
                    <xsl:value-of select="quantity"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell text-align="right" padding="1.5mm">
                <fo:block>
                    <xsl:value-of select="sellingPrice"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell text-align="right" padding="1.5mm">
                <fo:block>
                    <xsl:value-of select="cost"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>
</xsl:stylesheet>