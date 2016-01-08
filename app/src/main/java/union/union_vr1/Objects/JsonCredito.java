package union.union_vr1.Objects;

/**
 * Created by Usuario on 08/01/2016.
 */
public class JsonCredito {
    private String key;
    private Credito value;

    public JsonCredito(String key, Credito value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Credito getValue() {
        return value;
    }

    public void setValue(Credito value) {
        this.value = value;
    }
}
