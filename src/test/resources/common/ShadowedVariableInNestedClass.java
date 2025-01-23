
public class ShadowedVariableInNestedClass {
    class ShadowedVariableNestedClass {
        int i = 1;
        int j = 2;

        void z() {
            int i = 10;
        }
    }
}