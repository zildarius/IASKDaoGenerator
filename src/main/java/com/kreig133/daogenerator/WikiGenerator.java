package com.kreig133.daogenerator;

import com.kreig133.daogenerator.settings.Settings;

import java.io.IOException;

/**
 * @author eshangareev
 * @version 1.0
 */
public class WikiGenerator {
    public static void generateWiki( String path ) throws IOException {

        final String[] xmlFileNamesInDirectory = DaoGenerator.getXmlFileNamesInDirectory( path );

        for ( String s : xmlFileNamesInDirectory ) {
            generateWikiForXmlFile( path + "\\" +  s );
        }
    }

    public static void generateWikiForXmlFile( String xmlFileName ) throws IOException {
        final String[] cmdarray = { "cmd", "/C",
                "java  -classpath DaoGenerator-2.3.jar org.apache.xalan.xslt.Process " +
                        "-IN " + xmlFileName +
                        " -XSL XmlToWiki.xsl " +
                        "-OUT " + xmlFileName + ".txt" };

        for ( String s1 : cmdarray ) {
            System.out.println( s1 );
        }

        Runtime.getRuntime().exec( cmdarray );
        System.out.println( " END " );
    }
}
