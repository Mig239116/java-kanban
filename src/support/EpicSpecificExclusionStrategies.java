package support;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import model.Epic;

public class EpicSpecificExclusionStrategies implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes field) {
        if (field.getDeclaringClass() == Epic.class &
                (field.getName().equals("status")
                        || field.getName().equals("duration")
                        || field.getName().equals("startTime"))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
