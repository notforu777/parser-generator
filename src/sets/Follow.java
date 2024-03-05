package sets;

import java.util.Map;
import java.util.Set;

public class Follow {
    private Map<String, Set<String>> entity;

    public Follow(Map<String, Set<String>> entity) {
        this.entity = entity;
    }

    public Map<String, Set<String>> getEntity() {
        return entity;
    }

    public void setEntity(Map<String, Set<String>> entity) {
        this.entity = entity;
    }
}
