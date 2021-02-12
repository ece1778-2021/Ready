# Ready

#### What
An app which allows math teachers to gauge high school students’ readiness for a unit test. It does so by allowing teachers to administer several diagnostic quizzes throughout a given unit. The students have to upload pictures of solutions and provide a final answer. The diagnostic quizzes are auto graded based on the final answers. From this, the teacher can get an initial sense of student readiness. Teachers can additionally look at students’ uploaded work to check for understanding and processes where needed; with this, teachers can intervene as needed. Parents are kept in the loop via automated text/email upon the teacher’s command. 

#### Why
Formative quizzes are currently done by pencil and paper. Then, they are marked and returned to students. Communication with parents is then initiated by phone or email. This not only takes time on the part of the teacher, but adds a time lag in terms of students and parents getting feedback. Auto-graded quizzes not only reduce teacher workload, but provide automated feedback to all interested parties. Ongoing communication with parents regarding student progress is paramount in keeping students on track, and fulfills school administrators’ instructions to regularly keep parents informed. 

#### StudentMainActivity.java
- Student registration page 
- xml file: activity_student_register.xml 

#### StudentMainActivity.java
- Display a list of assignments/quizzes given by the teacher. 
- Adapter class: StudentAssgListAdapter.java 
- xml file: activity_student_main.xml, student_assg_list_adapter.xml

### StudentAssignmentActivity.java
- Assignment upload page 
- AdapterClass: StudentAssgAdapter.java
- xml file: student_assg_adapter.xml

