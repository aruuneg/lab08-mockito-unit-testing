# Лаборатори №8: Mockito Mock Объектууд

## Оюутны мэдээлэл

- Нэр: ANAR BATBOLD
- Код: B170910823
- Хичээл: F.CSA313 Программ хангамжийн чанарын баталгаа ба тест

---

# Ашигласан орчин

## Java Version

```text
openjdk version "17.0.21.0.2" 2026-10-22 LTS
OpenJDK Runtime Environment Microsoft-15779939 (build 17.0.21.0.2+1-LTS)
OpenJDK 64-Bit Server VM Microsoft-15779939 (build 17.0.21.0.2+1-LTS, mixed mode, sharing)
```

## Maven Version

```text
Apache Maven 3.9.16
Maven home: C:\apache-maven-3.9.16
Java version: 17.0.21.0.2
```

## Ашигласан сангууд

- JUnit 5
- Mockito
- Mockito JUnit Jupiter

---

# Төслийн тайлбар

Энэхүү лабораторийн ажлаар CourseRegistrationService классын бизнес логикийг Mockito ашиглан тестэлсэн.

Гадаад хамаарлууд болох:

```text
StudentRepository
EmailSender
```

интерфейсүүдийг mock объектуудаар орлуулж, зөвхөн бизнес логикийг тусгаарлан шалгасан.

---

# Хэрэгжүүлсэн тестүүд

## A1 – Амжилттай бүртгэл

Зорилго:

- "OK" буцааж байгаа эсэх
- saveRegistration() дуудагдаж байгаа эсэх
- и-мэйл илгээгдэж байгаа эсэх

Mockito хэрэгсэл:

```text
when(...)
verify(...)
```

---

## A2 – Байхгүй оюутан

Хүлээгдэх үр дүн:

```text
ERROR_NO_STUDENT
```

Шалгасан зүйл:

```text
saveRegistration() дуудагдаагүй
emailSender.send() дуудагдаагүй
```

Mockito хэрэгсэл:

```text
never()
```

---

## A3 – Идэвхгүй оюутан

Хүлээгдэх үр дүн:

```text
ERROR_INACTIVE_STUDENT
```

Шалгасан зүйл:

```text
EmailSender-тэй ямар ч харилцаа үүсээгүй
```

Mockito хэрэгсэл:

```text
verifyNoInteractions(...)
```

---

## A4 – Prerequisite дутуу

Хүлээгдэх үр дүн:

```text
ERROR_PREREQUISITES
```

Шалгасан зүйл:

```text
saveRegistration() дуудагдаагүй
```

Mockito хэрэгсэл:

```text
never()
```

---

## A5 – Имэйл илгээх үед exception

Хүлээгдэх үр дүн:

```text
OK
```

Бүртгэл хадгалагдах ёстой.

Mockito хэрэгсэл:

```text
doThrow(...)
```

---

## A6 – Null оролтын шалгалт

Шалгасан нөхцөл:

```text
courseId == null
```

Хүлээгдэх үр дүн:

```text
IllegalArgumentException
```

Mockito хэрэгсэл:

```text
assertThrows(...)
```

---

## A7 – ArgumentCaptor

Илгээсэн и-мэйлийн текст дотор:

```text
CS313
```

байгаа эсэхийг шалгасан.

Mockito хэрэгсэл:

```text
ArgumentCaptor
```

---

## A8 – Parameterized Test

Нэг тестээр дараах алдааны ангиллуудыг шалгасан.

```text
ERROR_NO_STUDENT
ERROR_INACTIVE_STUDENT
ERROR_NO_COURSE
```

Mockito хэрэгсэл:

```text
@ParameterizedTest
@CsvSource
```

---

# lenient() ашигласан тайлбар

A8 параметржүүлсэн тестийн үед мөр бүр өөр төрлийн stub ашигладаг.

Mockito-ийн strict stubs горимд тухайн мөрөнд ашиглагдаагүй stub байгаа бол алдаа үүсэх боломжтой.

Иймээс зөвхөн шаардлагатай хэсгүүдэд:

```java
lenient().when(...)
```

ашигласан.

