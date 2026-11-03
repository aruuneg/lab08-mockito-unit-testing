import mn.edu.must.sqat.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseRegistrationServiceTest {

    @Mock
    StudentRepository repository;

    @Mock
    EmailSender emailSender;

    @InjectMocks
    CourseRegistrationService service;

    @Test
    @DisplayName("Оюутан байхгүй бол ERROR_NO_STUDENT буцаана")
    void returnsErrorWhenStudentMissing() {

        when(repository.findStudent("S1")).thenReturn(null);

        String result = service.register("S1", "CS313");

        assertEquals("ERROR_NO_STUDENT", result);

        verifyNoInteractions(emailSender);
    }

    @Test
    @DisplayName("Идэвхгүй оюутан үед ERROR_INACTIVE_STUDENT")
    void returnsErrorWhenStudentInactive() {

        Student student =
                new Student(
                        "S1",
                        "a@test.com",
                        false,
                        List.of()
                );

        when(repository.findStudent("S1"))
                .thenReturn(student);

        String result = service.register("S1", "CS313");

        assertEquals(
                "ERROR_INACTIVE_STUDENT",
                result
        );

        verifyNoInteractions(emailSender);
    }

    @Test
    @DisplayName("Хичээл байхгүй үед ERROR_NO_COURSE")
    void returnsErrorWhenCourseMissing() {

        Student student =
                new Student(
                        "S1",
                        "a@test.com",
                        true,
                        List.of()
                );

        when(repository.findStudent("S1"))
                .thenReturn(student);

        when(repository.courseExists("CS313"))
                .thenReturn(false);

        String result = service.register("S1", "CS313");

        assertEquals("ERROR_NO_COURSE", result);
    }

    @Test
    @DisplayName("Prerequisite дутуу үед ERROR_PREREQUISITES")
    void returnsErrorWhenPrerequisiteMissing() {

        Student student =
                new Student(
                        "S1",
                        "a@test.com",
                        true,
                        List.of()
                );

        when(repository.findStudent("S1"))
                .thenReturn(student);

        when(repository.courseExists("CS313"))
                .thenReturn(true);

        when(repository.prerequisites("CS313"))
                .thenReturn(List.of("CS201"));

        String result =
                service.register("S1", "CS313");

        assertEquals(
                "ERROR_PREREQUISITES",
                result
        );

        verify(repository, never())
                .saveRegistration(any(), any());
    }

    @Test
    @DisplayName("Амжилттай үед бүртгэл хадгалагдана")
    void savesRegistrationWhenValid() {

        Student student =
                new Student(
                        "S1",
                        "a@test.com",
                        true,
                        List.of("CS201")
                );

        when(repository.findStudent("S1"))
                .thenReturn(student);

        when(repository.courseExists("CS313"))
                .thenReturn(true);

        when(repository.prerequisites("CS313"))
                .thenReturn(List.of("CS201"));

        String result =
                service.register("S1", "CS313");

        assertEquals("OK", result);

        verify(repository)
                .saveRegistration("S1", "CS313");
    }

    @Test
    @DisplayName("Баталгаажуулах имэйл илгээгдэнэ")
    void sendsConfirmationEmail() {

        Student student =
                new Student(
                        "S1",
                        "student@test.com",
                        true,
                        List.of("CS201")
                );

        when(repository.findStudent("S1"))
                .thenReturn(student);

        when(repository.courseExists("CS313"))
                .thenReturn(true);

        when(repository.prerequisites("CS313"))
                .thenReturn(List.of("CS201"));

        service.register("S1", "CS313");

        verify(emailSender)
                .send(
                        eq("student@test.com"),
                        anyString(),
                        contains("CS313")
                );
    }

    @Test
    @DisplayName("Имэйл алдаа өгсөн ч бүртгэл OK хэвээр")
    void emailFailureDoesNotFailRegistration() {

        Student student =
                new Student(
                        "S1",
                        "student@test.com",
                        true,
                        List.of("CS201")
                );

        when(repository.findStudent("S1"))
                .thenReturn(student);

        when(repository.courseExists("CS313"))
                .thenReturn(true);

        when(repository.prerequisites("CS313"))
                .thenReturn(List.of("CS201"));

        doThrow(new RuntimeException("mail"))
                .when(emailSender)
                .send(any(), any(), any());

        String result =
                service.register("S1", "CS313");

        assertEquals("OK", result);
    }

    @Test
    @DisplayName("ArgumentCaptor ашиглан имэйлийн агуулгыг шалгах")
    void capturesEmailBody() {

        Student student =
                new Student(
                        "S1",
                        "student@test.com",
                        true,
                        List.of("CS201")
                );

        when(repository.findStudent("S1"))
                .thenReturn(student);

        when(repository.courseExists("CS313"))
                .thenReturn(true);

        when(repository.prerequisites("CS313"))
                .thenReturn(List.of("CS201"));

        service.register("S1", "CS313");

        ArgumentCaptor<String> bodyCaptor =
                ArgumentCaptor.forClass(String.class);

        verify(emailSender)
                .send(
                        any(),
                        any(),
                        bodyCaptor.capture()
                );

        assertTrue(
                bodyCaptor.getValue().contains("CS313")
        );
    }
}