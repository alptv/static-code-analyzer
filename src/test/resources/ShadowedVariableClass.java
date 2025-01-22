
public class ShadowedVariableClass {
    int a = 1;
    int b = 2;
    int c;
    void x() {
        int a = 1;
        int b = 1;
    }
    void y() {
        int b = 3;
    }
}