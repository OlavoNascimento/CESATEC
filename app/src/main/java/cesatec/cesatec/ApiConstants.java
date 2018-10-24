package cesatec.cesatec;

public final class ApiConstants {
    // Value fields
    public static final String ENROLLMENTS_FIELD_ID = "id";
    public static final String ENROLLMENTS_FIELD_STUDENT_GROUP = "grupo";
    // Default value for the student group JSON field
    public static final String ENROLLMENTS_DEFAULT_STUDENT_GROUP = "A";
    // Nested resources
    public static final String ENROLLMENTS_NESTED_STUDENT = "aluno";
    public static final String ENROLLMENTS_NESTED_COURSE_SUB_GROUP = "turma";
    public static final String ENROLLMENTS_NESTED_AUTHORIZATIONS = "autorizacoes";
    /**
     * Course API resource
     * Used by EnrollmentDeserializer
     */
    // Value fields
    public static final String COURSE_FIELD_NAME = "nome";
    // Value fields
    public static final String REGISTRIES_FIELD_ENROLLMENT_ID = "matricula_id";
    public static final String REGISTRIES_FIELD_TYPE_ID = "tipo_registro_id";
    public static final String REGISTRIES_FIELD_DATE_TIME = "data_hora";
    /**
     * Students API resource
     * Used by StudentDeserializer
     */
    // Value fields
    public static final String STUDENTS_FIELD_NAME = "nome";
    public static final String STUDENTS_FIELD_ENROLL_ID = "ra";
    public static final String STUDENTS_FIELD_IMAGE = "imagem";
    /**
     * Authorizations API resource
     * Used by AuthorizationDeserializer
     */
    // Value fields
    public static final String AUTHORIZATIONS_FIELD_DATE_START = "vigencia_inicio";
    public static final String AUTHORIZATIONS_FIELD_DATE_END = "vigencia_fim";
    public static final String AUTHORIZATIONS_FIELD_TIME_START = "horario_inicio";
    public static final String AUTHORIZATIONS_FIELD_TIME_END = "horario_fim";
    public static final String AUTHORIZATIONS_FIELD_WEEKDAY = "dia_semana";
    public static final String AUTHORIZATIONS_FIELD_REGISTER_TYPE = "tipo_registro";
    /**
     * Registry Types API resource
     * Used by ApiCreateRegistryTask and AuthorizationDeserializer
     */
    // Value fields

    // Id that represents the type of an exit registry
    // Used by ApiCreateRegistryTask
    public static final int REGISTER_FIELD_TYPE_OUT_ID = 2;
    // Used by AuthorizationDeserializer
    public static final String REGISTRY_FIELD_TYPE_FIELD_DESCRIPTION = "descricao";
    // API base url
    private static final String API_URL = "https://portaria-api.herokuapp.com/api/";
    /**
     * Enrollment API resource
     * Used by EnrollmentDeserializer
     */
    // Resource name
    private static final String ENROLLMENTS_RESOURCE_NAME = "matriculas";
    // Endpoint
    public static final String API_ENDPOINT_ENROLLMENTS = API_URL + ENROLLMENTS_RESOURCE_NAME;
    /**
     * Registries API resource
     */
    // Resource name
    private static final String REGISTRIES_RESOURCE_NAME = "registros";
    // Endpoint
    public static final String API_REGISTRIES_ENDPOINT = API_URL + REGISTRIES_RESOURCE_NAME;
}
