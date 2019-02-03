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
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * Created by ashwinxd on 3/5/17.
 */
public class DriverProgram {
    private static final String filePath = "/home/ashwinxd/Desktop/ClearTax/input.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args){
        configureObjectMapper();
        File file = new File(filePath);
        try {
            String json = new Scanner(file).useDelimiter("\\Z").next();
            Equation equation = objectMapper.readValue(json, Equation.class);
            //ANSWER1
            String stringEquation = printEquation(equation);

            //ANSWER2
            String reorganizedEquation = printReorganizedEquation(stringEquation);

            //ANSWER3
            solveEquation(reorganizedEquation);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String printEquation(Equation equation) throws IOException{
        StringBuilder equationBulder = new StringBuilder(getLhs(equation) +" "+ getOp(equation) +" "+ getRhs(equation));//visits the lhs, op and rhs recursively
        String stringEquation = Helper.sanitizeEquation(equationBulder);
        System.out.println("The Equation is: "+stringEquation);
        return stringEquation;
    }

    public static String printReorganizedEquation(String stringEquation){
        String[] sides = stringEquation.split("=");
        String sanitizedLhs = Helper.removeWhiteSpaces(sides[0]);
        String sanitizedRhs = Helper.removeWhiteSpaces(sides[1]);
        StringBuilder lhsBuilder = new StringBuilder(sanitizedLhs);
        StringBuilder rhsBuilder = new StringBuilder(sanitizedRhs);
        reorganize(lhsBuilder, rhsBuilder);
        String newSanitizedRhs = Helper.sanitzeRhs(rhsBuilder);
        String reorganizedEquation ="x = "+newSanitizedRhs;
        System.out.println(reorganizedEquation);
        return reorganizedEquation;
    }

    public static String solveEquation(String reorganizedEquation) throws ScriptException{
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        String solution = engine.eval(reorganizedEquation.split("=")[1]).toString();
        System.out.println("final solution = "+solution);
        return solution;
    }

    //0 remove all white spaces from lhs
    //1 find out the contants which are not in brackets
    //2 find out the op for that constant
    //3 move the constant to the otherside with opposite op
    //4 free the lhs of the outer most brackets and redundant operators
    //5 start from step1 and repeat until only 'x' is left
    private static void reorganize(StringBuilder lhs, StringBuilder rhs){
        if(lhs.toString().equals("x")){
            return;
        }
        Substring constant = Helper.findConstantWithNoBrackets(lhs);
        Operator op = Helper.getOpForConstant(lhs, constant);
        Helper.moveConstantToRhs(constant, op, rhs);
        Helper.removeRedundantOperators(lhs, rhs, constant, op);
        Helper.removeOuterMostBracketsIfPresent(lhs);
        reorganize(lhs, rhs);

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
