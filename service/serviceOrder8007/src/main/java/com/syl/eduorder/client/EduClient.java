package com.syl.eduorder.client;

import com.syl.commonutils.ordervo.CourseWebVoOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-edu")
public interface EduClient {

    @GetMapping("/eduservice/coursefront/getCourseInfoOrder/{courseId}")
    public CourseWebVoOrder getCourseInfoOrder(@PathVariable("courseId") String courseId);
}
