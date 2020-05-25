package ro.andrei.microprofile.demo.controller;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import ro.andrei.microprofile.demo.dto.Student;
import ro.andrei.microprofile.demo.service.StudentService;

@Slf4j
@Path("/student")
@ApplicationScoped
public class StudentController {

    @Inject
    private StudentService studentService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Student> getAll() {
        return studentService.getAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Student getById(@PathParam("id") Integer id) {
        return studentService.getById(id);
    }

    @GET
    @Timeout(500)
    @Fallback(StudentFallbackHandler.class)
//    @Fallback(fallbackMethod = "fallback")
    @Path("/{id}/fallback")
    @Produces(MediaType.APPLICATION_JSON)
    public Student getByIdFallback(@PathParam("id") Integer id) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //
        }
        return studentService.getById(id);
    }

    @GET
    @Timeout(500)
    @Path("/{id}/timeout")
    @Produces(MediaType.APPLICATION_JSON)
    public Student getByIdTimeout(@PathParam("id") Integer id) {
        if (id.equals(3)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                //
            }
        }
        return studentService.getById(id);
    }

    @GET
    @CircuitBreaker(requestVolumeThreshold = 4, delay = 5000)
    @Path("/{id}/cb")
    @Produces(MediaType.APPLICATION_JSON)
    public Student getByIdCircuitBreaker(@PathParam("id") Integer id) {
        if(id.equals(1)) {
            throw new RuntimeException();
        }
        return studentService.getById(id);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteById(@PathParam("id") Integer id) {
        studentService.remove(id);
        return Response.noContent().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Student student) {
        studentService.create(student);
        return Response.ok().build();
    }

    public Student fallback(Integer id) {
        log.info("-----Fallback method-----");
        log.info("id " + id);
        return studentService.getById(1);
    }
}
