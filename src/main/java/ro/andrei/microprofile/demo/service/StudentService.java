package ro.andrei.microprofile.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import javax.ws.rs.NotFoundException;
import ro.andrei.microprofile.demo.dto.Address;
import ro.andrei.microprofile.demo.dto.Student;

public class StudentService {
    private static Map<Integer, Student> DB = new HashMap<>();

    static {
        Stream.iterate(1, i -> i + 1).limit(10)
                .forEach(i -> DB.put(i, Student.builder().id(i).name("Name" + i)
                        .location("location" + i)
                        .address(Address.builder()
                                .id(i + 1000)
                                .address("Address" + i)
                                .build())
                        .build()));
    }

    public List<Student> getAll() {
        return new ArrayList<>(DB.values());
    }

    public Student getById(Integer id) {
        if(!DB.containsKey(id)) {
            throw new NotFoundException();
        }
        return DB.get(id);
    }

    public void create(Student student) {
        DB.put(student.getId(), student);
    }

    public void remove(Integer id) {
        DB.remove(id);
    }
}
