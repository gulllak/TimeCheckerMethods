package ru.evgenii.timecheckermethods.service;

public interface TestMethods {
    void testTrackTimeAnnotation(int call);
    void testTrackTimeAsyncAnnotationVoid(int numOfCall);
    Object  testTrackTimeAsyncAnnotationReturn(int numOfCall);
}
