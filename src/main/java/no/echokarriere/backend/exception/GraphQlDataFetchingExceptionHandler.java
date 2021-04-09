package no.echokarriere.backend.exception;

import com.netflix.graphql.dgs.exceptions.DefaultDataFetcherExceptionHandler;
import com.netflix.graphql.types.errors.TypedGraphQLError;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class GraphQlDataFetchingExceptionHandler implements DataFetcherExceptionHandler {
    private final DefaultDataFetcherExceptionHandler defaultHandler = new DefaultDataFetcherExceptionHandler();

    @Override
    public DataFetcherExceptionHandlerResult onException(DataFetcherExceptionHandlerParameters handlerParameters) {
        if (handlerParameters.getException() instanceof ResourceNotFoundException) {
            var error = TypedGraphQLError.newNotFoundBuilder()
                    .message(handlerParameters.getException().getMessage())
                    .path(handlerParameters.getPath())
                    .build();

            return DataFetcherExceptionHandlerResult.newResult(error).build();
        } else if (handlerParameters.getException() instanceof BadRequestException) {
            var error = TypedGraphQLError.newBadRequestBuilder()
                    .message(handlerParameters.getException().getMessage())
                    .path(handlerParameters.getPath())
                    .build();

            return DataFetcherExceptionHandlerResult.newResult(error).build();
        } else if (handlerParameters.getException() instanceof AccessDeniedException) {
            var error = TypedGraphQLError.newPermissionDeniedBuilder()
                    .message(handlerParameters.getException().getMessage())
                    .path(handlerParameters.getPath())
                    .build();

            return DataFetcherExceptionHandlerResult.newResult(error).build();
        } else {
            return defaultHandler.onException(handlerParameters);
        }
    }
}