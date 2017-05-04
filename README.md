# EvaluateExpression
Program to read a single variable equation from json and solve it

It can read an input equation in this form
```
{

"op": "equal",

"lhs": {

        "op": "add",

        "lhs": 1,

        "rhs": {

                "op": "multiply",

                "lhs": "x",

                "rhs": 10

                }
         },
"rhs": 21

}
```
When the program is executed with the above file it prints : 

```
The Equation is: 1 + (x * 10) = 21
x = (21-1)/10
final solution = 2
```
