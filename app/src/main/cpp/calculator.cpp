#include <jni.h>

extern "C" {
    JNIEXPORT jdouble JNICALL
    Java_com_example_calculator_Calculator_add(JNIEnv *env, jobject, jdouble a, jdouble b) {
        return a + b;
    }

    JNIEXPORT jdouble JNICALL
    Java_com_example_calculator_Calculator_subtract(JNIEnv *env, jobject, jdouble a, jdouble b) {
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

    JNIEXPORT jdouble JNICALL
    Java_com_example_calculator_Calculator_convertToPercentage(JNIEnv *env, jobject, jdouble a) {
        return (a / 100.0);
    }

    JNIEXPORT jdouble JNICALL
    Java_com_example_calculator_Calculator_calculatePercentage(JNIEnv *env, jobject, jdouble a, jdouble b) {
        return (a / 100.0) * b;
    }
}
