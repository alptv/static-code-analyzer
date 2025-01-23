# Static code analyzer

## Rules
1. **[ShadowedVariablesAnalyzerRule.java](src%2Fmain%2Fjava%2Fanalyzer%2FShadowedVariablesAnalyzerRule.java)**

    Check that variables inside methods does not shadowing class variables.

**Adding rules**

Rule should be inherited from [AnalyzerRule.java](src%2Fmain%2Fjava%2Fanalyzer%2FAnalyzerRule.java) 
and added to rule list in [DefectsDetector.java](src%2Fmain%2Fjava%2FDefectsDetector.java)

## Usage

| Options            | Description                                                  |
|--------------------|--------------------------------------------------------------|
| -s, --src \<path\> | Directory or file to be analyzed. Default value: current dir |
| -h, --help         | Gets help                                                    |

## Build and run
1. Create jar
   - Windows
   ``./gradlew.bat jar``
   - Linux
   ``./gradlew jar``
   
    Resulted jar stored in ``./build/libs/static-code-analyzer.jar``
2. Run jar file 
    ``java -jar ./build/libs/static-code-analyzer.jar -s <path>``

## Testing report
Testing report can be viewed inside github actions 
or 
manually generated with command 
- Windows
  ``./gradlew.bat jacocoTestReport``
- Linux
  ``./gradlew jacocoTestReport``

## Output example

```
Processing source code in: src/test/resources/common
Found following defects:
ShadowedVariableClass.java         : Variable 'a' at 7:13 shadows class variable 'a' at 3:9 in class ShadowedVariableClass
ShadowedVariableClass.java         : Variable 'b' at 11:13 shadows class variable 'b' at 4:9 in class ShadowedVariableClass
ShadowedVariableClass.java         : Variable 'b' at 8:13 shadows class variable 'b' at 4:9 in class ShadowedVariableClass
ShadowedVariableInNestedClass.java : Variable 'i' at 8:17 shadows class variable 'i' at 4:13 in class ShadowedVariableNestedClass
```
