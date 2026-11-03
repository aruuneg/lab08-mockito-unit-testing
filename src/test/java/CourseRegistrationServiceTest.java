package mn.edu.must.sqat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    @BeforeEach
    void setUp() {
        service =
                new CourseRegistrationService(
                        repository,
                        emailSender
                );
    }

    @Test
    @DisplayName("Амжилттай бүртгэл OK буцаана")
    void savesRegistrationWhenValid() {

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

        String result =
                service.register("S1", "CS313");

        assertEquals("OK", result);

        verify(repository)
                .saveRegistration("S1", "CS313");

        verify(emailSender)
                .send(
                        eq("student@test.com"),
                        anyString(),
                        contains("CS313")
                );
    }

    @Test
    @DisplayName("Оюутан байхгүй үед ERROR_NO_STUDENT")
    void returnsErrorWhenStudentMissing() {

        when(repository.findStudent("S1"))
                .thenReturn(null);

        String result =
                service.register("S1", "CS313");

        assertEquals(
                "ERROR_NO_STUDENT",
                result
        );

        verify(repository, never())
                .saveRegistration(any(), any());

        verify(emailSender, never())
                .send(any(), any(), any());
    }

    @Test
    @DisplayName("Идэвхгүй оюутан бүртгэгдэхгүй")
    void doesNotRegisterInactiveStudent() {

        Student student =
                new Student(
                        "S1",
                        "a@test.com",
                        false,
                        List.of()
                );

        when(repository.findStudent("S1"))
                .thenReturn(student);

        String result =
                service.register("S1", "CS313");

        assertEquals(
                "ERROR_INACTIVE_STUDENT",
                result
        );

        verify(repository, never())
                .saveRegistration(any(), any());

        verifyNoInteractions(emailSender);
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
    @DisplayName("Имэйл алдаа өгсөн ч бүртгэл OK хэвээр үлдэнэ")
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

        verify(repository)
                .saveRegistration("S1", "CS313");
    }


    @Test
    @DisplayName("null courseId үед exception")
    void nullCourseIdShouldThrowException() {

        assertThrows(
                IllegalArgumentException.class,
                () -> service.register("S1", null)
        );

        verifyNoInteractions(repository);
        verifyNoInteractions(emailSender);
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

    @ParameterizedTest
    @CsvSource({
            "MISSING,ERROR_NO_STUDENT",
            "INACTIVE,ERROR_INACTIVE_STUDENT",
            "NO_COURSE,ERROR_NO_COURSE"
    })
    @DisplayName("Гурван алдааны ангийг нэг параметржүүлсэн тестээр")
    void errorScenariosParameterizedTest(
            String scenario,
            String expectedResult) {

        if (scenario.equals("MISSING")) {

            lenient()
                    .when(repository.findStudent("S1"))
                    .thenReturn(null);
        }

        if (scenario.equals("INACTIVE")) {

            Student student =
                    new Student(
                            "S1",
                            "a@test.com",
                            false,
                            List.of()
                    );

            lenient()
                    .when(repository.findStudent("S1"))
                    .thenReturn(student);
        }

        if (scenario.equals("NO_COURSE")) {

            Student student =
                    new Student(
                            "S1",
                            "a@test.com",
                            true,
                            List.of()
                    );

            lenient()
                    .when(repository.findStudent("S1"))
                    .thenReturn(student);

            lenient()
                    .when(repository.courseExists("CS313"))
                    .thenReturn(false);
        }

        String result =
                service.register("S1", "CS313");

        assertEquals(expectedResult, result);
    }
}