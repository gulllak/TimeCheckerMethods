package ru.evgenii.timecheckermethods.service.impl;

import org.springframework.stereotype.Service;
import ru.evgenii.timecheckermethods.annotation.TrackAsyncTime;
import ru.evgenii.timecheckermethods.annotation.TrackTime;
import ru.evgenii.timecheckermethods.service.TestMethods;

@Service
public class TestMethodsImpl implements TestMethods {
    @TrackTime
    @Override
    public void testTrackTimeAnnotation(int call) {
        try {
            Thread.sleep(call * 1000L) ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @TrackAsyncTime
    @Override
    public void testTrackTimeAsyncAnnotationVoid(int call) {
        try {
            Thread.sleep(call * 1000L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @TrackAsyncTime
    @Override
    public Object testTrackTimeAsyncAnnotationReturn(int call) {
        try {
            Thread.sleep(call * 1000L) ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return call;
    }
}
