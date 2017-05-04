package main;

import model.Equation;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.File;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Created by ashwinxd on 4/5/17.
 */
public class ClearTaxMainTest {

    @Test
    public void printEquation() throws Exception {
        String filePath = "/home/ashwinxd/Desktop/ClearTax/input.json";
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(filePath);
        String json = new Scanner(file).useDelimiter("\\Z").next();
        Equation equation = objectMapper.readValue(json, Equation.class);
        String expectedAnswer = "1+(x*10)=21";
        assertTrue(ClearTaxMain.printEquation(equation).replace(" ","").equals(expectedAnswer));
    }

    @Test
    public void printReorganizedEquation() throws Exception {
        String filePath = "/home/ashwinxd/Desktop/ClearTax/input.json";
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(filePath);
        String json = new Scanner(file).useDelimiter("\\Z").next();
        Equation equation = objectMapper.readValue(json, Equation.class);
        String strEquation = ClearTaxMain.printEquation(equation);
        String reorganizedEquation = ClearTaxMain.printReorganizedEquation(strEquation);
        assertTrue(reorganizedEquation.replace(" ","").equals("x=(21-1)/10"));
    }

    @Test
    public void solveEquation() throws Exception {
        String filePath = "/home/ashwinxd/Desktop/ClearTax/input.json";
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(filePath);
        String json = new Scanner(file).useDelimiter("\\Z").next();
        Equation equation = objectMapper.readValue(json, Equation.class);
        String strEquation = ClearTaxMain.printEquation(equation);
        String reorganizedEquation = ClearTaxMain.printReorganizedEquation(strEquation);
        assertTrue(ClearTaxMain.solveEquation(reorganizedEquation).replace(" ","").equals("2"));
    }

}