package ro.andrei.microprofile.demo.controller;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.faulttolerance.ExecutionContext;
import org.eclipse.microprofile.faulttolerance.FallbackHandler;
import ro.andrei.microprofile.demo.dto.Student;
import ro.andrei.microprofile.demo.service.StudentService;

@Slf4j
public class StudentFallbackHandler implements FallbackHandler<Student> {

    @Inject
    private StudentService studentService;

    @Override
    public Student handle(ExecutionContext context) {
        log.info("-------Fallback handler-------");
        return studentService.getById(1);
    }
}
