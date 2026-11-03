\# Лаборатори №8: Mockito Mock Объектууд



\## Оюутны мэдээлэл



\- Нэр: ANAR BATBOLD

\- Код: B170910823

\- Хичээл: F.CSA313 Программ хангамжийн чанарын баталгаа ба тест



\---



\# Ашигласан орчин



\## Java Version



```text

java 21.0.12.1

```



\## Maven Version



```text

Apache Maven 3.9.16

```



\## Ашигласан сангууд



\- JUnit 5

\- Mockito

\- Mockito JUnit Jupiter



\---



\# Төслийн тайлбар



Энэхүү лабораторийн ажлаар CourseRegistrationService классын бизнес логикийг Mockito ашиглан тестэлсэн.



Гадаад хамаарлууд болох:



```text

StudentRepository

EmailSender

```



интерфейсүүдийг mock объектуудаар орлуулж, зөвхөн бизнес логикийг тусгаарлан шалгасан.



\---



\# Хэрэгжүүлсэн тестүүд



\## A1 – Амжилттай бүртгэл



Зорилго:



\- "OK" буцааж байгаа эсэх

\- saveRegistration() дуудагдаж байгаа эсэх

\- и-мэйл илгээгдэж байгаа эсэх



Mockito хэрэгсэл:



```text

when(...)

verify(...)

```



\---



\## A2 – Байхгүй оюутан



Хүлээгдэх үр дүн:



```text

ERROR\_NO\_STUDENT

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



\---



\## A3 – Идэвхгүй оюутан



Хүлээгдэх үр дүн:



```text

ERROR\_INACTIVE\_STUDENT

```



Шалгасан зүйл:



```text

EmailSender-тэй ямар ч харилцаа үүсээгүй

```



Mockito хэрэгсэл:



```text

verifyNoInteractions(...)

```



\---



\## A4 – Prerequisite дутуу



Хүлээгдэх үр дүн:



```text

ERROR\_PREREQUISITES

```



Шалгасан зүйл:



```text

saveRegistration() дуудагдаагүй

```



Mockito хэрэгсэл:



```text

never()

```



\---



\## A5 – Имэйл илгээх үед exception



Хүлээгдэх үр дүн:



```text

OK

```



Бүртгэл хадгалагдах ёстой.



Mockito хэрэгсэл:



```text

doThrow(...)

```



\---



\## A6 – Null оролтын шалгалт



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



\---



\## A7 – ArgumentCaptor



Илгээсэн и-мэйлийн текст дотор:



```text

CS313

```



байгаа эсэхийг шалгасан.



Mockito хэрэгсэл:



```text

ArgumentCaptor

```



\---



\## A8 – Parameterized Test



Нэг тестээр дараах алдааны ангиллуудыг шалгасан.



```text

ERROR\_NO\_STUDENT

ERROR\_INACTIVE\_STUDENT

ERROR\_NO\_COURSE

```



Mockito хэрэгсэл:



```text

@ParameterizedTest

@CsvSource

```



\---



\# lenient() ашигласан тайлбар



A8 параметржүүлсэн тестийн үед мөр бүр өөр төрлийн stub ашигладаг.



Mockito-ийн strict stubs горимд тухайн мөрөнд ашиглагдаагүй stub байгаа бол алдаа үүсэх боломжтой.



Иймээс зөвхөн шаардлагатай хэсгүүдэд:



```java

lenient().when(...)

```



ашигласан.



Энэ нь параметр бүр өөр замаар execute болох үед шаардлагагүй strict stubbing алдаанаас сэргийлсэн.



\---



\# Үндсэн тестийн үр дүн



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



\## Хүснэгт



| Үзүүлэлт | Утга |

|----------|------|

| Tests | 10 |

| Failures | 0 |

| Errors | 0 |

| Skipped | 0 |

| Build | SUCCESS |



\---



\# Mutation Testing



\## Mutant #1



Өөрчлөлт:



```java

if (!student.active())

&#x20;   return "ERROR\_INACTIVE\_STUDENT";

```



мөрийг түр хугацаанд устгасан.



\### Унасан тестүүд



```text

doesNotRegisterInactiveStudent

errorScenariosParameterizedTest(INACTIVE)

```



\### Үр дүн



```text

BUILD FAILURE

```



\---



\## Mutant #2



Өөрчлөлт:



```java

repository.saveRegistration(...)

```



дуудлагыг устгасан.



\### Унасан тестүүд



```text

savesRegistrationWhenValid

emailFailureDoesNotFailRegistration

```



\### Үр дүн



```text

BUILD FAILURE

```



\---



\## Mutant #3



Өөрчлөлт:



```java

|| courseId == null

```



хэсгийг устгасан.



\### Унасан тестүүд



```text

nullCourseIdShouldThrowException

```



\### Үр дүн



```text

BUILD FAILURE

```



\---



\## Mutation Summary



| Mutant | Унасан тестүүд | Үр дүн |

|----------|----------|----------|

| #1 | doesNotRegisterInactiveStudent, errorScenariosParameterizedTest(INACTIVE) | BUILD FAILURE |

| #2 | savesRegistrationWhenValid, emailFailureDoesNotFailRegistration | BUILD FAILURE |

| #3 | nullCourseIdShouldThrowException | BUILD FAILURE |



\---



\# Дүгнэлт



Энэхүү лабораторийн ажлаар Mockito ашиглан гадаад хамаарлуудыг mock объектуудаар орлуулж нэгжийн тест бичлээ. StudentRepository болон EmailSender интерфейсүүдийг бодит хэрэгжилтгүйгээр тусгаарлан турших боломжтой болсон. when(), verify(), never() болон verifyNoInteractions() ашиглан хамаарлуудын зан төлөвийг шалгасан. doThrow() ашиглан exception нөхцөлд сервис хэрхэн ажиллахыг туршсан. ArgumentCaptor ашиглан илгээгдсэн и-мэйлийн агуулгыг шалгасан. Parameterized test ашиглан олон алдааны ангиллыг нэг тестээр үр ашигтай шалгасан. Дараа нь кодод зориудаар алдаа оруулж mutation тест хийсэн. Гурван өөр мутант бүрийг тестүүд амжилттай илрүүлсэн бөгөөд BUILD FAILURE болсон. Энэ нь бичсэн тестүүд зөвхөн код ажиллаж байгааг бус, бодит алдааг илрүүлэх чадвартай болохыг харуулсан. Mockito нь гадаад хамаарлаас тусгаарлагдсан, хурдан бөгөөд найдвартай нэгжийн тест боловсруулах үр дүнтэй хэрэгсэл болохыг ойлголоо.



\---



\# Хавсаргасан файлууд



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

