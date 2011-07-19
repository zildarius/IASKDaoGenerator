package com.kreig133.daogenerator;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class InOutClass {
    static final String PACKAGE = "ru.sbrf.iask.persistence.customer.entity.operation72";
    static final String COMMENT_TO_CLASS = "/**\n" +
                                           " * Generated by com.kreig133.daogenerator.DaoGenerator 0.1\n"+
                                           " * @author eshangareev\n" +
                                           " * @version 1.0\n" +
                                           " */\n";
    static final String IMPORTS = "import java.io.Serializable;\n" +
                                  "import java.util.*;\n\n";


    private List< Parameter > parameters;
    private String name;

    public InOutClass( List< Parameter > parameters, String name) {
        this.parameters = parameters;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("package ");
        result.append(PACKAGE);
        result.append(";\n\n");
        result.append(IMPORTS);
        result.append(COMMENT_TO_CLASS);
        result.append("public class "); result.append(name); result.append(" implements Serializable {\n\n");

        for(Parameter p : parameters){
            result.append(p.toString());
            result.append(p.generateGetter());
            result.append(p.generateSetter());
        }
        result.append("}");

        return result.toString();
    }
}
