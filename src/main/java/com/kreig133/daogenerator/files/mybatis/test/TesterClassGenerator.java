package com.kreig133.daogenerator.files.mybatis.test;

import com.kreig133.daogenerator.MavenProjectGenerator;
import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.enums.Scope;
import com.kreig133.daogenerator.files.JavaClassGenerator;
import com.kreig133.daogenerator.files.PackageAndFileUtils;
import com.kreig133.daogenerator.files.mybatis.mapping.MappingGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.settings.Settings;

import java.io.File;
import java.io.IOException;

/**
 * @author eshangareev
 * @version 1.0
 */
public class TesterClassGenerator extends JavaClassGenerator{

    @Override
    public File getFile() throws IOException {
        final File file = new File(
                Settings.settings().getPathForGeneratedTests() + "/"
                        + PackageAndFileUtils.replacePointBySlash( Settings.settings().getMapperPackage() )+ "/"
                        + getFileName()
                        + JAVA_EXTENSION
        );
        PackageAndFileUtils.createDirsAndFile( file.getParentFile() );
        return file;
    }

    @Override
    public void generateHead() throws IOException {
        setPackage( Settings.settings().getMapperPackage() );
        insertLine();
        addImport( "com.aplana.sbrf.deposit.AbstractDepoDaoExecuteTest" );
        addImport( "org.junit.Test" );
        addImport( "org.springframework.beans.factory.annotation.Autowired" );
        addImport( "org.springframework.test.context.ContextConfiguration" );
        addImport( "org.springframework.test.context.junit4.SpringJUnit4ClassRunner" );
        addImport( Settings.settings().getMapperPackage() + "." + MappingGenerator.instance().getFileName() );
        addImport( "java.util.HashMap" );
        addImport( "java.util.Map" );
        insertLine();
        builder.append( "@ContextConfiguration(locations = \"").append( MavenProjectGenerator.getConfigName() )
                .append( ".xml\" )" );
        insertLine();
        insertClassDeclaration( ClassType.CLASS, getFileName(), "AbstractDepoDaoExecuteTest", null );
        insertTabs().append( "@Autowired" );
        insertLine();
        insertTabs().append( MappingGenerator.instance().getFileName() ).append( " " )
                .append( "dao" ).append( ";" );
        insertLine();
    }

    @Override
    public void generateBody( DaoMethod daoMethod ) throws IOException {
        insertLine();
        insertTabs().append( "@Test" );
        insertLine();
        generateMethodSignature(
                Scope.PUBLIC,
                null,
                daoMethod.getCommon().getMethodName() + "RunTest",
                null,
                null,
                false
        );
        insertLine();
        insertTabs().append( "final Map<String, String> values = new HashMap<String, String>();" );
        insertLine();
        for ( ParameterType parameterType : daoMethod.getInputParametrs().getParameter() ) {
            insertTabs().append( "values.put( \"" ).append( parameterType.getRenameTo() ).append( "\", \"" )
                    .append( parameterType.getTestValue() ).append( "\" );" );
            insertLine();
        }
        insertLine();
        insertTabs().append( "final String methodName = \"" ).append( daoMethod.getCommon().getMethodName() )
                .append( "\";" );
        insertLine();
        insertLine();
        insertTabs().append( "Object invoke = invoke( dao, values, methodName );" );
        insertLine();
        insertLine();
        insertTabs().append( "org.junit.Assert.assertNotNull( invoke );" );
        insertLine();
        insertLine();
        closeMethodOrInnerClassDefinition();
    }

    @Override
    public String getFileName() {
        return MappingGenerator.instance().getFileName() + "Test";
    }
}