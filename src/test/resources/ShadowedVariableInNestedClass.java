
public class ShadowedVariableInNestedClass {
    int a = 1;
    int b = 2;
    void x() {
        int d = 1;
    }
    void y() {
        int e = 3;
    }

    class ShadowedVariableNestedClass {
        int i = 1;
        int j = 2;

        void z() {
            int i = 10;
        }
    }
}