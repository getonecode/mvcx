package guda.mvcx.core.helper;


import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by well on 2017/3/20.
 */
public class Form {

    private boolean error;
    private Map<String, String> errorResult = new HashMap<String, String>();

    public boolean validateError() {
        ValidatorFactory valiatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = valiatorFactory.getValidator();
        Set<ConstraintViolation<Form>> set = validator.validate(this);
        if (set.size() > 0) {
            error = true;
            for (ConstraintViolation<Form> val : set) {
                errorResult.put(val.getPropertyPath().toString(), val.getMessage());
            }
        }
        return error;
    }

    public void reject(String fieldName,String errorMsg){
        errorResult.put(fieldName,errorMsg);
    }


    public Map<String, String> getErrorResult() {
        return errorResult;
    }

    public void setErrorResult(Map<String, String> errorResult) {
        this.errorResult = errorResult;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
