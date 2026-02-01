package io.github.dohyeon0608.web.reservation.config.swagger;

import io.github.dohyeon0608.web.reservation.exception.ErrorCode;
import io.github.dohyeon0608.web.reservation.util.ApiErrorCodes;
import io.github.dohyeon0608.web.reservation.util.ApiResponse;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class SwaggerConfig implements OperationCustomizer {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Reservation System API")
                        .version("0.0.1")
                        .description("예약 시스템 API를 제공합니다."));
    }

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        ApiErrorCodes apiErrorCodes = handlerMethod.getMethodAnnotation(ApiErrorCodes.class);

        if (apiErrorCodes != null) {
            generateErrorCodeResponseExample(operation, apiErrorCodes.value());
        }
        return operation;
    }

    private void generateErrorCodeResponseExample(Operation operation, ErrorCode[] errorCodes) {
        io.swagger.v3.oas.models.responses.ApiResponses responses = operation.getResponses();

        for (ErrorCode errorCode : errorCodes) {
            String status = String.valueOf(errorCode.getStatus().value());
            Content content = new Content();
            MediaType mediaType = new MediaType();

            // ApiResponse 객체를 생성하여 예시 값 설정
            ApiResponse<String> errorResponse = ApiResponse.createError(errorCode);
            Example example = new Example();
            example.setValue(errorResponse);

            mediaType.addExamples(errorCode.name(), example);
            content.addMediaType("application/json", mediaType);

            io.swagger.v3.oas.models.responses.ApiResponse apiResponse = responses.get(status);

            if (apiResponse == null) {
                apiResponse = new io.swagger.v3.oas.models.responses.ApiResponse().content(content).description(errorCode.getStatus().getReasonPhrase());
                responses.addApiResponse(status, apiResponse);
            } else {
                apiResponse.getContent().get("application/json").addExamples(errorCode.name(), example);
            }
        }
    }
}
