import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonSetter;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created by ashwinxd on 3/5/17.
 */
public class Equation {

    private String op;
    private Object rhs;
    private Object lhs;




    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public Object getRhs() {
        return rhs;
    }

    public void setRhs(Object rhs) {
        this.rhs = rhs;
    }

    public Object getLhs() {
        return lhs;
    }

    public void setLhs(Object lhs) {
        this.lhs = lhs;
    }
}
