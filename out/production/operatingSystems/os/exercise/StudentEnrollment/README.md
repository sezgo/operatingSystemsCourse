Task: Student Enrollment
-   The enrollment process at one university is being done by skilled members of the Enrollment comission.
-   On the day of enrollment, in one room there can be at most 4 members of the comission at the same time.
-   Each of these commission members enrols 10 candidates, one by on, after which he leaves the room and allows
 another member of the Enrollment commission to enter.
 -  One candidate is enrolled by one of the commission members.
 -  One commission member can enroll one candidate at a given time.
 -  When a commission member has a candidate present, he enrolls the candidate using the virtual method `enroll()`.
    -   Before this happens, the candidate has to call the method `presentDocuments()`.
    -   When this method is being executed, the candidate has to be present in the room. After that, he's free to go.
-   Write a program which will help in this enrollment process, by using semaphores.
    -   Each commission member is a seperate thread in the system, and so is each candidate;
    -   The commission members calls the `commissionEnrollment()` method, and the candidates call the
     `candidateEnrollment` method.
    -   Write these two methods.