Энэ нь параметр бүр өөр замаар execute болох үед шаардлагагүй strict stubbing алдаанаас сэргийлсэн.

---

# Үндсэн тестийн үр дүн

Файл:

```text
results/mvn-test.txt
```

Үр дүн:

```text
Tests run: 10
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

## Хүснэгт

| Үзүүлэлт | Утга |
|----------|------|
| Tests | 10 |
| Failures | 0 |
| Errors | 0 |
| Skipped | 0 |
| Build | SUCCESS |

---

# Mutation Testing

## Mutant #1

Өөрчлөлт:

```java
if (!student.active())
    return "ERROR_INACTIVE_STUDENT";
```

мөрийг түр хугацаанд устгасан.

### Унасан тестүүд

```text
doesNotRegisterInactiveStudent
errorScenariosParameterizedTest(INACTIVE)
```

### Үр дүн

```text
BUILD FAILURE
```

---

## Mutant #2

Өөрчлөлт:

```java
repository.saveRegistration(...)
```

дуудлагыг устгасан.

### Унасан тестүүд

```text
savesRegistrationWhenValid
emailFailureDoesNotFailRegistration
```

### Үр дүн

```text
BUILD FAILURE
```

---

## Mutant #3

Өөрчлөлт:

```java
|| courseId == null
```

хэсгийг устгасан.

### Унасан тестүүд

```text
nullCourseIdShouldThrowException
```

### Үр дүн

```text
BUILD FAILURE
```

---

## Mutation Summary

| Mutant | Унасан тестүүд | Үр дүн |
|----------|----------|----------|
| #1 | doesNotRegisterInactiveStudent, errorScenariosParameterizedTest(INACTIVE) | BUILD FAILURE |
| #2 | savesRegistrationWhenValid, emailFailureDoesNotFailRegistration | BUILD FAILURE |
| #3 | nullCourseIdShouldThrowException | BUILD FAILURE |

---

# Дүгнэлт

Энэхүү лабораторийн ажлаар Mockito ашиглан гадаад хамаарлуудыг mock объектуудаар орлуулж нэгжийн тест бичлээ. StudentRepository болон EmailSender интерфейсүүдийг mock хийснээр CourseRegistrationService классын бизнес логикийг тусгаарлан шалгах боломжтой болсон. Mock объект нь зан төлөвийг программчлан тодорхойлдог бол fake объект нь бодит хэрэгжилтийн хялбаршуулсан хувилбар байдаг. Жишээлбэл, энэхүү лабораторид StudentRepository-г fake хэрэгжилтээр орлуулах боломжтой байсан боловч янз бүрийн нөхцөлүүдийг хялбар удирдахын тулд Mockito mock ашигласан. Бизнес логик буюу шалгаж буй үндсэн классыг өөрийг нь mock хийх ёсгүй бөгөөд зөвхөн гаднах хамаарлуудыг mock хийх нь зөв аргачлал юм. Мөн өгөгдлийн бүтэц, domain object зэрэг энгийн объектуудыг mock хийх шаардлагагүй бөгөөд бодит объект ашиглах нь тестийг ойлгомжтой болгодог. verify() аргыг хэтрүүлэн ашиглавал тест нь хэрэгжилтийн дотоод дэлгэрэнгүй зүйлсээс хэт хамааралтай болж, кодын жижиг refactor бүрт эвдэрдэг эрсдэлтэй. Иймээс зөвхөн бизнесийн хувьд чухал харилцан үйлчлэлүүдийг verify хийх нь зүйтэй. Parameterized test ашиглан олон алдааны ангиллыг нэг тестээр шалгаж, mutation testing ашиглан тестүүд бодит алдааг илрүүлж чадаж байгааг баталгаажуулсан. Гурван мутант тус бүрийг дор хаяж нэг тест амжилттай барьсан нь тестийн чанар болон хамралт хангалттай байгааг харууллаа.

---

# Хавсаргасан файлууд

```text
pom.xml

src/main/java/
src/test/java/

results/
├── mvn-test.txt
├── mvn-test-mutant-1.txt
├── mvn-test-mutant-2.txt
└── mvn-test-mutant-3.txt
```