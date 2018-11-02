package cesatec.cesatec;

public final class ApiConstants {
    /**
     * API base url
     */
    private static final String API_URL = "https://portaria-api.herokuapp.com/api/";

    /**
     * Registry Types API resource
     * Used by ApiCreateRegistryTask
     */
    public final class RegistryTypesResource {
        // Value fields
        // Id that represents the type of an exit registry
        // Used by ApiCreateRegistryTask
        // TODO Auto set using API value
        public static final int FIELD_TYPE_EXIT_ID = 2;
    }

    /**
     * Enrollment API resource
     * Used by EnrollmentDeserializer
     */
    public class EnrollmentResource {
        // Value fields
        public static final String FIELD_ID = "id";
        public static final String FIELD_STUDENT_GROUP = "grupo";
        // Nested resources
        public static final String NESTED_STUDENT = "aluno";
        public static final String NESTED_SUB_COURSE = "turma";
        public static final String NESTED_AUTHORIZATIONS = "autorizacoes";
        // Default value for the student group JSON field
        public static final String DEFAULT_STUDENT_GROUP = "A";
        // Resource name
        private static final String RESOURCE_NAME = "matriculas";
        // Endpoint
        public static final String API_ENDPOINT = API_URL + RESOURCE_NAME;
    }

    /**
     * Courses API resource
     * Used by EnrollmentDeserializer, ApiFetchCoursesTask
     * and CourseDeserializer
     */
    public class CoursesResource {
        // Value fields
        // Used by EnrollmentDeserializer
        public static final String FIELD_ID = "id";
        public static final String FIELD_NAME = "nome";
        // Resource name
        // Used by ApiFetchCoursesTask
        private static final String RESOURCE_NAME = "cursos";
        // Nested resources
        public static final String NESTED_SUB_COURSES = "turmas";
        // Endpoint
        // Used by ApiFetchCoursesTask
        public static final String API_ENDPOINT = API_URL + RESOURCE_NAME;
    }

    /**
     * SubCourses API resource
     * Used by SubCourseDeserializer and ApiFetchSubCourseTask
     */
    public class SubCoursesResource {
        // Value fields
        // Used by EnrollmentDeserializer
        public static final String FIELD_ID = "id";
        public static final String FIELD_PARENT_COURSE_ID = "curso_id";
        public static final String FIELD_NAME = "nome";
        // Nested resources
        public static final String NESTED_ENROLLMENTS = "matriculas";
        // Resource name
        // Used by ApiFetchSubCourseTask
        private static final String RESOURCE_NAME = "turmas";
        // Endpoint
        // Used by ApiFetchSubCourseTask
        public static final String API_ENDPOINT = API_URL + RESOURCE_NAME;
    }

    /**
     * Students API resource
     * Used by StudentDeserializer
     */
    public class StudentsResource {
        // Value fields
        public static final String FIELD_NAME = "nome";
        public static final String FIELD_ENROLL_ID = "ra";
        public static final String FIELD_IMAGE = "imagem";
    }

    /**
     * Authorizations API resource
     * Used by AuthorizationDeserializer
     */
    public class AuthorizationsResource {
        // Value fields
        public static final String FIELD_DATE_START = "vigencia_inicio";
        public static final String FIELD_DATE_END = "vigencia_fim";
        public static final String FIELD_TIME_START = "horario_inicio";
        public static final String FIELD_TIME_END = "horario_fim";
        public static final String FIELD_WEEKDAY = "dia_semana";
        public static final String FIELD_RESPONSIBLE = "responsavel";
    }

    /**
     * Registries API resource
     * Used by ApiCreateRegistryTask
     */
    public class RegistriesResource {
        // Value fields
        public static final String FIELD_ENROLLMENT_ID = "matricula_id";
        public static final String FIELD_TYPE_ID = "tipo_registro_id";
        public static final String FIELD_DATE_TIME = "data_hora";
        // Resource name
        private static final String RESOURCE_NAME = "registros";
        // Endpoint
        public static final String API_ENDPOINT = API_URL + RESOURCE_NAME;
    }
}
