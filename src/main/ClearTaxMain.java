package main;

import helper.Helper;
import model.Equation;
import model.Operator;
import model.Substring;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * Created by ashwinxd on 3/5/17.
 */
public class ClearTaxMain {
    private static final String filePath = "/home/ashwinxd/Desktop/ClearTax/input1.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args){
        configureObjectMapper();
        File file = new File(filePath);
        try {
            String json = new Scanner(file).useDelimiter("\\Z").next();
            Equation equation = objectMapper.readValue(json, Equation.class);
            StringBuilder equationBulder = new StringBuilder(getLhs(equation) +" "+ getOp(equation) +" "+ getRhs(equation));
            String stringEquation = Helper.sanitizeEquation(equationBulder);

            //ANSWER1
            System.out.println("The model.Equation is: "+stringEquation);

            String lhs = stringEquation.split("=")[0];
            String rhs = stringEquation.split("=")[1];
            String sanitizedLhs = Helper.removeWhiteSpaces(lhs);
            String sanitizedRhs = Helper.removeWhiteSpaces(rhs);
            StringBuilder lhsBuilder = new StringBuilder(sanitizedLhs);
            StringBuilder rhsBuilder = new StringBuilder(sanitizedRhs);
            moveToRhs(lhsBuilder, rhsBuilder);

            //ANSWER2
            String newSanitizedRhs = Helper.sanitzeRhs(rhsBuilder);
            System.out.println("x = "+newSanitizedRhs);

            //ANSWER3
            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("JavaScript");
            System.out.println("final solution = "+engine.eval(newSanitizedRhs));


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //0 remove all white spaces from lhs
    //1 find out the contants which are not in brackets
    //2 find out the op for that constant
    //3 move the constant to the otherside with opposite op
    //4 free the lhs of the outer most brackets and redundant operators
    //5 start from step1 and repeat until only 'x' is left
    private static void moveToRhs(StringBuilder lhs, StringBuilder rhs){
        if(lhs.toString().equals("x")){
            return;
        }
        Substring constant = Helper.findConstantWithNoBrackets(lhs);
        Operator op = Helper.getOpForConstant(lhs, constant);
        Helper.moveConstantToRhs(constant, op, rhs);
        Helper.removeRedundantOperators(lhs, rhs, constant, op);
        Helper.removeOuterMostBracketsIfPresent(lhs);
        moveToRhs(lhs, rhs);

    }



    //returns the rhs of the equation
    private static String getRhs(Equation equation) throws IOException{
        try{
            Integer.parseInt(equation.getRhs().toString());
            return equation.getRhs().toString();
        }catch (NumberFormatException e){
        }
        Equation subEquation = Helper.constructEquation((HashMap<String, Object>) equation.getRhs());
        return "("+getLhs(subEquation) +" "+ getOp(subEquation) +" "+ getRhs(subEquation)+")";
    }

    //returns the lhs of the equation
    private static String getLhs(Equation equation) throws IOException{
        try{
            if(equation.getLhs().equals("x")) return equation.getLhs().toString();
            Integer.parseInt(equation.getLhs().toString());
            return equation.getLhs().toString();
        }catch (NumberFormatException e){
        }
        Equation subEquation = Helper.constructEquation((HashMap<String, Object>) equation.getLhs());
        return "("+getLhs(subEquation) +" "+ getOp(subEquation) +" "+ getRhs(subEquation)+")";
    }

    private static String getOp(Equation equation){
        return Helper.opMap.get(equation.getOp());
    }

    private static void configureObjectMapper(){
        objectMapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }




}
