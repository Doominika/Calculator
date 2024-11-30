// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("calculator");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("calculator")
//      }
//    }
#include <jni.h>

extern "C" {
    JNIEXPORT jdouble JNICALL
    Java_com_example_calculator_Calculator_add(JNIEnv *env, jobject, jdouble a, jdouble b) {
        return a + b;
    }

    JNIEXPORT jdouble JNICALL
    Java_com_example_calculator_Calculator_substract(JNIEnv *env, jobject, jdouble a, jdouble b) {
        return a - b;
    }

    JNIEXPORT jdouble JNICALL
    Java_com_example_calculator_Calculator_multiply(JNIEnv *env, jobject, jdouble a, jdouble b) {
        return a * b;
    }

    JNIEXPORT jdouble JNICALL
    Java_com_example_calculator_Calculator_divide(JNIEnv *env, jobject, jdouble a, jdouble b) {
       if(b == 0.0)
       {
           return 0.0;
       }
        return a / b;
    }
}
