package support;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class EpicTestingExclusionStrategies implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes field) {
        if (field.getName().equals("status")
                        | field.getName().equals("duration")
                        | field.getName().equals("startTime")
                        | field.getName().equals("endTime")
                        | field.getName().equals("subtaskReferences")
                        | field.getName().equals("taskID")
                        ) {
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}



