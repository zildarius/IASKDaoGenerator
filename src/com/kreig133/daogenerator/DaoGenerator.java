package com.kreig133.daogenerator;

import com.kreig133.daogenerator.common.InOutClass;
import com.kreig133.daogenerator.common.Settings;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.enums.ReturnType;
import com.kreig133.daogenerator.enums.SelectType;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.mybatis.MyBatis;
import com.kreig133.daogenerator.parameter.Parameter;
import com.kreig133.daogenerator.parsers.InputFileParser;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


import static com.kreig133.daogenerator.common.Utils.*;
/**
 * @author eshangareev
 * @version 1.0
 */
public class DaoGenerator  implements Settings {

    public static void main(String[] args) throws IOException {

        JFileChooser fc = new JFileChooser( );
        fc.setMultiSelectionEnabled( false );
        fc.setCurrentDirectory ( new File( System.getProperty("user.dir") ) );
        fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );

        String path = null;

        int returnVal = fc.showOpenDialog( null );

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            OPERATION_NAME = file.getName();
            path = file.getAbsolutePath();
        } else {
            System.exit( 0 );
        }

        returnVal = fc.showSaveDialog( null );
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            OUTPUT_PATH = file.getAbsolutePath()+"/";
            OUTPUT_PATH_FOR_ENTITY = OUTPUT_PATH + "Entity";
        } else {
            System.exit( 0 );
        }

        createDirectoriesIfTheyNotExists();

        MyBatis.prepareFiles    ( instance() );

        for(
                String s:
                ( new File( path ) )
                        .list(
                                new FilenameFilter() {
                                    public boolean accept(File dir, String name) {
                                        return name.endsWith("txt");
                                    }
                                }
                        )
        ) {
            controller(new File(path + "/"+s));
        }
        
        MyBatis.closeFiles      ( instance() );
    }

    private static void controller(
            File fileWithData
    ) throws IOException {
        //считываем название из файла ( название файла = название хранимки, запроса )
        FUNCTION_NAME = fileWithData.getName().split(".txt")[0];

        clearBefore();

        InputFileParser.readFileWithDataForGenerateDao( fileWithData );

        if ( checkToNeedOwnInClass( instance() ) ) {
            createJavaClassForInputOutputEntities( INPUT_PARAMETER_LIST,
                    Utils.convertNameForClassNaming( FUNCTION_NAME ) + "In" );
        }

        if ( OUTPUT_PARAMETER_LIST.size() > 1 ) {
            createJavaClassForInputOutputEntities( OUTPUT_PARAMETER_LIST, Utils.convertNameForClassNaming(
                    FUNCTION_NAME ) +
                    "Out" );
        }

        MyBatis.generateFiles   ( instance() );
    }



    private static void createJavaClassForInputOutputEntities(
            List<Parameter> parameterList,
            String name
    ) throws IOException {

        FileWriter writer = null;
        try {
            InOutClass inOutClass = new InOutClass( ENTITY_PACKAGE, parameterList, name);

            File inClassFile = new File(OUTPUT_PATH_FOR_ENTITY + "/" + inOutClass.getName() + ".java");
            inClassFile.createNewFile();

            writer = new FileWriter(inClassFile);
            writer.write(inOutClass.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }



    private static String FUNCTION_NAME;

    private static String OPERATION_NAME;

    private static String ENTITY_PACKAGE = "com.aplana.sbrf.deposit.persistence.custom.entity.accounts.operation" +
            ".administrative.closecount";
    private static String DAO_PACKAGE = "com.aplana.sbrf.deposit.persistence.custom.entity.accounts.operation" +
            ".administrative.closecount";
    private static String MAPPER_PACKAGE = "com.aplana.sbrf.deposit.persistence.custom.entity.accounts.operation" +
            ".administrative.closecount";

    private final static List<Parameter> INPUT_PARAMETER_LIST  = new ArrayList<Parameter>();
    private final static List<Parameter> OUTPUT_PARAMETER_LIST = new ArrayList<Parameter>();

    private static String OUTPUT_PATH ;
    private static String OUTPUT_PATH_FOR_ENTITY ;

    private static StringBuilder QUERY;



    private static Type       TYPE          = Type.DEPO; //TODO костыль
    private static SelectType SELECT_TYPE;
    private static ReturnType RETURN_TYPE;

    public static void setTYPE( Type TYPE ) {
        DaoGenerator.TYPE = TYPE;
    }

    public static void setSELECT_TYPE( SelectType SELECT_TYPE ) {
        DaoGenerator.SELECT_TYPE = SELECT_TYPE;
    }

    public static void setRETURN_TYPE( ReturnType RETURN_TYPE ) {
        DaoGenerator.RETURN_TYPE = RETURN_TYPE;
    }

    private static void createDirectoriesIfTheyNotExists() {
        final File file = new File(OUTPUT_PATH);
        if(!file.exists()){
            file.mkdirs();
        }

        final File file1 = new File(OUTPUT_PATH_FOR_ENTITY);
        if(!file1.exists()){
            file1.mkdirs();
        }
    }

    private static void clearBefore() {
        INPUT_PARAMETER_LIST.clear();
        OUTPUT_PARAMETER_LIST.clear();
        QUERY = new StringBuilder();
    }

    public Type getType() {
        return TYPE;
    }

    public SelectType getSelectType() {
        return SELECT_TYPE;
    }

    public List<Parameter> getInputParameterList() {
        return INPUT_PARAMETER_LIST;
    }

    public List<Parameter> getOutputParameterList() {
        return OUTPUT_PARAMETER_LIST;
    }

    public StringBuilder getSelectQuery() {
        return QUERY;
    }

    public String getFunctionName() {
        return FUNCTION_NAME;
    }

    public ReturnType getReturnType() {
        return RETURN_TYPE;
    }

    public String getOutputPath() {
        return OUTPUT_PATH;
    }

    @Override
    public String getOperationName() {
        return OPERATION_NAME;
    }

    @Override
    public String getEntityPackage() {
        return ENTITY_PACKAGE;
    }

    @Override
    public String getMapperPackage() {
        return MAPPER_PACKAGE;
    }

    @Override
    public String getDaoPackage() {
        return DAO_PACKAGE;
    }

    private DaoGenerator(){}

    private static class Inner{
        final static DaoGenerator INSTANCE = new DaoGenerator();
    }

    public static DaoGenerator instance(){
        return Inner.INSTANCE;
    }
}
