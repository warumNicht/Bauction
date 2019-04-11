package bauction.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class EnumValidatorImpl implements ConstraintValidator<EnumValidator,String> {
    private List<String> enumValues;

    @Override
    public void initialize(EnumValidator constraintAnnotation) {
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClass();
        Enum<?>[] enumConstants = enumClass.getEnumConstants();
        this.enumValues=new ArrayList<>();
        for (Enum<?> enumConstant : enumConstants) {
            this.enumValues.add(enumConstant.name());
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value==null){
            return true;
        }
        return this.enumValues.contains(value);
    }


}
