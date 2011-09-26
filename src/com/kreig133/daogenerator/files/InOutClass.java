package com.kreig133.daogenerator.files;

import com.kreig133.daogenerator.common.strategy.FunctionalObjectWithoutFilter;
import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.parameter.Parameter;

import java.util.ArrayList;
import java.util.List;
import static com.kreig133.daogenerator.common.StringBuilderUtils.*;
import static com.kreig133.daogenerator.files.JavaFilesUtils.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class InOutClass {
    private static final String COMMENT_TO_CLASS = "/**\n" +
                                           " * Generated by DaoGenerator 0.2\n"+
                                           " * @author eshangareev\n" +
                                           " * @version 1.0\n" +
                                           " */\n";

    private final String package_;


    private final List< Parameter > parameters;
    private final String name;

    public InOutClass( String package_, List<Parameter> parameters, String name ) {
        this.package_ = package_;
        this.parameters = parameters;
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append( "package " ).append( package_ ).append( ";\n\n" );

        insertImport( builder, "java.io.Serializable" );
        insertImport( builder, "java.util.*" );
        builder.append( "\n" );

        builder.append( COMMENT_TO_CLASS );

        insertClassDeclaration(
                ClassType.Class,
                builder,
                name,
                null,
                new ArrayList<String>(){{add( "Serializable" );}}
        );

        writeSerialVersionUID( builder );
        writeEmptyConstructor( builder );
        writeFullConstructor ( builder );

        for(Parameter p : parameters){
            builder.append( p.toString() );

            p.generateGetter( builder );
            p.generateSetter( builder );
        }
        builder.append( "}" );

        return builder.toString();
    }

    private void writeSerialVersionUID( StringBuilder builder ) {
        builder.append( "\n    private static final long serialVersionUID = " );
        builder.append( (long)( Math.random() * Long.MAX_VALUE ) ).append( "L;\n\n" );
    }

    private void writeEmptyConstructor( StringBuilder builder ) {
        builder.append( "    public " ).append( name ).append( "(){\n    }\n\n" );
    }

    private void writeFullConstructor( StringBuilder builder ) {
        builder.append( "    public " ).append( name ).append( "(\n" );
        iterateForParameterList( builder, parameters, 2, new FunctionalObjectWithoutFilter() {
            @Override
            public void writeString( StringBuilder builder, Parameter p ) {
                builder.append( p.getType() ).append( " " ).append( p.getName() );
            }
        } );

        builder.append( "    ){\n" );
        for( Parameter p: parameters ){
            builder.append( "        this." ).append( p.getName() ).append( " = " ).append( p.getName() ).append(";\n");
        }
        builder.append( "    }\n\n" );
    }

    public String getName() {
        return name;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }
}
